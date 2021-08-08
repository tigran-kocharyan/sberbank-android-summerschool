package ru.totowka.mvvm.data.repository

import com.google.common.truth.Truth
import io.mockk.Called
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import ru.totowka.mvvm.data.model.DogImageModel
import ru.totowka.mvvm.data.provider.DogInfoProvider
import ru.totowka.mvvm.data.store.DogStore


class DogInfoRepositoryImplTest {
    companion object {
        private const val FACT_URL = "https://dog-facts-api.herokuapp.com/api/v1/resources/dogs?index=100"
        private const val FACT_URL_RESULT =
            "It pays to be a lap dog. Three dogs \"from First Class cabins!\" survived the sinking of the Titanic – two Pomeranians and one Pekingese."
        private const val RANDOM_IMAGE_URL = "https://dog.ceo/api/breeds/image/random"
        private const val DOG_IMAGE_URL = "https://images.dog.ceo/breeds/terrier-australian/n02096294_1564.jpg"

        private const val TEN_ELEMENTS = 10
    }

    lateinit var dogInfoProvider: DogInfoProvider
    lateinit var dogInfoRepository: DogInfoRepository
    lateinit var dogStore: DogStore


    @Before
    fun setUp() {
        dogInfoProvider = spyk()
        dogStore = spyk()
        dogInfoRepository = DogInfoRepositoryImpl(dogInfoProvider, dogStore)

    }

    @Test
    fun `loadDataAsyncRx is successful with amount param`() {
        // Arrange
        every { dogInfoProvider.getData(1) } returns mutableListOf(DogImageModel(DOG_IMAGE_URL))
        val expected = mutableListOf(DogImageModel(DOG_IMAGE_URL))

        // Act
        val actual = dogInfoRepository.loadDataAsyncRx(amount = 1).blockingGet()

        // Assert
        Truth.assertThat(actual).isEqualTo(expected)
        verify(exactly = 1) { dogInfoProvider.getData(amount = 1) }
    }

    @Test
    fun `loadDataAsyncRx is successful with saved dogs`() {
        // Arrange
        every { dogStore.getDogPictures() } returns mutableListOf(DogImageModel(DOG_IMAGE_URL))
        every { dogInfoProvider.getData() } returns mutableListOf(DogImageModel(DOG_IMAGE_URL), DogImageModel(DOG_IMAGE_URL))
        val expected = mutableListOf(DogImageModel(DOG_IMAGE_URL))

        // Act
        val actual = dogInfoRepository.loadDataAsyncRx().blockingGet()

        // Assert
        Truth.assertThat(actual).isEqualTo(expected)
        verify(exactly = 1) { dogStore.getDogPictures() }
        verify{dogInfoProvider wasNot Called}
    }

    @Test
    fun `loadDataAsyncRx is successful with refresh swipe`() {
        // Arrange
        every { dogStore.getDogPictures() } returns mutableListOf(DogImageModel(DOG_IMAGE_URL), DogImageModel(DOG_IMAGE_URL))
        every { dogStore.getDogPictures() } returns emptyList()
        every { dogInfoProvider.getData(any(), any()) } returns mutableListOf(DogImageModel(DOG_IMAGE_URL))

        val expected = mutableListOf(DogImageModel(DOG_IMAGE_URL))

        // Act
        val actual = dogInfoRepository.loadDataAsyncRx(isSwiped = true).blockingGet()

        // Assert
        Truth.assertThat(actual).isEqualTo(expected)
        verify(exactly = 1) { dogInfoProvider.getData(any(), any()) }
        verify(exactly = 1) { dogStore.saveDogPictures(expected) }
        verify(exactly = 0) { dogStore.getDogPictures() }
    }

    @Test
    fun `loadDataAsyncRx is successful with amount and URL param`() {
        // Arrange
        every { dogInfoProvider.getData(1, RANDOM_IMAGE_URL) } returns mutableListOf(DogImageModel(DOG_IMAGE_URL))
        val expected = mutableListOf(DogImageModel(DOG_IMAGE_URL))

        // Act
        val actual = dogInfoRepository.loadDataAsyncRx(amount = 1, url = RANDOM_IMAGE_URL).blockingGet()

        // Assert
        Truth.assertThat(actual).isEqualTo(expected)
        verify(exactly = 1) { dogInfoProvider.getData(amount = 1, url = RANDOM_IMAGE_URL) }
    }

    @Test
    fun `loadFactAsyncRx is successful with FACT_URL param`() {
        // Arrange
        every { dogInfoProvider.getFact(FACT_URL) } returns FACT_URL_RESULT
        var expected = FACT_URL_RESULT

        // Act
        val actual = dogInfoRepository.loadDogFactAsyncRx(FACT_URL).blockingGet()

        // Assert
        Truth.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `loadFactAsyncRx is successful with no params`() {
        // Arrange
        every { dogInfoProvider.getFact() } returns FACT_URL_RESULT
        var expected = FACT_URL_RESULT

        // Act
        val actual = dogInfoRepository.loadDogFactAsyncRx().blockingGet()

        // Assert
        Truth.assertThat(actual).isEqualTo(expected)
    }
}