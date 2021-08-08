package ru.totowka.mvvm.data.store

import android.content.SharedPreferences
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.totowka.mvvm.data.model.DogImageModel

/**
 * Релазизация [DogStore]
 */
class DogStoreImpl(
    private val preferences: SharedPreferences,
    private val json: Json
) : DogStore {

    companion object {
        private const val DOGS_KEY = "dogs_key"
    }

    override fun saveDogPictures(dogs: List<DogImageModel>) {
        if (dogs.isEmpty())
            return
        preferences.edit()
            .putString(DOGS_KEY, json.encodeToString(dogs))
            .apply()
    }

    override fun getDogPictures(): List<DogImageModel>? {
        val dogs = preferences.getString(DOGS_KEY, null)
        var result = emptyList<DogImageModel>()
        if(dogs != null) {
            result = json.decodeFromString<List<DogImageModel>>(dogs)
        } else {
            return null
        }
        return result
    }
}