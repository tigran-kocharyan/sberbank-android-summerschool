package ru.totowka.mvvm.presentation.view.dogs

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import ru.totowka.mvvm.data.model.DogImageModel
import ru.totowka.mvvm.data.repository.DogInfoRepository
import ru.totowka.mvvm.data.repository.DogInfoRepositoryImpl
import ru.totowka.mvvm.databinding.ActivityMainBinding
import ru.totowka.mvvm.di.DaggerDogComponent
import ru.totowka.mvvm.di.DogComponent
import ru.totowka.mvvm.presentation.utils.scheduler.SchedulersProvider
import ru.totowka.mvvm.presentation.utils.scheduler.SchedulersProviderImpl
import ru.totowka.mvvm.presentation.view.dogs.adapter.PreviewRecyclerAdapter
import javax.inject.Inject

/**
 * Основное окно приложения со списком, который хранит в себе изображение и номер факта.
 */
class MainActivity : AppCompatActivity() {

    companion object {
        private const val PREFS_NAME = "PREFS"
        private const val TAG = "LiveData"
    }

    private lateinit var dogComponent: DogComponent
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var viewModel: DogInfoViewModel

    @Inject lateinit var schedulerProvider: SchedulersProviderImpl
    @Inject lateinit var repository: DogInfoRepositoryImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = activityMainBinding.root
        setContentView(view)
        dogComponent = initDagger()
        dogComponent.inject(this)

        createViewModel()
        observeLiveData()

        activityMainBinding.swiperefresh.setOnRefreshListener {
            viewModel.loadDataAsyncRx(isSwiped = true)
            activityMainBinding.swiperefresh.isRefreshing = false
        }
        if (savedInstanceState == null) {
            viewModel.loadDataAsyncRx()
        }
    }

    private fun initDagger(): DogComponent =
        DaggerDogComponent.builder()
            .activity(this)
            .build()

    private fun createViewModel() {
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return DogInfoViewModel(repository, schedulerProvider) as T
            }
        }).get(DogInfoViewModel::class.java)
    }

    private fun observeLiveData() {
        viewModel.getErrorLiveData().observe(this, this::showError)
        viewModel.getProgressLiveData().observe(this, this::showProgress)
        viewModel.getDogInfoLiveData().observe(this, this::showData)
    }

    private fun showProgress(isVisible: Boolean) {
        Log.i(TAG, "showProgress called with param = $isVisible")
        activityMainBinding.progressbar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun showData(list: List<DogImageModel>) {
        Log.d(TAG, "showData() called with: list = $list")
        activityMainBinding.recyclerview.adapter = PreviewRecyclerAdapter(list)
    }

    private fun showError(throwable: Throwable) {
        Log.d(TAG, "showError() called with: throwable = $throwable")
        Snackbar.make(activityMainBinding.root, throwable.toString(), BaseTransientBottomBar.LENGTH_SHORT).show()
    }
}