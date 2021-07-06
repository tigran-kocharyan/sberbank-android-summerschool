package ru.totowka.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.totowka.ui.R
import ru.totowka.ui.controller.Converter
import ru.totowka.ui.model.Measurements
import ru.totowka.ui.view.MeasureActivity

class MeasureAdapter(val list: ArrayList<Measurements>) :
    RecyclerView.Adapter<MeasureAdapter.MeasureViewHolder>() {

    inner class MeasureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var title: TextView = itemView.findViewById(R.id.measure_title)
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            when (title.text) {
                Measurements.LENGTH.title -> {
                    val intent = Intent(itemView.context, MeasureActivity::class.java)
                    intent.putExtra("measures", Converter.coefLength)
                    v?.context?.startActivity(intent)
                }
                Measurements.AREA.title -> {
                    val intent = Intent(itemView.context, MeasureActivity::class.java)
                    intent.putExtra("measures", Converter.coefArea)
                    v?.context?.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeasureViewHolder {
        return MeasureViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_measure, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MeasureViewHolder, position: Int) {
        holder.title.text = list[position].title
    }

    override fun getItemCount() = list.size
}