package ru.totowka.ui.controller

import ru.totowka.ui.model.MeasureConverter
import ru.totowka.ui.model.MeasureItem
import ru.totowka.ui.model.MeasureValue
import ru.totowka.ui.model.Measurements
import java.math.RoundingMode

class Converter {
    companion object {
        var coefArea = MeasureItem(
            Measurements.AREA.title, arrayListOf(
                MeasureConverter("Кв. Метр", 1.0, 1.0),
                MeasureConverter("Кв. Километр", 1000000.0, 0.000001),
                MeasureConverter("Гектар", 10000.0, 0.0001),
                MeasureConverter("Кв. Cантиметр", 0.0001, 10000.0)))

        var coefLength = MeasureItem(
            Measurements.AREA.title, arrayListOf(
            MeasureConverter("Метр", 1.0, 1.0),
            MeasureConverter("Километр", 1000.0, 0.001),
            MeasureConverter("Миля", 1609.344, 0.0006214),
            MeasureConverter("Сантиметр", 0.01, 100.0),
            MeasureConverter("Милиметр", 0.001, 1000.0),
            MeasureConverter("Микрометр", 0.000001, 100000.0)))
    }

    fun convert(measures: List<MeasureValue>) : ArrayList<MeasureValue> {
        var mutable = measures.toMutableList()
        val base = mutable.removeFirst()
        val temp = base.value * base.converter.toBaseRate
        for (i in mutable.indices) {
            val new = (temp * mutable[i].converter.fromBaseRate)
                .toBigDecimal().setScale(2, RoundingMode.UP).toDouble()
            mutable[i] = mutable[i].copy(value = new)
        }
        mutable.sortBy { it.value }
        mutable.add(0, base)
        return ArrayList(mutable)
    }
}