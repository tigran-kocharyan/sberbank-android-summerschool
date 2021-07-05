package ru.totowka.recyclerview.controller

import androidx.recyclerview.widget.DiffUtil
import ru.totowka.recyclerview.model.type.Apple
import ru.totowka.recyclerview.model.type.Basket
import ru.totowka.recyclerview.model.type.StorageItem
import ru.totowka.recyclerview.model.type.Sum

class StorageDiffUtilCallback(
    val oldList: List<StorageItem>,
    val newList: List<StorageItem>
) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old: StorageItem = oldList[oldItemPosition]
        val new: StorageItem = newList[newItemPosition]
        return old === new
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old: StorageItem = oldList[oldItemPosition]
        val new: StorageItem = newList[newItemPosition]
        if (old::class == new::class) {
            when (old::class) {
                Basket::class -> {
                    val olds = (old as Basket)
                    val news = (new as Basket)
                    if (olds.title == news.title &&
                        olds.apples.size == news.apples.size &&
                        compareApples(olds.apples, news.apples)
                    ) {
                        return true
                    }
                }
                Apple::class -> {
                    return (old as Apple).title == (new as Apple).title
                }
                Sum::class -> {
                    return (old as Sum).apples == (new as Sum).apples
                }
            }
        }
        return false
    }

    private fun compareApples(olds: ArrayList<Apple>, news: ArrayList<Apple>): Boolean {
        for (i in olds.indices) {
            if (olds[i] != news[i]) {
                return false
            }
        }
        return true
    }
}