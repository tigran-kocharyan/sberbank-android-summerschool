package ru.totowka.mvvm.data.provider

import com.google.common.truth.Truth
import com.google.gson.Gson
import com.squareup.okhttp.OkHttpClient
import io.mockk.every
import io.mockk.spyk
import junit.runner.Version
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import ru.totowka.mvvm.data.model.DogFactResponseModel
import ru.totowka.mvvm.data.model.DogImageResponseModel


internal class DogInfoProviderTest {
    companion object {
        private const val FACT_URL = "https://dog-facts-api.herokuapp.com/api/v1/resources/dogs?index=100"
        private const val FACT_RESPONSE_BODY =
            "[\n{\n\"fact\": \"The most popular dog breed in Canada, U.S., and Great Britain is the Labrador retriever.\"\n}\n]"

        private const val IMAGE_URL = "https://dog.ceo/api/breeds/image/random"
        private const val IMAGE_RESPONSE_BODY =
            "{\"message\":\"https:\\/\\/images.dog.ceo\\/breeds\\/terrier-fox\\/n02095314_293.jpg\",\"status\":\"success\"}"

        private const val TEN_ELEMENTS = 10
        private const val SUCCESSFUL_API_CALL_STATUS = "200"
    }

    lateinit var dogInfoProvider: DogInfoProvider
    lateinit var okHttpClient: OkHttpClient
    lateinit var gson: Gson

    @Before
    fun setUp() {
        okHttpClient = spyk()
        gson = spyk()

        dogInfoProvider = DogInfoProviderImpl(okHttpClient, gson)
    }

    @Test
    fun `getData returns 10 elements`() {
        // Arrange
        getDataSetup(true)
        val expectedResult = TEN_ELEMENTS

        // Act
        val collection = dogInfoProvider.getData(TEN_ELEMENTS)
        val actualResult = collection.size

        // Assert
        Truth.assertThat(actualResult).isEqualTo(expectedResult)
    }

    @Test
    fun `getData is successful with no params`() {
        getDataSetup(true)
        every { okHttpClient.newCall(any()).execute().isSuccessful } returns true

        Truth.assertThat(dogInfoProvider.getData()).isNotEmpty()
    }

    @Test
    fun `getData is successful with right URL`() {
        getDataSetup(true)
        every { okHttpClient.newCall(any()).execute().isSuccessful } returns true

        Truth.assertThat(dogInfoProvider.getData(url = IMAGE_URL)).isNotEmpty()
    }

    @Test
    fun `getData is failed with wrong URL`() {
        getDataSetup(false)
        try {
            dogInfoProvider.getData(url = "OBVIOUSLY_WRONG_URL")
        } catch (e: IllegalArgumentException) {
            Truth.assertThat(e).isInstanceOf(IllegalArgumentException::class.java)
        }
    }


    @Test
    fun `getFact is successful with right URL`() {
        getFactSetup(true)

        Truth.assertThat(dogInfoProvider.getFact(url = FACT_URL)).isNotEmpty()
    }

    @Test
    fun `getFact is successful with no params`() {
        getFactSetup(true)

        Truth.assertThat(dogInfoProvider.getFact()).isNotEmpty()
    }

    @Test
    fun `getFact is failed with wrong URL`() {
        getFactSetup(false)

        try {
            dogInfoProvider.getFact(url = "OBVIOUSLY_WRONG_URL")
        } catch (e: NullPointerException) {
            Truth.assertThat(e).isInstanceOf(NullPointerException::class.java)
        }
    }

    private fun getDataSetup(isSuccessful: Boolean) {
        every { okHttpClient.newCall(any()).execute().isSuccessful } returns isSuccessful
        every {
            okHttpClient.newCall(any()).execute().body()?.string()
        } returns IMAGE_RESPONSE_BODY
        every {
            gson.fromJson(
                anyString(),
                DogImageResponseModel::class.java
            )
        } returns DogImageResponseModel(
            IMAGE_RESPONSE_BODY,
            SUCCESSFUL_API_CALL_STATUS
        )
    }

    private fun getFactSetup(isSuccessful: Boolean) {
        every { okHttpClient.newCall(any()).execute().isSuccessful } returns isSuccessful
        every {
            okHttpClient.newCall(any()).execute().body()?.string()
        } returns FACT_RESPONSE_BODY
        every {
            gson.fromJson(
                anyString(),
                DogFactResponseModel::class.java
            )
        } returns DogFactResponseModel(
            FACT_RESPONSE_BODY
        )
    }
}