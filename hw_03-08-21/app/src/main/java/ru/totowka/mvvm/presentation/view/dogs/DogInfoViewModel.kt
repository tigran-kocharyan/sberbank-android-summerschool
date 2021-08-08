package ru.totowka.mvvm.presentation.view.dogs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import ru.totowka.mvvm.data.model.DogImageModel
import ru.totowka.mvvm.data.repository.DogInfoRepository
import ru.totowka.mvvm.presentation.utils.scheduler.SchedulersProvider
import kotlin.random.Random

/**
 * ViewModel для работы с экраном MainActivity
 *
 * @param dogInfoRepository репозиторий для работы с методами обращения к сети
 */
class DogInfoViewModel(private val dogInfoRepository: DogInfoRepository, private val schedulers: SchedulersProvider) :
    ViewModel() {

    companion object {
        private const val FACT_URL = "https://dog-facts-api.herokuapp.com/api/v1/resources/dogs"
        private const val IMAGE_URL = "https://dog.ceo/api/breeds/image/random"
        private const val MINIMUM_IMAGES = 10
        private const val MAXIMUM_IMAGES = 20
    }

    private val progressLiveData = MutableLiveData<Boolean>()
    private val errorLiveData = MutableLiveData<Throwable>()
    private val dogImageLiveData: MutableLiveData<List<DogImageModel>> = MutableLiveData<List<DogImageModel>>()
    private var disposable: Disposable? = null

    /**
     * Загрузка списка изображений в IO-потоке и оповещение View об изменениях
     */
    fun loadDataAsyncRx(
        isSwiped: Boolean = false,
        amount: Int = Random.nextInt(MINIMUM_IMAGES, MAXIMUM_IMAGES),
        url: String = IMAGE_URL
    ) {
        disposable =
            dogInfoRepository.loadDataAsyncRx(amount = amount, url = url, isSwiped = isSwiped)
                .doOnSubscribe { progressLiveData.postValue(true) }
                .doAfterTerminate { progressLiveData.postValue(false) }
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
                .subscribe(dogImageLiveData::setValue, errorLiveData::setValue)
    }

    override fun onCleared() {
        super.onCleared()

        disposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
            disposable = null
        }
    }

    /**
     * @return LiveData<Boolean> для подписки
     */
    fun getProgressLiveData(): LiveData<Boolean> {
        return progressLiveData
    }

    /**
     * @return LiveData<Boolean> для подписки
     */
    fun getErrorLiveData(): LiveData<Throwable> {
        return errorLiveData
    }

    /**
     * @return LiveData<List<DogImageModel>> для подписки
     */
    fun getDogInfoLiveData(): LiveData<List<DogImageModel>> {
        return dogImageLiveData
    }
}