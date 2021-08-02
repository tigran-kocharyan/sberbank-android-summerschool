package ru.totowka.okhttp

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable


class MainActivity : AppCompatActivity(), OkHttpResponseCallback {
    companion object {
        private const val TAG = "Callback"
    }

    private lateinit var getRequest: Button
    private lateinit var postRequest: Button
    private lateinit var textviewBody: TextView
    private lateinit var client: OkHttpRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialize()
        getRequest.setOnClickListener {
            client.get("http://jsonplaceholder.typicode.com/posts?userId=1")
                .subscribe(object : SingleObserver<String?> {
                    override fun onSuccess(value: String) {
                        textviewBody.text = value
                    }
                    override fun onError(e: Throwable) {
                        textviewBody.text = e.message
                    }
                    override fun onSubscribe(d: Disposable) {
                        println("Single: onSubscribe")
                    }
                })
        }
        postRequest.setOnClickListener {
            client.post("http://jsonplaceholder.typicode.com/posts")
                .subscribe(object : SingleObserver<String?> {
                    override fun onSuccess(value: String) {
                        textviewBody.text = value
                    }
                    override fun onError(e: Throwable) {
                        textviewBody.text = e.message
                    }
                    override fun onSubscribe(d: Disposable) {
                        println("Single: onSubscribe")
                    }
                })
        }
        textviewBody.movementMethod = ScrollingMovementMethod()
    }

    private fun initialize() {
        getRequest = findViewById(R.id.get)
        postRequest = findViewById(R.id.post)
        textviewBody = findViewById(R.id.body)
        client = OkHttpRepository()
    }

    override fun onFailure(response: String?, throwable: Throwable?) {
        Log.d(TAG, "onFailure() called with: response = $response, throwable = $throwable")
        Toast.makeText(this, "Fail to call API", Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess(response: String?) {
        Log.d(TAG, "onSuccess() called with: response = $response")
        textviewBody.text = response
    }
}