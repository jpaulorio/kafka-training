package com.thoughtworks.consumers

import java.time.Duration
import java.util.Properties

import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import org.apache.kafka.clients.consumer.ConsumerConfig

import scala.collection.JavaConverters._

object AvroConsumer {

  def consumeSales(topic: String, brokers: String) = {
    val props = new Properties
    props.put("bootstrap.servers", brokers)
    props.put("group.id", "OrdersConsumer")
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "io.confluent.kafka.serializers.KafkaAvroDeserializer")
    props.put("schema.registry.url", "http://ae34acbe5ed9b11e8810a0a4e9b68c10-2021023861.us-east-1.elb.amazonaws.com:8081")
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

    val kafkaConsumer = new KafkaConsumer[String, GenericRecord](props)
    kafkaConsumer.subscribe(List(topic).asJava)

    while (true) {
      val results = kafkaConsumer.poll(Duration.ofMillis(100))
      results.forEach(record => println(s"Consuming record ${record.value()}"))
    }
  }
}
