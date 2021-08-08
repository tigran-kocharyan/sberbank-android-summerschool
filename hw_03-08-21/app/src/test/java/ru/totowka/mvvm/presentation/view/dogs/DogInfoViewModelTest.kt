package ru.totowka.mvvm.presentation.view.dogs

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
import ru.totowka.mvvm.data.model.DogImageModel
import ru.totowka.mvvm.data.repository.DogInfoRepository
import ru.totowka.mvvm.presentation.utils.scheduler.SchedulersProvider
import ru.totowka.mvvm.presentation.utils.scheduler.SchedulersProviderImplStub
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class DogInfoViewModelTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val dogInfoRepository: DogInfoRepository = mockk()
    private val scheduler: SchedulersProvider = SchedulersProviderImplStub()
    lateinit var viewModel: DogInfoViewModel

    private val dogsObserver: Observer<List<DogImageModel>> = mockk()
    private val progressObserver: Observer<Boolean> = mockk()
    private val errorObserver: Observer<Throwable> = mockk()

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        viewModel = DogInfoViewModel(dogInfoRepository, scheduler)
        viewModel.getDogInfoLiveData().observeForever(dogsObserver)
        viewModel.getErrorLiveData().observeForever(errorObserver)
        viewModel.getProgressLiveData().observeForever(progressObserver)

        every { dogsObserver.onChanged(any()) } just Runs
        every { progressObserver.onChanged(any()) } just Runs
        every { errorObserver.onChanged(any()) } just Runs
    }

    @Test
    fun `loadDataAsyncRx is success with isSwiped = false`() {
        every { dogInfoRepository.loadDataAsyncRx() }
        every { dogInfoRepository.loadDataAsyncRx(any(), any(), isSwiped = false) } returns Single.just(dogImageModels())
        viewModel.loadDataAsyncRx(isSwiped = false)
        verifySequence {
            progressObserver.onChanged(true)
            dogsObserver.onChanged(dogImageModels())
            progressObserver.onChanged(false)
        }
        verify { errorObserver wasNot Called }
    }

    @Test
    fun `loadDataAsyncRx is success with isSwiped = true`() {
        every { dogInfoRepository.loadDataAsyncRx(any(), any(), isSwiped = true) } returns  Single.just(dogImageModels())
        viewModel.loadDataAsyncRx(isSwiped = true)
        verifySequence {
            progressObserver.onChanged(true)
            dogsObserver.onChanged(dogImageModels())
            progressObserver.onChanged(false)
        }
        verify { errorObserver wasNot Called }
    }


    @Test
    fun `loadDataAsyncRx throws error with wrong URL`() {
        val exception = IOException("Test")
        every { dogInfoRepository.loadDataAsyncRx(any(), "", any()) } returns  Single.error(exception)
        viewModel.loadDataAsyncRx(url = "")
        verifySequence {
            progressObserver.onChanged(true)
            errorObserver.onChanged(exception)
            progressObserver.onChanged(false)
        }
        verify { dogsObserver wasNot Called }
    }

    @Test
    fun `loadDataAsyncRx throws error`() {
        val exception = IOException("Test")
        every { dogInfoRepository.loadDataAsyncRx(any(), any(), isSwiped = false) } returns Single.error(exception)
        viewModel.loadDataAsyncRx(isSwiped = false)
        verifySequence {
            progressObserver.onChanged(true)
            errorObserver.onChanged(exception)
            progressObserver.onChanged(false)
        }
        verify { dogsObserver wasNot Called }
    }

    private fun dogImageModels(): List<DogImageModel> = listOf(DogImageModel(""))
}