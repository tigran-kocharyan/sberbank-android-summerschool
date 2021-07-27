package ru.totowka.timer

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    companion object {
        const val REGULAR_DELAY: Long = 1000
        const val START_DELAY: Long = 0
        const val SECONDS_TO_COUNT: Int = 10
    }

    private lateinit var textview: TextView
    private lateinit var button: Button
    private var time = SECONDS_TO_COUNT
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textview = findViewById(R.id.textView)
        button = findViewById(R.id.button)

        button.setOnClickListener {
            handler.removeCallbacks(counter)
            time = SECONDS_TO_COUNT
            handler.postDelayed(counter, START_DELAY)
        }
    }

    // Описание Runnable-объекта, который будет отвечать за отсчёт
    private val counter: Runnable = object : Runnable {
        override fun run() {
            if(time >= 0) {
                textview.text = "$time"
                time--
                handler.postDelayed(this, REGULAR_DELAY)
            }
        }
    }

    // Паузим таймер если пользовател не видит его
    override fun onPause() {
        handler.removeCallbacks(counter)
        super.onPause()
    }

    // Возобновляем таймер при возвращении
    override fun onResume() {
        super.onResume()
        handler.postDelayed(counter, REGULAR_DELAY)
    }
}