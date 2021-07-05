package ru.totowka.recyclerview.model.type

import ru.totowka.recyclerview.model.util.GreedException

data class Basket(val title: String = "Корзина") : StorageItem {
    var apples: ArrayList<Apple> = ArrayList()

    constructor(title: String, apples: ArrayList<Apple> = ArrayList()) : this(title) {
        if(apples.size > 3) {
            throw IllegalArgumentException("Basket size should be <= 3")
        }
        this.apples = apples
    }

    fun addApple(apple: Apple) {
        if(apples.size == 3) {
            throw GreedException("Greedy!")
        }
        apples.add(apple)
    }
}

