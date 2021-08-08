package ru.totowka.mvvm.data.repository

import io.reactivex.Single
import ru.totowka.mvvm.data.model.DogImageModel
import kotlin.random.Random

/**
 * Интерфейс для взаимодействия с репозиторием работы информации с собаками
 */
interface DogInfoRepository {
    companion object {
        private const val FACT_URL = "https://dog-facts-api.herokuapp.com/api/v1/resources/dogs"
        private const val IMAGE_URL = "https://dog.ceo/api/breeds/image/random"
        private const val MINIMUM_IMAGES = 10
        private const val MAXIMUM_IMAGES = 20
    }

    fun loadDataAsyncRx(
        amount: Int = Random.nextInt(MINIMUM_IMAGES, MAXIMUM_IMAGES),
        url: String = IMAGE_URL,
        isSwiped: Boolean = false
    ): Single<List<DogImageModel>>

    fun loadDogFactAsyncRx(url: String = FACT_URL): Single<String>
}