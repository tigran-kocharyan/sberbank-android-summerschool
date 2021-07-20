package ru.totowka.recyclerview.controller

import ru.totowka.recyclerview.model.type.Apple
import ru.totowka.recyclerview.model.type.Basket
import ru.totowka.recyclerview.model.type.StorageItem
import ru.totowka.recyclerview.model.type.Sum

class StorageRepository {
    fun store(baskets: List<Basket>): ArrayList<StorageItem> {
        val storage = ArrayList<StorageItem>()
        var sum = 0
        for (basket in baskets) {
            storage.add(basket)
            for (apple in basket.apples) {
                storage.add(apple)
                sum++
            }
        }
        storage.add(Sum(sum))
        return storage
    }

    fun onClick(storage: List<StorageItem>, position: Int): ArrayList<StorageItem> {
        var element = storage[position]
        when (element) {
            is Basket -> {
                element.addApple(Apple())
            }
            is Apple -> {
                var iterator = position
                while (storage[iterator] !is Basket) {
                    iterator--
                }
                ((storage[iterator] as Basket).apples).removeAt(0)
            }
        }
        var baskets = ArrayList<Basket>()
        for (item in storage) {
            if (item is Basket) {
                baskets.add(item)
            }
        }
        return store(baskets)
    }

    fun onSwipe(storage: List<StorageItem>, position: Int): ArrayList<StorageItem> {
        val updatedStorage = storage.toMutableList()
        var element = updatedStorage[position]
        when (element) {
            is Basket -> {
                updatedStorage.removeAt(position)
            }
            is Apple -> {
                var iterator = position
                while (updatedStorage[iterator] !is Basket) {
                    iterator--
                }
                ((updatedStorage[iterator] as Basket).apples).removeAt(0)
            }
        }
        val baskets = ArrayList<Basket>()
        for (item in updatedStorage) {
            if (item is Basket) {
                baskets.add(item)
            }
        }
        return store(baskets)
    }

    fun onMove(
        storage: List<StorageItem>,
        fromPosition: Int,
        toPosition: Int
    ): ArrayList<StorageItem> {
        val updatedStorage = storage.toMutableList()
        when (updatedStorage[toPosition]) {
            is Apple -> {
                var iterator = toPosition
                while (updatedStorage[iterator] !is Basket) {
                    iterator--
                }
                (updatedStorage[iterator] as Basket).addApple(updatedStorage[fromPosition] as Apple)
                iterator = fromPosition
                while (updatedStorage[iterator] !is Basket) {
                    iterator--
                }
                (updatedStorage[iterator] as Basket).apples.removeAt(0)
            }
            is Basket -> {
                (updatedStorage[toPosition] as Basket).addApple(updatedStorage[fromPosition] as Apple)
                var iterator = fromPosition
                while (updatedStorage[iterator] !is Basket) {
                    iterator--
                }
                (updatedStorage[iterator] as Basket).apples.removeAt(0)
            }
        }
        val baskets = ArrayList<Basket>()
        for (item in updatedStorage) {
            if (item is Basket) {
                baskets.add(item)
            }
        }
        return store(baskets)
    }

    fun addBasket(storage: List<StorageItem>): ArrayList<StorageItem> {
        var baskets = ArrayList<Basket>()
        for (item in storage) {
            if (item is Basket) {
                baskets.add(item)
            }
        }
        baskets.add(Basket())
        return store(baskets)
    }

    fun deleteAll() = ArrayList<StorageItem>()
}