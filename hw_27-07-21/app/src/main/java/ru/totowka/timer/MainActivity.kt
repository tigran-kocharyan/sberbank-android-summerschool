package ru.totowka.timer

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    companion object {
        const val SECONDS_TO_COUNT: Long = 10
    }

    private lateinit var textview: TextView
    private lateinit var button: Button

    private var publisher = Observable.interval(1, TimeUnit.SECONDS)
        .take(SECONDS_TO_COUNT)
        .map { value -> SECONDS_TO_COUNT - value }

    private var subscriber: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textview = findViewById(R.id.textView)
        button = findViewById(R.id.button)

        button.setOnClickListener {
            subscriber?.dispose()

            subscriber = publisher
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { value ->
                        textview.text = "$value"
                    },
                    { error -> println("Error: $error") }
                ) { textview.text = "Finished!"}


        }
    }
}