package ru.totowka.okhttp

import io.reactivex.Single

interface HttpClient {
    fun get(url: String) : Single<String?>
    fun post(url: String) : Single<String?>
}