package ru.totowka.okhttp.domain

import android.os.Handler
import android.os.Looper
import okhttp3.*
import java.io.IOException


class OkHttpRepository {
    companion object {
        private const val TAG = "OkHttpRepository"
    }

    private val client = OkHttpClient()
    private val body: RequestBody = FormBody.Builder()
        .add("title", "foo")
        .add("body", "bar")
        .add("userId", "1")
        .build()

    fun get(url: String, callback: OkHttpResponseCallback) {
        val request = Request.Builder()
            .url(url)
            .build()
        client.newCall(request).enqueue(object : Callback {
            var mainHandler: Handler = Handler(Looper.getMainLooper())

            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post {
                    callback.onFailure(null, e)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                // Студия ругалась, если доставать body().string() из UI-трэда.
                // Пришлось вынести из коллбэка и доставать здесь
                val result = response.body()?.string()
                mainHandler.post(Runnable {
                    if (!response.isSuccessful) {
                        callback.onFailure(result, null)
                        return@Runnable
                    }
                    callback.onSuccess(result)
                })
            }
        })
    }

    fun post(url: String, callback: OkHttpResponseCallback) {
        val request: Request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            var mainHandler: Handler = Handler(Looper.getMainLooper())

            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post {
                    callback.onFailure(null, e)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                // Студия ругалась, если доставать body().string() из UI-трэда.
                // Пришлось вынести из коллбэка и доставать здесь
                val result = response.body()?.string()
                mainHandler.post(Runnable {
                    if (!response.isSuccessful) {
                        callback.onFailure(result, null)
                        return@Runnable
                    }
                    callback.onSuccess(result)
                })
            }
        })
    }
}

interface OkHttpResponseCallback {
    fun onFailure(response: String?, throwable: Throwable?)
    fun onSuccess(response: String?)
}

