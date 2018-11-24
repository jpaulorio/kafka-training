package com.thoughtworks.producers

import java.time.format.DateTimeFormatter
import java.util.Properties

import com.thoughtworks.models.{Order, Product, Store}
import org.apache.avro.Schema
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.avro
import scala.collection.JavaConverters._
import org.apache.avro.generic.GenericData
import org.apache.kafka.clients.producer.ProducerRecord

import scala.collection.mutable.ListBuffer
import scala.io.Source

object AvroProducer {
  val availableStores = Store.getAvailableStores()

  val availableProducts = Product.getAvailableProducts()

  def generateOrderWithItemsCSV(order:Order) = {
    val orderString = order.orderToCSVString().replace("\n", "")
    val orderItemsString = order.itemsToCSVString().mkString("|")
      .replace(';', ',').replace("\n", "")
    orderString + ";" + orderItemsString
  }

  def generateSale(topic:String, brokers:String) = {
    val props = new Properties
    props.put("bootstrap.servers", brokers)
    props.put("client.id", "Producer")
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaAvroSerializer")
    props.put("schema.registry.url", "http://ae34acbe5ed9b11e8810a0a4e9b68c10-2021023861.us-east-1.elb.amazonaws.com:8081")

    val kafkaProducer = new KafkaProducer[String,GenericData.Record](props)

    val order = Order.generateRandom(availableStores, availableProducts)
    val orderWithItemsCSV = generateOrderWithItemsCSV(order)

    val pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
    val formatter = DateTimeFormatter.ofPattern(pattern)

    val parser = new Schema.Parser()
    val orderSchema = Source.fromResource("schemas/order.avsc").mkString
    val schema = parser.parse(orderSchema)
    val avroRecord = new GenericData.Record(schema)
    avroRecord.put("id", order.id)
    avroRecord.put("customerId", order.customerId)
    avroRecord.put("storeId", order.storeId)
    avroRecord.put("timestamp", order.timestamp.format(formatter))


    val childSchema = avroRecord.getSchema().getField("items").schema().getElementType
    val items = ListBuffer[GenericData.Record]()

    order.items.foreach(item => {
      val itemRecord = new GenericData.Record(childSchema)
      itemRecord.put("orderId", item.orderId)
      itemRecord.put("productId", item.productId)
      itemRecord.put("quantity", item.quantity)
      itemRecord.put("discount", item.discount)
      items += itemRecord
    })

    avroRecord.put("items", items.toList.asJavaCollection)

    val record = new ProducerRecord[String, GenericData.Record](topic, order.storeId, avroRecord)

    kafkaProducer.send(record)
  }
}
