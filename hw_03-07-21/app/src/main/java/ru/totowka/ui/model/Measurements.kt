package ru.totowka.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class MeasureValue(val converter: MeasureConverter, var value: Double)

@Parcelize
data class MeasureConverter(val title: String, val toBaseRate: Double, val fromBaseRate: Double) : Parcelable

@Parcelize
data class MeasureItem(val title: String, val units: ArrayList<MeasureConverter>) : Parcelable

enum class Measurements(val title: String) {
    LENGTH("Длина"),
    AREA("Площадь")
}