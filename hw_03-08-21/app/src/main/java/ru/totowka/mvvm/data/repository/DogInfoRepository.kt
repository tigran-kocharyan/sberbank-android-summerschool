package ru.totowka.mvvm.data.repository

import io.reactivex.Single
import ru.totowka.mvvm.data.model.DogImageModel

/**
 * Интерфейс для взаимодействия с репозиторием работы информации с собаками
 */
interface DogInfoRepository {
    fun loadDataAsyncRx(): Single<List<DogImageModel>>
    fun loadDogFactAsyncRx(): Single<String>
}