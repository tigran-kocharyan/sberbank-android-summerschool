package ru.totowka.okhttp

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable


class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "Callback"
    }

    private lateinit var getRequest: Button
    private lateinit var postRequest: Button
    private lateinit var textviewBody: TextView
    private lateinit var okhttp: RadioButton
    private lateinit var httpurl: RadioButton
    private var compositeDisposables = CompositeDisposable()

    private lateinit var httpOkClient: HttpOkClient
    private lateinit var httpUrlConnectionClient: HttpUrlConnectionClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialize()
        setListeners()
        textviewBody.movementMethod = ScrollingMovementMethod()
    }


    private fun get(client: HttpClient) {
        compositeDisposables.add(client.get("http://jsonplaceholder.typicode.com/posts?userId=1")
            .subscribe({ value -> textviewBody.text = value }, { error -> textviewBody.text = error.message })
        )
    }

    private fun post(client: HttpClient) {
        compositeDisposables.add(client.post("http://jsonplaceholder.typicode.com/posts")
            .subscribe({ value -> textviewBody.text = value }, { error -> textviewBody.text = error.message })
        )
    }

    private fun initialize() {
        getRequest = findViewById(R.id.get)
        postRequest = findViewById(R.id.post)
        textviewBody = findViewById(R.id.body)
        okhttp = findViewById(R.id.ok_http)
        httpurl = findViewById(R.id.http_url_connection)
        httpOkClient = HttpOkClient()
        httpUrlConnectionClient = HttpUrlConnectionClient()
    }

    private fun setListeners() {
        getRequest.setOnClickListener {
            when (okhttp.isChecked) {
                true -> {
                    get(httpOkClient)
                }
                else -> {
                    get(httpUrlConnectionClient)
                }
            }

        }
        postRequest.setOnClickListener {
            when (okhttp.isChecked) {
                true -> {
                    post(httpOkClient)
                }
                else -> {
                    post(httpUrlConnectionClient)
                }
            }

        }
    }

    override fun onDestroy() {
        compositeDisposables.clear()
        super.onDestroy()
    }
}