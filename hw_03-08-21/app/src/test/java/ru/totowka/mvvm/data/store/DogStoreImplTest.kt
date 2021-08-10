package ru.totowka.mvvm.data.store

import android.content.SharedPreferences
import io.mockk.*
import io.mockk.verify
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test
import ru.totowka.mvvm.data.model.DogImageModel

class DogStoreImplTest {
    companion object {
        private const val DOGS_KEY = "dogs_key"
    }

    lateinit var dogStore: DogStore
    lateinit var json: Json
    lateinit var preferences: SharedPreferences

    @Before
    fun setUp() {
        preferences = mockk()
        mockkStatic(Json::class)
        json = mockk()
        dogStore = DogStoreImpl(preferences, json)
    }

    /**
     * С данным тестом есть некоторые проблемы, так как mockk() не поддерживает полноценный mock-инг inline функций.
     * По-сути, тест написан правильный по синтаксису mock'а обычной функции. Прошу не учитывать его пока не будет
     * добавлена поддержка mock'а inline-функций.
     */
    @Test
    fun `getDogPictures returns correct list`() {
        every { preferences.getString(DOGS_KEY, null) } returns ""
        every { json.decodeFromString<List<Any>>(any()) } returns emptyList()

        dogStore.getDogPictures()

        verify(exactly = 1) { preferences.getString(DOGS_KEY, null) }
        verify(exactly = 1) { json.decodeFromString<List<DogImageModel>?>("") }
    }

    @Test
    fun `saveDogPictures with empty list`() {
        dogStore.saveDogPictures(emptyList())

        verify { preferences wasNot Called }
        verify { json wasNot Called }
    }

    @Test
    fun `getDogPictures returns null`() {
        every { preferences.getString(DOGS_KEY, null) } returns null

        dogStore.getDogPictures()

        verify(exactly = 1) { preferences.getString(DOGS_KEY, null) }
        verify { json wasNot Called }
    }
}