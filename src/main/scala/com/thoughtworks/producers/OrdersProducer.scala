package com.thoughtworks.producers

import java.util.Properties

import com.thoughtworks.models.{Order, Product, Store}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

object OrdersProducer {

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
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    val kafkaProducer = new KafkaProducer[String,String](props)

    val order = Order.generateRandom(availableStores, availableProducts)
    val orderWithItemsCSV = generateOrderWithItemsCSV(order)

    val record = new ProducerRecord[String,String](topic, order.storeId, orderWithItemsCSV)
    kafkaProducer.send(record)
  }
}