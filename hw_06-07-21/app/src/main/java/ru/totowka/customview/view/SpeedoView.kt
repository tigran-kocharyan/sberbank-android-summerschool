package ru.totowka.customview.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.ColorInt
import ru.totowka.customview.R
import ru.totowka.customview.util.SpeedoException
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

class SpeedoView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    companion object {
        private const val TAG = "SpeedoView"
        private const val ARC_WIDTH = 40f
        private const val ARROW_WIDTH = 10f
        private const val TEXT_SIZE = 64f
        private const val START_ARC_ANGLE = 0f
        private const val END_ARC_ANGLE = 180f
        private const val ARROW_ANGLE_OFFSET = 90f
    }

    @ColorInt
    private var lowSpeedColor: Int
    @ColorInt
    private var mediumSpeedColor: Int
    @ColorInt
    private var highSpeedColor: Int
    @ColorInt
    private var arrowColor: Int
    private var currentSpeed: Int
    private var maxSpeed: Int
    private var currentSpeedText = ""
    private var maxSpeedText = ""
    private var linearGradient : LinearGradient


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
    private val maxSpeedTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = TEXT_SIZE
    }
    private val currentSpeedTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = TEXT_SIZE
    }
    private val arcRectangle = RectF()
    private val currentSpeedRectangle = Rect()
    private val maxSpeedRectangle = Rect()

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
            linearGradient = LinearGradient(
                arcRectangle.left + arcRectangle.width(),
                arcRectangle.top + arcRectangle.height() / 2,
                arcRectangle.left,
                arcRectangle.top + arcRectangle.height() / 2,
                intArrayOf(lowSpeedColor, mediumSpeedColor, highSpeedColor),
                null,
                Shader.TileMode.CLAMP
            )
            if (currentSpeed > maxSpeed) {
                throw SpeedoException("Current Speed should be <= Max Speed")
            }
        } finally {
            typedArray.recycle()
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        calcBounds()
        val measuredWidth =
            (maxSpeedRectangle.width() + (ARC_WIDTH * 2) + currentSpeedRectangle.width() + paddingLeft + paddingRight).toInt()
        val measuredHeight =
            ((ARC_WIDTH * 2) + (currentSpeedRectangle.height() * 2) + paddingTop + paddingBottom).toInt()

        val requestedWidth = max(measuredWidth, suggestedMinimumWidth)
        val requestedHeight = max(measuredHeight, suggestedMinimumHeight)

        setMeasuredDimension(
            resolveSizeAndState(requestedWidth, widthMeasureSpec, 0),
            resolveSizeAndState(requestedHeight, heightMeasureSpec, 0)
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        calcBounds()
        arcRectangle.set(
            paddingLeft + maxSpeedRectangle.width().toFloat() + ARC_WIDTH,
            ARC_WIDTH / 2 + paddingTop,
            w - paddingLeft - paddingRight - maxSpeedRectangle.width() + ARC_WIDTH,
            (h - paddingTop - paddingBottom).toFloat()
        )

        // Градиент напрямую зависит от размеров.
        // Не факт, что вызовется onDraw + затратная операция для onDraw,
        // поэтому лучше посчитать параметры shader здесь.
        foregroundArcPaint.shader = LinearGradient(
            arcRectangle.left + arcRectangle.width(),
            arcRectangle.top + arcRectangle.height() / 2,
            arcRectangle.left,
            arcRectangle.top + arcRectangle.height() / 2,
            intArrayOf(lowSpeedColor, mediumSpeedColor, highSpeedColor),
            null,
            Shader.TileMode.CLAMP
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        calcBounds()
        arrowPaint.color = arrowColor

        canvas?.apply {
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
            val radius = min(arcRectangle.width() / 3, arcRectangle.height() / 3)
            val stopX = centerX + (sin(angle) * radius)
            val stopY = centerY - (cos(angle) * radius)
            drawLine(centerX, centerY, stopX.toFloat(), stopY.toFloat(), arrowPaint)
        }
    }

    private fun drawTitles(canvas: Canvas?) {
        canvas?.apply {
            drawText(
                currentSpeedText,
                arcRectangle.width() / 2 + arcRectangle.left - currentSpeedRectangle.width() / 2,
                arcRectangle.height() / 2 + arcRectangle.top + currentSpeedRectangle.height() + ARROW_WIDTH / 2,
                currentSpeedTextPaint
            )
            drawText(
                maxSpeedText,
                paddingLeft.toFloat(),
                arcRectangle.height() / 2 + arcRectangle.top,
                maxSpeedTextPaint
            )
        }
    }

    private fun calcBounds() {
        currentSpeedText = "$currentSpeed km/h"
        maxSpeedText = "$maxSpeed"
        currentSpeedTextPaint.getTextBounds(currentSpeedText, 0, currentSpeedText.length, currentSpeedRectangle)
        maxSpeedTextPaint.getTextBounds(maxSpeedText, 0, maxSpeedText.length, maxSpeedRectangle)
    }

    fun setLowSpeedColor(@ColorInt color: Int) {
        this.lowSpeedColor = color

        // У LinearGradient отсутствует сеттер массива цветов, поэтому приходится пересоздавать объект
        linearGradient = LinearGradient(
            arcRectangle.left + arcRectangle.width(),
            arcRectangle.top + arcRectangle.height() / 2,
            arcRectangle.left,
            arcRectangle.top + arcRectangle.height() / 2,
            intArrayOf(lowSpeedColor, mediumSpeedColor, highSpeedColor),
            null,
            Shader.TileMode.CLAMP
        )
        invalidate()
    }

    fun setMediumSpeedColor(@ColorInt color: Int) {
        this.mediumSpeedColor = color

        // У LinearGradient отсутствует сеттер массива цветов, поэтому приходится пересоздавать объект
        linearGradient = LinearGradient(
            arcRectangle.left + arcRectangle.width(),
            arcRectangle.top + arcRectangle.height() / 2,
            arcRectangle.left,
            arcRectangle.top + arcRectangle.height() / 2,
            intArrayOf(lowSpeedColor, mediumSpeedColor, highSpeedColor),
            null,
            Shader.TileMode.CLAMP
        )
        invalidate()
    }

    fun setHighSpeedColor(@ColorInt color: Int) {
        this.highSpeedColor = color

        // У LinearGradient отсутствует сеттер массива цветов, поэтому приходится пересоздавать объект
        linearGradient = LinearGradient(
            arcRectangle.left + arcRectangle.width(),
            arcRectangle.top + arcRectangle.height() / 2,
            arcRectangle.left,
            arcRectangle.top + arcRectangle.height() / 2,
            intArrayOf(lowSpeedColor, mediumSpeedColor, highSpeedColor),
            null,
            Shader.TileMode.CLAMP
        )
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

    fun setCurrentSpeedTextColor(value: Int) {
        currentSpeedTextPaint.color = value
        invalidate()
    }

    fun setCurrentSpeedTextSize(value: Float) {
        currentSpeedTextPaint.textSize = value
        invalidate()
    }

    fun getMaxSpeed() = maxSpeed
}