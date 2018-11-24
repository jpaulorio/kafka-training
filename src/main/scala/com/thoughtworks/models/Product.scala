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
      Product("123", "Bicicleta", "Esporte & Lazer", 234.56),
      Product("234", "Bola de Futebol", "Esporte & Lazer", 34.56),
      Product("345", "Televisão", "Eletrônicos", 234.56),
      Product("456", "Celular", "Eletrônicos", 1234.56),
      Product("567", "Geladeira", "Eletrodomésticos", 2465.67),
      Product("678", "Fogão", "Eletrodomésticos", 356.78),
      Product("789", "Videogame", "Eletrônicos", 3567.89),
      Product("890", "Camisa", "Vestuário", 56.79),
      Product("901", "Calça", "Vestuário", 89.01),
      Product("012", "Vinho", "Bebidas", 67.89)
    )
  }
}
