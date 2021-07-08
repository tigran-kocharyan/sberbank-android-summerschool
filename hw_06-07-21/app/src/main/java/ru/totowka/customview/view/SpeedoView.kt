package ru.totowka.customview.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.ColorInt
import ru.totowka.customview.R
import ru.totowka.customview.util.SpeedoException
import kotlin.math.cos
import kotlin.math.sin


class SpeedoView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    companion object {
        private const val TAG = "SpeedoView"

        @ColorInt
        private const val LOW_SPEED_COLOR_DEFAULT = 0

        @ColorInt
        private const val MEDIUM_SPEED_COLOR_DEFAULT = 0

        @ColorInt
        private const val HIGH_SPEED_COLOR_DEFAULT = 0

        @ColorInt
        private const val ARROW_COLOR_DEFAULT = 0
        private const val SPEED_DEFAULT = 0
        private const val MAX_SPEED_DEFAULT = 0
        private const val ARC_WIDTH = 40f
        private const val ARROW_WIDTH = 10f
        private const val TEXT_SIZE = 64f
        private const val START_ARC_ANGLE = 0f
        private const val END_ARC_ANGLE = 180f
        private const val ARC_HEIGHT_TRANSLATE = 250f
        private const val ARC_WIDTH_TRANSLATE = 250f
        private const val ARC_LEFT = 0f
        private const val ARC_RIGHT = 400f
        private const val ARC_TOP = 0f
        private const val ARC_BOTTOM = 400f
        private const val GRADIENT_START_X = 250f
        private const val GRADIENT_START_Y = 250f
        private const val GRADIENT_END_X = 450f
        private const val GRADIENT_END_Y = 450f
        private const val ARROW_ANGLE_OFFSET = 90f
    }

    @ColorInt
    private var lowSpeedColor: Int = LOW_SPEED_COLOR_DEFAULT

    @ColorInt
    private var mediumSpeedColor: Int = MEDIUM_SPEED_COLOR_DEFAULT

    @ColorInt
    private var highSpeedColor: Int = HIGH_SPEED_COLOR_DEFAULT

    @ColorInt
    private var arrowColor: Int = ARROW_COLOR_DEFAULT
    private var currentSpeed: Int = SPEED_DEFAULT
    private var maxSpeed: Int = MAX_SPEED_DEFAULT

    private val backGroundArcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GRAY
        style = Paint.Style.STROKE
        strokeWidth = ARC_WIDTH
    }
    private val foregroundArcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = ARC_WIDTH

    }
    private val arrowPaint = Paint().apply {
        strokeWidth = ARROW_WIDTH
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = TEXT_SIZE
    }
    private val arcRectangle = RectF(ARC_LEFT, ARC_TOP, ARC_RIGHT, ARC_BOTTOM)
    private val speedRectangle = Rect()

    init {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SpeedoView,
            R.attr.speedoDefaultAttr,
            0
        )
        try {
            arrowColor = typedArray.getColor(R.styleable.SpeedoView_arrowColor, 0)
            lowSpeedColor = typedArray.getColor(R.styleable.SpeedoView_lowSpeedColor, 0)
            mediumSpeedColor = typedArray.getColor(R.styleable.SpeedoView_mediumSpeedColor, 0)
            highSpeedColor = typedArray.getColor(R.styleable.SpeedoView_highSpeedColor, 0)
            currentSpeed = typedArray.getInt(R.styleable.SpeedoView_currentSpeed, 0)
            maxSpeed = typedArray.getInt(R.styleable.SpeedoView_maxSpeed, 180)

            if (currentSpeed > maxSpeed) {
                throw SpeedoException("Current Speed should be <= Max Speed")
            }
        } finally {
            typedArray.recycle()
        }

    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        foregroundArcPaint.shader = LinearGradient(
            GRADIENT_START_X,
            GRADIENT_START_Y,
            GRADIENT_END_X,
            GRADIENT_END_Y,
            intArrayOf(lowSpeedColor, mediumSpeedColor, highSpeedColor),
            null,
            Shader.TileMode.MIRROR
        )
        arrowPaint.color = arrowColor

        canvas?.apply {
            translate(ARC_WIDTH_TRANSLATE, ARC_HEIGHT_TRANSLATE)
            drawArc(
                arcRectangle,
                START_ARC_ANGLE,
                -END_ARC_ANGLE,
                false,
                backGroundArcPaint
            )
            drawArc(
                arcRectangle,
                START_ARC_ANGLE,
                -(currentSpeed * END_ARC_ANGLE / maxSpeed),
                false,
                foregroundArcPaint
            )
            drawTitles(canvas)
            drawArrow(canvas)
        }
    }

    private fun drawArrow(canvas: Canvas?) {
        canvas?.apply {
            val centerX = (arcRectangle.width() / 2) + arcRectangle.left
            val centerY = (arcRectangle.height() / 2) + arcRectangle.top
            val angle =
                Math.toRadians((-(currentSpeed * END_ARC_ANGLE / maxSpeed) + ARROW_ANGLE_OFFSET).toDouble())
            val radius = arcRectangle.width() / 2 - 40
            val stopX = centerX + (sin(angle) * radius)
            val stopY = centerY - (cos(angle) * radius)
            drawLine(centerX, centerY, stopX.toFloat(), stopY.toFloat(), arrowPaint)
        }
    }

    private fun drawTitles(canvas: Canvas?) {
        val current = "$currentSpeed km/h"
        val max = maxSpeed.toString()
        canvas?.apply {
            textPaint.getTextBounds(current, 0, current.length, speedRectangle)
            drawText(
                current,
                arcRectangle.width() / 2 + arcRectangle.left - speedRectangle.width() / 2,
                arcRectangle.height() / 2 + speedRectangle.height() + 10,
                textPaint
            )
            drawText(
                max,
                -200F,
                arcRectangle.height() / 2 + arcRectangle.top,
                textPaint
            )
        }
    }

    fun setLowSpeedColor(@ColorInt color: Int) {
        this.lowSpeedColor = color
        invalidate()
    }

    fun setMediumSpeedColor(@ColorInt color: Int) {
        this.mediumSpeedColor = color
        invalidate()
    }

    fun setHighSpeedColor(@ColorInt color: Int) {
        this.highSpeedColor = color
        invalidate()
    }

    fun setArrowColor(@ColorInt color: Int) {
        this.arrowColor = color
        invalidate()
    }

    fun setCurrentSpeed(value: Int) {
        if (value > this.maxSpeed) {
            Log.d(TAG, "setCurrentSpeed() called with: value = $value")
            throw SpeedoException("Current should be <= maxSpeed")
        }
        currentSpeed = value
        invalidate()
    }

    fun setMaxSpeed(value: Int) {
        if (value < this.currentSpeed) {
            Log.d(TAG, "setMaxSpeed() called with: value = $value")
            throw SpeedoException("Max should be >= currentSpeed")
        }
        maxSpeed = value
        invalidate()
    }
}