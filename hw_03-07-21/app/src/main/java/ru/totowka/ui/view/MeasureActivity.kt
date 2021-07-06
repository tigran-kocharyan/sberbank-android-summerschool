package ru.totowka.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.totowka.ui.R
import ru.totowka.ui.adapter.ConverterAdapter
import ru.totowka.ui.controller.Converter
import ru.totowka.ui.model.MeasureItem
import ru.totowka.ui.model.MeasureValue
import ru.totowka.ui.util.MeasureTextWatcher

class MeasureActivity : AppCompatActivity() {
    lateinit var recycler: RecyclerView
    lateinit var measures: MeasureItem
    lateinit var storageAdapter: ConverterAdapter
    var values = ArrayList<MeasureValue>()
    val converter = Converter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        measures = intent.getParcelableExtra("measures")!!
        for (measure in measures.units) {
            values.add(MeasureValue(measure, 1.0))
        }

        values = converter.convert(values)

        recycler = findViewById(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(this)
        storageAdapter = ConverterAdapter(values, object : MeasureTextWatcher {
            override fun onChange(items: ArrayList<MeasureValue>, value: Double, index: Int) {
                if(!recycler.isComputingLayout) {
                    items[index].value = value
                    values = converter.convert(items)
                    storageAdapter.update(values)
                }
            }
        })
        recycler.adapter = storageAdapter
    }
}