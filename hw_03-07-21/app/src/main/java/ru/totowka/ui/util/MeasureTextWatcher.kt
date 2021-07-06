package ru.totowka.ui.util

import ru.totowka.ui.model.MeasureValue

interface MeasureTextWatcher {
    fun onChange(items: ArrayList<MeasureValue>, value: Double, index: Int)
}