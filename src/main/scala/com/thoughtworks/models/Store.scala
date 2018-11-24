package com.thoughtworks.models

import java.util.UUID

case class Store(id:String, name:String) {
  def storeToCSVString(): String = {
    s"""$id;$name;"""
  }
}

object Store {
  def getAvailableStores(): List[Store] = {
    List(
      Store(UUID.randomUUID().toString, "Loja Sul"),
      Store(UUID.randomUUID().toString, "Loja Norte"),
      Store(UUID.randomUUID().toString, "Loja Leste"),
      Store(UUID.randomUUID().toString, "Loja Oeste")
    )
  }
}
