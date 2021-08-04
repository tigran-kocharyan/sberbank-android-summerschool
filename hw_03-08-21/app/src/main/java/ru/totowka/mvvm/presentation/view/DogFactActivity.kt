package ru.totowka.mvvm.presentation.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import ru.totowka.mvvm.R
import ru.totowka.mvvm.data.provider.DogInfoProvider
import ru.totowka.mvvm.data.repository.DogInfoRepository
import ru.totowka.mvvm.data.repository.DogInfoRepositoryImpl
import ru.totowka.mvvm.databinding.ActivityDogInfoBinding
import ru.totowka.mvvm.presentation.viewmodel.DogFactViewModel
import ru.totowka.mvvm.presentation.viewmodel.DogInfoViewModel

/**
 * Окно с отображением изображения собаки и рандомного факта о собаках
 */
class DogFactActivity : AppCompatActivity() {
    private lateinit var dogInfoActivityBinding: ActivityDogInfoBinding
    private lateinit var viewModel: DogFactViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dogInfoActivityBinding = ActivityDogInfoBinding.inflate(layoutInflater)
        val view = dogInfoActivityBinding.root
        setContentView(view)
        intent.getStringExtra(DOG_IMG_URL)?.let { updateImg(it) }

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

    private fun createViewModel() {
        val provider = DogInfoProvider()
        val repository: DogInfoRepository = DogInfoRepositoryImpl(provider)

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return DogFactViewModel(repository) as T
            }
        }).get(DogFactViewModel::class.java)
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
        Snackbar.make(dogInfoActivityBinding.root, throwable.toString(), BaseTransientBottomBar.LENGTH_SHORT).show();
    }

    companion object {
        private const val DOG_IMG_URL = "doginfourl"
    }
}