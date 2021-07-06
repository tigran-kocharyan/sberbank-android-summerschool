package ru.totowka.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.totowka.ui.R
import ru.totowka.ui.adapter.MeasureAdapter
import ru.totowka.ui.model.Measurements

class MainActivity : AppCompatActivity() {
    lateinit var recycler: RecyclerView

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recycler = findViewById(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(this, RecyclerView.VERTICAL)
        dividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.shape_divider))
        recycler.addItemDecoration(dividerItemDecoration)
        recycler.adapter =  MeasureAdapter(createMeasures())
    }

    fun createMeasures() : ArrayList<Measurements> {
        val list = ArrayList<Measurements>()
        for (i in 0..24) {
            list.add(Measurements.AREA)
            list.add(Measurements.LENGTH)
        }
        return list
    }
}