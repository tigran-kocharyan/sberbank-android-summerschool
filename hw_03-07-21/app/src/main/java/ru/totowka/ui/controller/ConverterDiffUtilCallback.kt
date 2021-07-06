package ru.totowka.ui.controller

import androidx.recyclerview.widget.DiffUtil
import ru.totowka.ui.model.MeasureValue

class ConverterDiffUtilCallback(
    val oldList: List<MeasureValue>,
    val newList: List<MeasureValue>
) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old: MeasureValue = oldList[oldItemPosition]
        val new: MeasureValue = newList[newItemPosition]
        return old === new
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old: MeasureValue = oldList[oldItemPosition]
        val new: MeasureValue = newList[newItemPosition]
        return (old.converter.title == new.converter.title &&
                old.converter.toBaseRate == new.converter.toBaseRate &&
                old.converter.fromBaseRate == new.converter.fromBaseRate &&
                old.value == new.value)
    }
}