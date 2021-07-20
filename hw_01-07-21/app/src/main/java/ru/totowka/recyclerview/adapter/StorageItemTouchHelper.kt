package ru.totowka.recyclerview.adapter

interface StorageItemTouchHelper {
    fun onItemMove(fromPosition: Int, toPosition: Int) : Boolean

    fun onItemDismiss(position: Int)
}