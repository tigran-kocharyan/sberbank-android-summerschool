package ru.totowka.drawer.model

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF


class Box(val origin: PointF, var current: PointF, val color: Int) : Figure {
    override fun draw(canvas: Canvas, paint: Paint) {
        paint.color = this.color
        var new = Paint()
        new.apply {

        }
        val left: Float = origin.x.coerceAtMost(current.x)
        val right: Float = origin.x.coerceAtLeast(current.x)
        val top: Float = origin.y.coerceAtMost(current.y)
        val bottom: Float = origin.y.coerceAtLeast(current.y)
        canvas.drawRect(left, top, right, bottom, paint)
    }
}