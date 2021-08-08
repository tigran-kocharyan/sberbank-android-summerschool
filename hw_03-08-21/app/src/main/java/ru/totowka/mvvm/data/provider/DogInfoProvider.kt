package ru.totowka.mvvm.data.provider

import ru.totowka.mvvm.data.model.DogImageModel
import kotlin.random.Random

/**
 * Класс-провайдер для отправки асинхронных запросов в сеть для получения факта о собаках и изображения собаки
 */
interface DogInfoProvider {
    companion object {
        private const val FACT_URL = "https://dog-facts-api.herokuapp.com/api/v1/resources/dogs"
        private const val IMAGE_URL = "https://dog.ceo/api/breeds/image/random"
        private const val MINIMUM_IMAGES = 10
        private const val MAXIMUM_IMAGES = 20
    }

    /**
     * Функция для получения списка изображений.
     *
     * @return cписок с рандомным количеством изображений
     */
    fun getData(
        amount: Int = Random.nextInt(MINIMUM_IMAGES, MAXIMUM_IMAGES),
        url: String = IMAGE_URL
    ): List<DogImageModel>


    /**
     * Функция для получения рандомного факта.
     *
     * @return рандомное строку факта
     */
    fun getFact(url: String = FACT_URL): String
}