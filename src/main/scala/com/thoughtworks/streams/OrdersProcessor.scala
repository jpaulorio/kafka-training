package com.thoughtworks.streams

import java.time.format.DateTimeFormatter
import java.time.{Duration, ZonedDateTime}
import java.util.Properties

import com.thoughtworks.models.{Order, OrderItem}
import org.apache.kafka.streams.kstream.Printed
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream._
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}

object OrdersProcessor {

  import org.apache.kafka.streams.scala.ImplicitConversions._
  import org.apache.kafka.streams.scala.Serdes._

  def getItems(value: String): List[OrderItem] = {
    val items = value.split("\\|")
    items.map(item => {
      val fields = item.split(",")
      OrderItem(fields(0), fields(1), fields(2).toDouble, fields(3).toInt)
    }).toList
  }

  def processOrders(topic: String, bootstrapServers: String): Unit = {
    val props = new Properties()
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "orders-stream-processing");
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

    val builder = new StreamsBuilder()
    val source: KStream[String, String] = builder.stream[String, String](topic)

    val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ")

    val ordersWithItems = source
      .mapValues(value => value.split(";"))
      .mapValues(value => Order(value(0), value(1), ZonedDateTime.parse(value(2), dateTimeFormatter), value(3), getItems(value(4))))

    val orderTotals: KTable[String, Double] = ordersWithItems
      .mapValues(order => order.items.map(item => item.discount * item.quantity).reduce(_+_))
      .groupBy((store, _) => store)
      .reduce(_ + _)

    orderTotals.toStream.to("order-totals")

    orderTotals.toStream.print(Printed.toSysOut[String, Double])

    val streams = new KafkaStreams(builder.build(), props)

    streams.cleanUp()

    streams.start()

    sys.ShutdownHookThread {
      streams.close(Duration.ofSeconds(10))
    }
  }
}
