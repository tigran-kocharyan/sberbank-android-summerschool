package ru.totowka.drawer

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.View
import ru.totowka.drawer.model.Box
import ru.totowka.drawer.model.Curve
import ru.totowka.drawer.model.DrawType
import ru.totowka.drawer.model.Vector


class DrawView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    companion object {
        const val STROKE_WIDTH = 10f
        const val COLOR = Color.RED
    }
    private var drawType = DrawType.PATH
    private var mColor: Int = COLOR

    private var mCurrentBox: Box? = null
    private val mBoxes: ArrayList<Box> = ArrayList()
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = STROKE_WIDTH
        color = COLOR
        style = Paint.Style.STROKE
    }
    private var mCurrentVector: Vector? = null
    private val mVectors: ArrayList<Vector> = ArrayList()

    private var mCurrentCurve: Curve? = null
    private val mCurves: ArrayList<Curve> = ArrayList()

    override fun onDraw(canvas: Canvas) {
        for (curve in mCurves) {
            curve.draw(canvas, mPaint)
        }
        for (box in mBoxes) {
            box.draw(canvas, mPaint)
        }
        for (vector in mVectors) {
            vector.draw(canvas, mPaint)
        }
        mPaint.color = mColor
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(drawType) {
            DrawType.PATH -> {
                processCurve(event)
            }
            DrawType.RECTANGLE -> {
                processRectangle(event)
            }
            DrawType.VECTOR -> {
                processVector(event)
            }
        }
        return true
    }

    private fun processVector(event: MotionEvent) {
        val current = PointF(event.x, event.y)
        when (event.action) {
            ACTION_DOWN -> {
                mCurrentVector = Vector(current, current, mPaint.color)
                mVectors.add(mCurrentVector!!)
            }
            ACTION_MOVE -> if (mCurrentVector != null) {
                mCurrentVector!!.end = current
                invalidate()
            }
            else -> mCurrentVector = null
        }
    }

    private fun processRectangle(event: MotionEvent) {
        val current = PointF(event.x, event.y)
        when (event.action) {
            ACTION_DOWN -> {
                mCurrentBox = Box(current, current,  mPaint.color)
                mBoxes.add(mCurrentBox!!)
            }
            ACTION_MOVE -> if (mCurrentBox != null) {
                mCurrentBox!!.current = current
                invalidate()
            }
            else -> mCurrentBox = null
        }
    }

    private fun processCurve(event: MotionEvent) {
        when (event.action) {
            ACTION_DOWN -> {
                mCurrentCurve = Curve(Path(), mPaint.color)
                mCurves.add(mCurrentCurve!!)
                mCurrentCurve!!.curve.moveTo(event.x, event.y)
            }
            ACTION_MOVE -> if (mCurrentCurve != null) {
                mCurrentCurve!!.curve.lineTo(event.x, event.y)
                invalidate()
            }
            else -> mCurrentCurve = null
        }
    }

    fun reset() {
        mBoxes.clear()
        mCurves.clear()
        mVectors.clear()
        invalidate()
    }

    fun setColor(color: Int) {
        mColor = color
        mPaint.color = color
    }

    fun setDrawType(type: DrawType) {
        drawType = type
    }
}
