package ru.totowka.okhttp

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.*

class HttpOkClient : HttpClient{
    companion object {
        private const val TAG = "OkHttpClient"
    }

    private val client = OkHttpClient()
    private val body: RequestBody = FormBody.Builder()
        .add("title", "foo")
        .add("body", "bar")
        .add("userId", "1")
        .build()

    override fun get(url: String): Single<String?> {
        val request = Request.Builder()
            .url(url)
            .build()

        return Single.fromCallable {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                return@fromCallable response.body()?.string()
            } else {
                throw IllegalArgumentException("Error: ${response.code()}")
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun post(url: String): Single<String?> {
        val request: Request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        return Single.fromCallable {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                return@fromCallable response.body()?.string()
            } else {
                throw IllegalArgumentException("Error: ${response.code()}")
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}