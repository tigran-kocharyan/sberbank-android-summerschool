package ru.totowka.mvvm.presentation.view.dogfact

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.*
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import ru.totowka.mvvm.data.repository.DogInfoRepository
import ru.totowka.mvvm.presentation.utils.scheduler.SchedulersProvider
import ru.totowka.mvvm.presentation.utils.scheduler.SchedulersProviderImplStub
import java.io.IOException


@RunWith(MockitoJUnitRunner::class)
class DogFactViewModelTest {
    companion object {
        private const val DOG_FACT = "fact"
    }

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val dogInfoRepository: DogInfoRepository = mockk(relaxed = true)
    private val scheduler: SchedulersProvider = SchedulersProviderImplStub()
    lateinit var viewModel: DogFactViewModel

    private val dogsObserver: Observer<String> = mockk()
    private val progressObserver: Observer<Boolean> = mockk()
    private val errorObserver: Observer<Throwable> = mockk()

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        viewModel = DogFactViewModel(dogInfoRepository, scheduler)
        viewModel.getDogFactLiveData().observeForever(dogsObserver)
        viewModel.getErrorLiveData().observeForever(errorObserver)
        viewModel.getProgressLiveData().observeForever(progressObserver)

        every { dogsObserver.onChanged(any()) } just Runs
        every { progressObserver.onChanged(any()) } just Runs
        every { errorObserver.onChanged(any()) } just Runs
    }

    @Test
    fun `loadDogFactAsyncRx is success`() {
        every { dogInfoRepository.loadDogFactAsyncRx() } returns Single.just(DOG_FACT)
        viewModel.loadFactAsyncRx()
        verifySequence {
            progressObserver.onChanged(true)
            dogsObserver.onChanged(DOG_FACT)
            progressObserver.onChanged(false)
        }
        verify { errorObserver wasNot Called }
    }

    @Test
    fun `loadDogFactAsyncRx throws error`() {
        val exception = IOException("Test")
        every { dogInfoRepository.loadDogFactAsyncRx() } returns Single.error(exception)
        viewModel.loadFactAsyncRx()
        verifySequence {
            progressObserver.onChanged(true)
            errorObserver.onChanged(exception)
            progressObserver.onChanged(false)
        }
        verify { dogsObserver wasNot Called }
    }
}