package ru.totowka.mvvm.data.repository

import io.reactivex.Single
import ru.totowka.mvvm.data.model.DogImageModel
import ru.totowka.mvvm.data.provider.DogInfoProvider

/**
 * Имплементация интерфейса DogInfoRepository
 *
 * @param dogInfoProvider провайдер для работы с методами обращения к сети
 */
class DogInfoRepositoryImpl(private val dogInfoProvider: DogInfoProvider) : DogInfoRepository {
    /**
     * Метод для запуска получения изображений
     *
     * @return Single для подписки и получения саиска DogImageModel
     */
    override fun loadDataAsyncRx(): Single<List<DogImageModel>> {
        return Single.fromCallable { dogInfoProvider.getData() }
    }

    /**
     * Метод для запуска получения факта
     *
     * @return Single для подписки на получение String
     */
    override fun loadDogFactAsyncRx(): Single<String> {
        return Single.fromCallable {dogInfoProvider.getFact()}
    }
}