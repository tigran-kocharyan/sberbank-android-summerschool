package ru.totowka.customview

import android.animation.ArgbEvaluator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import ru.totowka.customview.view.SpeedoView

class MainActivity : AppCompatActivity() {
    lateinit var speedoView: SpeedoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        speedoView = findViewById(R.id.speedo)

        // Чтобы условия одной домашней не ломали условия другой, то я разделил две логики:
        // withSeekVar() - для Андрея Кудрявцева
        // withValueAnimator(speedoView) - для Anton Amialiuk
        withValueAnimator(speedoView)
//        withSeekVar()
    }

    private fun withSeekVar() {
        findViewById<SeekBar>(R.id.seekbar).setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                speedoView.setCurrentSpeed(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun withValueAnimator(speedoView: SpeedoView) {
        val colorEvaluator = ArgbEvaluator()
        val textSizeHolder: PropertyValuesHolder =
            PropertyValuesHolder.ofFloat("textSize", 64f, 128f)
        val currentSpeedHolder: PropertyValuesHolder =
            PropertyValuesHolder.ofInt("currentSpeed", 0, speedoView.getMaxSpeed())

        ValueAnimator.ofPropertyValuesHolder(textSizeHolder, currentSpeedHolder)
            .apply {
                duration = 5000
                repeatMode = ValueAnimator.REVERSE
                repeatCount = ValueAnimator.INFINITE
                interpolator = AccelerateDecelerateInterpolator()
                addUpdateListener { animator: ValueAnimator ->
                    val currentSpeed = animator.getAnimatedValue("currentSpeed") as Int
                    val textSize = animator.getAnimatedValue("textSize") as Float

                    speedoView.setCurrentSpeedTextColor(
                        colorEvaluator.evaluate(
                            (currentSpeed.toFloat() / speedoView.getMaxSpeed().toFloat()),
                            Color.GREEN,
                            Color.RED
                        ).toString().toInt()
                    )
                    speedoView.setCurrentSpeed(currentSpeed)
                    speedoView.setCurrentSpeedTextSize(textSize)
                }
            }
            .start()
    }
}