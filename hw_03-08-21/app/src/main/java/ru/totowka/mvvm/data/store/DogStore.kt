package ru.totowka.mvvm.data.store

import ru.totowka.mvvm.data.model.DogImageModel

interface DogStore {
    /**
     * Сохранить изображения всех собак
     * @param dogs список изображений собак
     */
    fun saveDogPictures(dogs: List<DogImageModel>)

    /**
     * Получить изображения всех собак
     *
     * @return список пользователей [DogImageModel], [null] если пользователей нет
     */
    fun getDogPictures(): List<DogImageModel>?
}