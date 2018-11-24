package com.thoughtworks.models

import java.util.UUID

case class Product(id:String, name:String, category:String, price:Double) {
  def productToCSVString(): String = {
    s"""${this.id};${this.name};${this.category};${this.price}\n"""
  }
}

object Product {
  def getAvailableProducts(): List[Product] = {
    List(
      Product(UUID.randomUUID().toString, "Bicicleta", "Esporte & Lazer", 234.56),
      Product(UUID.randomUUID().toString, "Bola de Futebol", "Esporte & Lazer", 34.56),
      Product(UUID.randomUUID().toString, "Televisão", "Eletrônicos", 234.56),
      Product(UUID.randomUUID().toString, "Celular", "Eletrônicos", 1234.56),
      Product(UUID.randomUUID().toString, "Geladeira", "Eletrodomésticos", 2465.67),
      Product(UUID.randomUUID().toString, "Fogão", "Eletrodomésticos", 356.78),
      Product(UUID.randomUUID().toString, "Videogame", "Eletrônicos", 3567.89),
      Product(UUID.randomUUID().toString, "Camisa", "Vestuário", 56.79),
      Product(UUID.randomUUID().toString, "Calça", "Vestuário", 89.01),
      Product(UUID.randomUUID().toString, "Vinho", "Bebidas", 67.89)
    )
  }
}
