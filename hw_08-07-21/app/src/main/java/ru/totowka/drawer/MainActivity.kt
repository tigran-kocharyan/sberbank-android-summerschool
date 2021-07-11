package ru.totowka.drawer

import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.azeesoft.lib.colorpicker.ColorPickerDialog
import ru.totowka.drawer.model.DrawType


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var buttonRectangle: Button
    private lateinit var buttonVector: Button
    private lateinit var buttonPath: Button
    private lateinit var buttonReset: Button
    private lateinit var buttonPickColor: Button
    private lateinit var drawer: DrawView
    private lateinit var colorPicker: ColorPickerDialog
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var scaleFactor = 1.0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialize()
        setClickListeners()
        colorPicker.setOnColorPickedListener { color, _ -> drawer.setColor(color) }
        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())
        drawer.setScaleGestureDetector(scaleGestureDetector)
    }

    private fun initialize() {
        buttonRectangle = findViewById(R.id.rectangle)
        buttonVector = findViewById(R.id.vector)
        buttonPath = findViewById(R.id.path)
        buttonReset = findViewById(R.id.reset)
        buttonPickColor = findViewById(R.id.color_picker)
        drawer = findViewById(R.id.drawer)
    }

    private fun setClickListeners() {
        buttonPath.isEnabled = false
        buttonRectangle.setOnClickListener(this)
        buttonVector.setOnClickListener(this)
        buttonPath.setOnClickListener(this)
        buttonPath.setOnClickListener(this)
        buttonReset.setOnClickListener(this)
        buttonPickColor.setOnClickListener(this)
        colorPicker = ColorPickerDialog.createColorPickerDialog(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.color_picker -> {
                    colorPicker.show()
                }
                R.id.rectangle -> {
                    enableAllButton()
                    drawer.setDrawType(DrawType.RECTANGLE)
                    buttonRectangle.isEnabled = false
                }
                R.id.vector -> {
                    enableAllButton()
                    drawer.setDrawType(DrawType.VECTOR)
                    buttonVector.isEnabled = false
                }
                R.id.path -> {
                    enableAllButton()
                    drawer.setDrawType(DrawType.PATH)
                    buttonPath.isEnabled = false
                }
                R.id.reset -> {
                    drawer.reset()
                }
            }
        }
    }

    private fun enableAllButton() {
        buttonRectangle.isEnabled = true
        buttonVector.isEnabled = true
        buttonPath.isEnabled = true
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor
            scaleFactor = 0.1f.coerceAtLeast(scaleFactor.coerceAtMost(10.0f))
            drawer.scaleX = scaleFactor
            drawer.scaleY = scaleFactor
            return true
        }
    }
}