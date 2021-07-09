package ru.totowka.drawer.model

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path

class Curve(var curve: Path, val color: Int) : Figure {
    override fun draw(canvas: Canvas, paint: Paint) {
        paint.color = color
        canvas.drawPath(curve, paint)
    }
}