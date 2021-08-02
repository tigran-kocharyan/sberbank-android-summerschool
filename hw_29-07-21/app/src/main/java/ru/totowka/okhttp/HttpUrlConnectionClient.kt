package ru.totowka.okhttp

import com.google.gson.JsonObject
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class HttpUrlConnectionClient : HttpClient{
    companion object {
        private const val TAG = "HttpUrlConnectionRepository"
    }

    private var body = JsonObject().apply {
        addProperty("title", "foo")
        addProperty("body", "bar")
        addProperty("userId", "1")
    }

    override fun get(url: String): Single<String?> {
        return Single.fromCallable {
            val connection = setupGetConnection(url)
            val code = connection.responseCode
            if (code != 200) {
                throw IOException("Invalid response from server: $code")
            }
            return@fromCallable readResponse(connection)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun setupGetConnection(url: String): HttpURLConnection {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.apply {
            requestMethod = "GET"
            connectTimeout = 5000
            readTimeout = 5000
        }
        return connection
    }


    override fun post(url: String): Single<String?> {
        return Single.fromCallable {
            val connection = setupPostConnection(url)
            writeRequest(connection)
            val code: Int = connection.responseCode
            if (code != 201) {
                throw IOException("Invalid response from server: $code")
            }
            return@fromCallable readResponse(connection)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun setupPostConnection(url: String): HttpURLConnection {
        val connection = (URL(url).openConnection() as HttpURLConnection)
        connection.apply {
            setRequestProperty("Content-Type", "application/json")
            requestMethod = "POST"
            doOutput = true
            doInput = true
            setChunkedStreamingMode(0)
        }
        return connection
    }

    private fun writeRequest(connection: HttpURLConnection) {
        BufferedWriter(OutputStreamWriter(BufferedOutputStream(connection.outputStream), "UTF-8"))
            .use { writer ->
                writer.write(body.toString())
                writer.flush()
            }
    }

    private fun readResponse(connection: HttpURLConnection) : String {
        BufferedReader(InputStreamReader(connection.inputStream)).use { bufferedReader ->
            var inputLine: String?
            val content = StringBuffer()
            while (bufferedReader.readLine().also { inputLine = it } != null) {
                content.append(inputLine).append("\n")
            }
            connection.disconnect()
            return content.toString()
        }
    }
}