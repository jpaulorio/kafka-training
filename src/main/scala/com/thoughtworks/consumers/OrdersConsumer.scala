package com.thoughtworks.consumers

import java.time.Duration
import java.util.Properties

import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}

import scala.collection.JavaConverters._

object OrdersConsumer {
  def consumeSales(topic: String, brokers: String) = {
    val props = new Properties
    props.put("bootstrap.servers", brokers)
    props.put("group.id", "OrdersConsumer")
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

    val kafkaConsumer = new KafkaConsumer[String, String](props)
    kafkaConsumer.subscribe(List(topic).asJava)

    while (true) {
      val results = kafkaConsumer.poll(Duration.ofMillis(100))
      results.forEach(record => println(s"Consuming record ${record.value()}"))
    }
  }

}
