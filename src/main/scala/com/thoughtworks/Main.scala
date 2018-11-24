package com.thoughtworks

import com.thoughtworks.consumers.{AvroConsumer, OrdersConsumer}
import com.thoughtworks.producers.{AvroProducer, OrdersProducer}
import com.thoughtworks.streams.OrdersProcessor

object Main {
  def main(args: Array[String]): Unit = {
    val mode = args(0)
    var recordCount = 0
    if (args.length > 1) {
      recordCount = Integer.parseInt(args(1))
    }

    val topic = "orders"
    val avroTopic = "orders-avro"
    val brokers =
      "a41eb0771ed9211e8aa15029578fb917-1813474711.us-east-1.elb.amazonaws.com:9092, " +
        "a42059552ed9211e8aa15029578fb917-1584479926.us-east-1.elb.amazonaws.com:9092" +
        "a421f0772ed9211e8aa15029578fb917-1025101154.us-east-1.elb.amazonaws.com:9092"

    if (mode == "producer" || mode == "p") {
      println(s"Producing $recordCount records on topic $topic...")
      (1 to recordCount).foreach(_ => OrdersProducer.generateSale(topic, brokers))
    } else if (mode == "consumer" || mode == "c") {
      println(s"Consumer records from topic $topic...")
      OrdersConsumer.consumeSales(topic, brokers)
    } else if (mode == "streams" || mode == "s") {
      OrdersProcessor.processOrders(topic, brokers)
    } else if (mode == "avro-producer" || mode == "ap") {
      (1 to recordCount).foreach(_ => AvroProducer.generateSale(avroTopic, brokers))
    } else if (mode == "avro-consumer" || mode == "ac") {
      AvroConsumer.consumeSales(avroTopic, brokers)
    }

    println("Application exited.")
  }
}
