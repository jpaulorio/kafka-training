package com.thoughtworks.models

import java.util.concurrent.ThreadLocalRandom

import scala.util.Random

case class OrderItem(orderId:String, productId:String, discount:Double, quantity:Integer) {
  def itemToCSVString(): String = {
    s"""${this.orderId};${this.productId};${this.discount};${this.quantity}\n"""
  }

}

object OrderItem {
  def generateRandom(order:Order, availableProducts:List[Product]):OrderItem = {
    val orderId = order.id
    val product = availableProducts(Random.nextInt(availableProducts.size))
    val productId = product.id
    val discount = ThreadLocalRandom.current().nextDouble(0, product.price / 5)
    val quantity = Random.nextInt(10) + 1
    OrderItem(orderId, productId, discount, quantity)
  }
}