package ru.totowka.recyclerview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.totowka.recyclerview.R
import ru.totowka.recyclerview.controller.StorageDiffUtilCallback
import ru.totowka.recyclerview.model.type.Apple
import ru.totowka.recyclerview.model.type.Basket
import ru.totowka.recyclerview.model.type.StorageItem
import ru.totowka.recyclerview.model.type.Sum
import ru.totowka.recyclerview.model.util.StorageException
import ru.totowka.recyclerview.view.StorageViewClickListener
import java.lang.ref.WeakReference


class StorageAdapter(private var items: List<StorageItem>, private val listener: StorageViewClickListener, private val itemHelper: StorageItemTouchHelper) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), StorageItemTouchHelper {

    fun reload(newStorage: ArrayList<StorageItem>) {
        val diffResult = DiffUtil.calculateDiff(StorageDiffUtilCallback(this.items, newStorage))
        this.items = ArrayList(newStorage)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class AppleViewHolder(itemView: View, listener: StorageViewClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var appleTitle: TextView? = null
        var appleButton: Button? = null
        var listener: WeakReference<StorageViewClickListener>

        init {
            appleTitle = itemView.findViewById(R.id.apple_title)
            appleButton = itemView.findViewById(R.id.button_eat_apple)
            this.listener = WeakReference(listener)
            appleButton?.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.get()?.onClick(v, adapterPosition)
        }
    }

    inner class BasketViewHolder(itemView: View, listener: StorageViewClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var basketTitle: TextView? = null
        var basketButton: Button? = null
        var listener: WeakReference<StorageViewClickListener>

        init {
            basketTitle = itemView.findViewById(R.id.basket_title)
            basketButton = itemView.findViewById(R.id.button_add_apple)
            this.listener = WeakReference(listener)
            basketButton?.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.get()?.onClick(v, adapterPosition)
        }
    }

    inner class SumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var sumTitle: TextView? = null
        var sumValue: TextView? = null

        init {
            sumTitle = itemView.findViewById(R.id.sum_title)
            sumValue = itemView.findViewById(R.id.sum_value)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> AppleViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.view_apple, parent, false), listener)
            1 -> BasketViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.view_basket, parent, false), listener)
            2 -> SumViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.view_sum, parent, false))
            else -> throw StorageException("Wrong Type in storage")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is Apple -> 0
            is Basket -> 1
            is Sum -> 2
            else -> throw StorageException("Wrong Type in storage")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            0 -> {
                val appleHolder = holder as AppleViewHolder
                val apple = items[position] as Apple
                appleHolder.appleTitle?.text = apple.title
            }
            1 -> {
                val basketHolder = holder as BasketViewHolder
                val basket = items[position] as Basket
                basketHolder.basketTitle?.text = basket.title
            }
            2 -> {
                val sumHolder = holder as SumViewHolder
                val sum = items[position] as Sum
                sumHolder.sumValue?.text = sum.apples.toString()
            }

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) : Boolean {
        return itemHelper.onItemMove(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        itemHelper.onItemDismiss(position)
    }
}