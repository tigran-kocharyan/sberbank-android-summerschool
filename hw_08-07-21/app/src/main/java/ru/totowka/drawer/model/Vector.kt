package ru.totowka.drawer.model

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF

class Vector(val start: PointF, var end: PointF, val color: Int) : Figure {
    override fun draw(canvas: Canvas, paint: Paint) {
        paint.color = color
        canvas.drawLine(start.x, start.y, end.x, end.y, paint)
    }
}