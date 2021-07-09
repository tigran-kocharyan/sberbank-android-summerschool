package ru.totowka.drawer

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.View
import ru.totowka.drawer.model.Box
import ru.totowka.drawer.model.DrawType
import ru.totowka.drawer.model.Vector


class DrawView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    companion object {
        const val STROKE_WIDTH = 10f
    }

    private var drawType = DrawType.PATH

    private var mCurrentBox: Box? = null
    private val mBoxes: ArrayList<Box> = ArrayList()
    private val mBoxPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = STROKE_WIDTH
        color = Color.RED
        style = Paint.Style.STROKE
    }

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = STROKE_WIDTH
        color = Color.RED
        style = Paint.Style.STROKE
    }
    private val mPath = Path()

    private var mCurrentVector: Vector? = null
    private val mVectors: ArrayList<Vector> = ArrayList()

    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(mPath, mPaint)

        for (box in mBoxes) {
            val left: Float = box.origin.x.coerceAtMost(box.current.x)
            val right: Float = box.origin.x.coerceAtLeast(box.current.x)
            val top: Float = box.origin.y.coerceAtMost(box.current.y)
            val bottom: Float = box.origin.y.coerceAtLeast(box.current.y)
            canvas.drawRect(left, top, right, bottom, mBoxPaint)
        }

        for (vector in mVectors) {
            canvas.drawLine(vector.start.x, vector.start.y, vector.end.x, vector.end.y, mPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val eventX = event.x
        val eventY = event.y
        when(drawType) {
            DrawType.PATH -> {
                when (event.action) {
                    ACTION_DOWN -> {
                        mPath.moveTo(eventX, eventY)
                        return true
                    }
                    ACTION_MOVE -> mPath.lineTo(eventX, eventY)
                    else -> return false
                }
                invalidate()
            }
            DrawType.RECTANGLE -> {
                val current = PointF(event.x, event.y)
                when (event.action) {
                    ACTION_DOWN -> {
                        mCurrentBox = Box(current, current)
                        mBoxes.add(mCurrentBox!!)
                    }
                    ACTION_MOVE -> if (mCurrentBox != null) {
                        mCurrentBox!!.current = current
                        invalidate()
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> mCurrentBox = null
                }
            }
            DrawType.VECTOR -> {
                val current = PointF(event.x, event.y)
                when (event.action) {
                    ACTION_DOWN -> {
                        mCurrentVector = Vector(current, current)
                        mVectors.add(mCurrentVector!!)
                    }
                    ACTION_MOVE -> if (mCurrentVector != null) {
                        mCurrentVector!!.end = current
                        invalidate()
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> mCurrentBox = null
                }
            }
        }
        return true;
    }

    fun reset() {
        mBoxes.clear();
        mPath.reset()
        mVectors.clear()
        invalidate()
    }

    fun setColor(color: Int) {
        mPaint.color = color
        mBoxPaint.color = color
        mBoxPaint.color = color
    }

    fun setDrawType(type: DrawType) {
        drawType = type
    }
}
