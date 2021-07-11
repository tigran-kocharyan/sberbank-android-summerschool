package ru.totowka.drawer

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.SparseArray
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import androidx.core.util.isEmpty
import androidx.core.util.isNotEmpty
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

    private val mCurves: ArrayList<Curve> = ArrayList()
    private val mMultiTouchCurves: SparseArray<Curve> = SparseArray()

    override fun onDraw(canvas: Canvas) {
        if(mMultiTouchCurves.isNotEmpty()) {
            for (i in 0 until mMultiTouchCurves.size()) {
                mMultiTouchCurves.valueAt(i).draw(canvas, mPaint)
            }
        }
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
        return when (drawType) {
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
    }

    private fun processVector(event: MotionEvent): Boolean {
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
            else -> {
                mCurrentVector = null
                return false
            }
        }
        return true
    }

    private fun processRectangle(event: MotionEvent): Boolean {
        val current = PointF(event.x, event.y)
        when (event.action) {
            ACTION_DOWN -> {
                mCurrentBox = Box(current, current, mPaint.color)
                mBoxes.add(mCurrentBox!!)
            }
            ACTION_MOVE -> if (mCurrentBox != null) {
                mCurrentBox!!.current = current
                invalidate()
            }
            else -> {
                mCurrentBox = null
                return false
            }
        }
        return true
    }

    private fun processCurve(event: MotionEvent): Boolean {
        val pointerIndex = event.actionIndex
        val pointerId = event.getPointerId(pointerIndex)
        when (event.actionMasked) {
            ACTION_DOWN, ACTION_POINTER_DOWN -> {
                var currentCurve = Curve(Path(), mPaint.color)
                currentCurve.curve.moveTo(event.getX(pointerIndex), event.getY(pointerIndex))
                mMultiTouchCurves.put(pointerId, currentCurve)
            }
            ACTION_MOVE -> {
                for (i in 0 until event.pointerCount) {
                    var id = event.getPointerId(i)
                    var currentCurve = mMultiTouchCurves.get(id)
                    currentCurve.curve.lineTo(event.getX(i), event.getY(i))
                }

            }
            ACTION_POINTER_UP -> {
                mCurves.add(mMultiTouchCurves.get(pointerId))
                mMultiTouchCurves.delete(pointerId)
            }
            else -> {
                for (i in 0 until mMultiTouchCurves.size()) {
                    mCurves.add(mMultiTouchCurves.valueAt(i))
                }
                mMultiTouchCurves.clear()
            }
        }
        invalidate()
        return true
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
