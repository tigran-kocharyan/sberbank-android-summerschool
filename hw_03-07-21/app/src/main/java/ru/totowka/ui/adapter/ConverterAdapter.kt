package ru.totowka.ui.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.totowka.ui.R
import ru.totowka.ui.controller.ConverterDiffUtilCallback
import ru.totowka.ui.model.MeasureValue
import ru.totowka.ui.util.MeasureTextWatcher
import java.lang.NumberFormatException
import java.util.*
import kotlin.collections.ArrayList


class ConverterAdapter(var list: ArrayList<MeasureValue>, var watcher: MeasureTextWatcher) :
    RecyclerView.Adapter<ConverterAdapter.ConverterViewHolder>() {

    inner class ConverterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.unit_title)
        var value: EditText = itemView.findViewById(R.id.unit_value)

        fun setListeners() {
            value.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(text: Editable?) {
                    try {
                        if(text.toString() == "") {
                            watcher.onChange(list, 0.0, adapterPosition)
                        } else {
                            watcher.onChange(list, text.toString().toDouble(), adapterPosition)
                        }
                    } catch (exception: NumberFormatException) {
                        return
                    }
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(str: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConverterViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_converter, parent, false)
        return ConverterViewHolder(itemView).apply { setListeners() }
    }

    override fun onBindViewHolder(holder: ConverterViewHolder, position: Int) {
        holder.title.text = list[position].converter.title
        holder.value.setText(list[position].value.toString())
        holder.value.setOnFocusChangeListener { v, hasFocus ->
            run {
                if (holder.adapterPosition == 0) {
                    return@setOnFocusChangeListener
                }
                if (hasFocus) {
                    Collections.swap(list, 0, holder.adapterPosition)
                    list = ArrayList(list)
                    notifyItemMoved(holder.adapterPosition, 0)
                }
            }
        }
    }

    override fun getItemCount() = list.size

    fun update(news: ArrayList<MeasureValue>) {
        val diffResult = DiffUtil.calculateDiff(ConverterDiffUtilCallback(this.list, news))
        this.list = ArrayList(news)
        diffResult.dispatchUpdatesTo(this)
    }
}