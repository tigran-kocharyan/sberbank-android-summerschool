package ru.totowka.mvvm.presentation.view.dogs

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.serialization.json.Json
import ru.totowka.mvvm.data.model.DogImageModel
import ru.totowka.mvvm.data.provider.DogInfoProviderImpl
import ru.totowka.mvvm.data.repository.DogInfoRepository
import ru.totowka.mvvm.data.repository.DogInfoRepositoryImpl
import ru.totowka.mvvm.data.store.DogStoreImpl
import ru.totowka.mvvm.databinding.ActivityMainBinding
import ru.totowka.mvvm.presentation.view.dogs.adapter.PreviewRecyclerAdapter
import ru.totowka.mvvm.presentation.utils.scheduler.SchedulersProviderImpl

/**
 * Основное окно приложения со списком, который хранит в себе изображение и номер факта.
 */
class MainActivity : AppCompatActivity() {

    companion object {
        private const val PREFS_NAME = "PREFS"
        private const val TAG = "LiveData"
    }

    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var viewModel: DogInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = activityMainBinding.root
        setContentView(view)

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

    private fun createViewModel() {
        val provider = DogInfoProviderImpl()
        val json = Json { ignoreUnknownKeys = true }
        val schedulerProvider = SchedulersProviderImpl()
        val storage = DogStoreImpl(this.getSharedPreferences(PREFS_NAME, MODE_PRIVATE), json)
        val repository: DogInfoRepository = DogInfoRepositoryImpl(provider, storage)

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
        Log.i(TAG, "showProgress called with param = $isVisible");
        activityMainBinding.progressbar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun showData(list: List<DogImageModel>) {
        Log.d(TAG, "showData() called with: list = $list")
        activityMainBinding.recyclerview.adapter = PreviewRecyclerAdapter(list)
    }

    private fun showError(throwable: Throwable) {
        Log.d(TAG, "showError() called with: throwable = $throwable")
        Snackbar.make(activityMainBinding.root, throwable.toString(), BaseTransientBottomBar.LENGTH_SHORT).show();
    }
}