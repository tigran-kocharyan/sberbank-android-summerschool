package ru.totowka.mvvm.data.repository

import io.reactivex.Single
import ru.totowka.mvvm.data.model.DogImageModel
import ru.totowka.mvvm.data.provider.DogInfoProvider
import ru.totowka.mvvm.data.store.DogStore
import java.io.IOException

/**
 * Релазизация [DogInfoRepository]
 */
class DogInfoRepositoryImpl(private val dogInfoProvider: DogInfoProvider, private val dogStore: DogStore) :
    DogInfoRepository {
    /**
     * Метод для запуска получения изображений
     *
     * @return Single для подписки и получения саиска DogImageModel
     *
     * @throws IOException if the request could not be executed due to
     *     cancellation, a connectivity problem or timeout. Because networks can
     *     fail during an exchange, it is possible that the remote server
     *     accepted the request before the failure.
     *
     * @throws IllegalStateException when the call has already been executed.
     */
    @Throws(IOException::class, IllegalStateException::class)
    override fun loadDataAsyncRx(
        amount: Int,
        url: String,
        isSwiped: Boolean
    ): Single<List<DogImageModel>> {
        return when (isSwiped) {
            true -> Single.fromCallable {
                dogInfoProvider.getData(amount, url).also { dogStore.saveDogPictures(it) }
            }
            false -> Single.fromCallable {
                dogStore.getDogPictures() ?: dogInfoProvider.getData(amount, url).also { dogStore.saveDogPictures(it) }
            }
        }
    }

    /**
     * Метод для запуска получения факта
     *
     * @return Single для подписки на получение String
     *
     * @throws IOException if the request could not be executed due to
     *     cancellation, a connectivity problem or timeout. Because networks can
     *     fail during an exchange, it is possible that the remote server
     *     accepted the request before the failure.
     *
     * @throws IllegalStateException when the call has already been executed.
     */
    @Throws(IOException::class, IllegalStateException::class)
    override fun loadDogFactAsyncRx(url: String): Single<String> {
        return Single.fromCallable { dogInfoProvider.getFact(url) }
    }
}