package ru.totowka.mvvm.data.provider

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.okhttp.HttpUrl
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import ru.totowka.mvvm.data.model.DogFactResponseModel
import ru.totowka.mvvm.data.model.DogImageModel
import ru.totowka.mvvm.data.model.DogImageResponseModel
import java.lang.reflect.Type
import kotlin.random.Random

/**
 * Класс-провайдер для отправки асинхронных запросов в сеть для получения факта о собаках и изображения собаки
 */
class DogInfoProvider {

    companion object {
        private const val FACT_URL = "https://dog-facts-api.herokuapp.com/api/v1/resources/dogs"
        private const val IMAGE_URL = "https://dog.ceo/api/breeds/image/random"
        private const val MINIMUM_IMAGES = 10
        private const val MAXIMUM_IMAGES = 20
        private const val MINIMUM_FACT_INDEX = 0
        private const val MAXIMUM_FACT_INDEX = 100
    }

    private val client = OkHttpClient()
    private val gson = Gson();
    private val requestData: Request = Request.Builder()
        .url(IMAGE_URL)
        .build()
    private val requestFact: HttpUrl.Builder =
        HttpUrl.parse(FACT_URL).newBuilder()

    /**
     * Функция для получения списка изображений.
     *
     * @return cписок с рандомным количеством изображений
     */
    fun getData(): List<DogImageModel> {
        val list = ArrayList<DogImageModel>()
        for (i in 1..Random.nextInt(MINIMUM_IMAGES,MAXIMUM_IMAGES)) {
            val response = client.newCall(requestData).execute()
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
     */
    fun getFact(): String {
        requestFact.addQueryParameter("index", Random.nextInt(MINIMUM_FACT_INDEX, MAXIMUM_FACT_INDEX).toString())
        val request = Request.Builder().url(requestFact.build()).build()
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