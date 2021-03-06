package ru.totowka.mvvm.presentation.view.dogfact

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.serialization.json.Json
import ru.totowka.mvvm.R
import ru.totowka.mvvm.data.provider.DogInfoProviderImpl
import ru.totowka.mvvm.data.repository.DogInfoRepository
import ru.totowka.mvvm.data.repository.DogInfoRepositoryImpl
import ru.totowka.mvvm.data.store.DogStoreImpl
import ru.totowka.mvvm.databinding.ActivityDogInfoBinding
import ru.totowka.mvvm.databinding.ActivityMainBinding
import ru.totowka.mvvm.di.DaggerDogComponent
import ru.totowka.mvvm.di.DogComponent
import ru.totowka.mvvm.presentation.utils.scheduler.SchedulersProviderImpl
import ru.totowka.mvvm.presentation.view.dogs.DogInfoViewModel
import javax.inject.Inject

/**
 * Окно с отображением изображения собаки и рандомного факта о собаках
 */
class DogFactActivity : AppCompatActivity() {
    companion object {
        private const val DOG_IMG_URL = "dog_info_url"
    }

    private lateinit var dogComponent: DogComponent
    @Inject
    lateinit var schedulerProvider: SchedulersProviderImpl
    @Inject
    lateinit var repository: DogInfoRepositoryImpl


    private lateinit var dogInfoActivityBinding: ActivityDogInfoBinding
    private lateinit var viewModel: DogFactViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dogInfoActivityBinding = ActivityDogInfoBinding.inflate(layoutInflater)
        val view = dogInfoActivityBinding.root
        dogComponent = initDagger()
        dogComponent.inject(this)
        setContentView(view)
        if(getFragmentPreferencesOption()) {
            dogInfoActivityBinding.image.visibility = View.VISIBLE
            intent.getStringExtra(DOG_IMG_URL)?.let { updateImg(it) }
        } else {
            dogInfoActivityBinding.image.visibility = View.GONE
        }
        createViewModel()
        observeLiveData()

        if (savedInstanceState == null) {
            viewModel.loadFactAsyncRx()
        }
    }

    private fun updateImg(dogUrl: String) {
        Glide.with(this)
            .load(dogUrl)
            .error(R.drawable.ic_baseline_broken_image_24)
            .centerCrop()
            .into(dogInfoActivityBinding.image)
    }

    private fun initDagger(): DogComponent =
        DaggerDogComponent.builder()
            .activity(this)
            .build()

    private fun createViewModel() {
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return DogFactViewModel(repository, schedulerProvider) as T
            }
        }).get(DogFactViewModel::class.java)
    }

    private fun getFragmentPreferencesOption() : Boolean {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean("show_pics", true)
    }

    private fun observeLiveData() {
        viewModel.getErrorLiveData().observe(this, this::showError)
        viewModel.getProgressLiveData().observe(this, this::showProgress)
        viewModel.getDogFactLiveData().observe(this, this::showData)
    }

    private fun showProgress(isVisible: Boolean) {
        dogInfoActivityBinding.progressbar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun showData(fact: String) {
        dogInfoActivityBinding.description.text = fact
    }

    private fun showError(throwable: Throwable) {
        Log.d("error", "showError() called with: throwable = $throwable")
        Snackbar.make(dogInfoActivityBinding.root, throwable.toString(), BaseTransientBottomBar.LENGTH_SHORT).show()
    }
}