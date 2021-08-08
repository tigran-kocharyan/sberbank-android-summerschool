package ru.totowka.mvvm.presentation.view.dogfact

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import ru.totowka.mvvm.data.repository.DogInfoRepository
import ru.totowka.mvvm.presentation.utils.scheduler.SchedulersProvider

/**
 * ViewModel для работы с экраном DogFactActivity
 *
 * @param dogInfoRepository репозиторий для работы с методами обращения к сети
 */
class DogFactViewModel(private val dogInfoRepository: DogInfoRepository, private val schedulers: SchedulersProvider) : ViewModel() {
    private val progressLiveData = MutableLiveData<Boolean>()
    private val errorLiveData = MutableLiveData<Throwable>()
    private val dogFactLiveData = MutableLiveData<String>()
    private var disposable: Disposable? = null

    /**
     * Загрузка факта в IO-потоке и оповещение View об изменениях
     */
    fun loadFactAsyncRx() {
        disposable = dogInfoRepository.loadDogFactAsyncRx()
            .doOnSubscribe { progressLiveData.postValue(true) }
            .doAfterTerminate { progressLiveData.postValue(false) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe(dogFactLiveData::setValue, errorLiveData::setValue)
    }

    override fun onCleared() {
        super.onCleared()

        disposable?.let {
            if(!it.isDisposed) {
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
     * @return LiveData<Throwable> для подписки
     */
    fun getErrorLiveData(): LiveData<Throwable> {
        return errorLiveData
    }

    /**
     * @return LiveData<String> для подписки
     */
    fun getDogFactLiveData(): LiveData<String> {
        return dogFactLiveData
    }
}