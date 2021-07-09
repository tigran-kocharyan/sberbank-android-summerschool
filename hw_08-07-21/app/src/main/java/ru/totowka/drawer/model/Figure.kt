package ru.totowka.drawer.model

import android.graphics.Canvas
import android.graphics.Paint

interface Figure {
    fun draw(canvas: Canvas, paint: Paint)
}