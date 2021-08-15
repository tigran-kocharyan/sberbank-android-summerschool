package ru.totowka.mvvm.data.provider

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.okhttp.HttpUrl
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import ru.totowka.mvvm.data.model.DogFactResponseModel
import ru.totowka.mvvm.data.model.DogImageModel
import ru.totowka.mvvm.data.model.DogImageResponseModel
import java.io.IOException
import java.lang.reflect.Type
import kotlin.random.Random

/**
 * Релазизация [DogInfoProvider]
 */
class DogInfoProviderImpl(private val client: OkHttpClient = OkHttpClient(), private val gson: Gson = Gson()) :
    DogInfoProvider {

    companion object {
        private const val MINIMUM_FACT_INDEX = 0
        private const val MAXIMUM_FACT_INDEX = 100
        private const val PARAM_INDEX = "index"
    }


    /**
     * Функция для получения списка изображений.
     *
     * @return cписок с рандомным количеством изображений
     *
     * @throws IOException if the request could not be executed due to
     *     cancellation, a connectivity problem or timeout. Because networks can
     *     fail during an exchange, it is possible that the remote server
     *     accepted the request before the failure.
     *
     * @throws IllegalStateException when the call has already been executed.
     */
    @Throws(IOException::class, IllegalStateException::class)
    override fun getData(
        amount: Int,
        url: String
    ): List<DogImageModel> {
        val list = ArrayList<DogImageModel>()
        val requestImage: Request = Request.Builder()
            .url(url)
            .build()

        for (i in 1..amount) {
            val response = client.newCall(requestImage).execute()
            if (response.isSuccessful) {
                val body = response.body()?.string()
                list.add(DogImageModel(gson.fromJson(body, DogImageResponseModel::class.java).message))
            } else {
                throw IllegalArgumentException("Error: ${response.code()}")
            }
        }

        return list
    }

    /**
     * Функция для получения рандомного факта.
     *
     * @return рандомное строку факта
     *
     * @throws IOException if the request could not be executed due to
     *     cancellation, a connectivity problem or timeout. Because networks can
     *     fail during an exchange, it is possible that the remote server
     *     accepted the request before the failure.
     *
     * @throws IllegalStateException when the call has already been executed.
     */
    @Throws(IOException::class, IllegalStateException::class)
    override fun getFact(url: String): String {
        val requestParams: HttpUrl.Builder = HttpUrl.parse(url).newBuilder()
            .addQueryParameter(PARAM_INDEX, Random.nextInt(MINIMUM_FACT_INDEX, MAXIMUM_FACT_INDEX).toString())

        val request = Request.Builder().url(requestParams.build()).build()
        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            val body = response.body()?.string()
            val collectionType: Type = object : TypeToken<List<DogFactResponseModel>>() {}.type
            return (gson.fromJson(body, collectionType) as List<DogFactResponseModel>)[0].fact
        } else {
            throw IllegalArgumentException("Error: ${response.code()}")
        }
    }
}