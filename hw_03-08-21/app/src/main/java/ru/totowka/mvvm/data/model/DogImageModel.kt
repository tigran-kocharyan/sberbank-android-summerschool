package ru.totowka.mvvm.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Объект, который хранит в себе ссылку на изображение собаки
 */

@Serializable
data class DogImageModel(@SerialName("imgUrl") val imgUrl: String)
