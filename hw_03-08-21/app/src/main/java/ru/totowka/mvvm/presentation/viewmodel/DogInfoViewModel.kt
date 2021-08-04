package ru.totowka.mvvm.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.totowka.mvvm.data.model.DogImageModel
import ru.totowka.mvvm.data.repository.DogInfoRepository

/**
 * ViewModel для работы с экраном MainActivity
 *
 * @param dogInfoRepository репозиторий для работы с методами обращения к сети
 */
class DogInfoViewModel(private val dogInfoRepository: DogInfoRepository) : ViewModel() {

    private val progressLiveData = MutableLiveData<Boolean>()
    private val errorLiveData = MutableLiveData<Throwable>()
    private val dogImageLiveData: MutableLiveData<List<DogImageModel>> = MutableLiveData<List<DogImageModel>>()
    private var disposable: Disposable? = null

    /**
     * Загрузка списка изображений в IO-потоке и оповещение View об изменениях
     */
    fun loadDataAsyncRx() {
        disposable = dogInfoRepository.loadDataAsyncRx()
            .doOnSubscribe { progressLiveData.postValue(true) }
            .doAfterTerminate { progressLiveData.postValue(false) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(dogImageLiveData::setValue, errorLiveData::setValue)
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