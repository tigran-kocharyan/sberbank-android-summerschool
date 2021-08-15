package ru.totowka.mvvm.di

import android.app.Activity
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import ru.totowka.mvvm.data.provider.DogInfoProvider
import ru.totowka.mvvm.data.provider.DogInfoProviderImpl
import ru.totowka.mvvm.data.repository.DogInfoRepositoryImpl
import ru.totowka.mvvm.data.store.DogStore
import ru.totowka.mvvm.data.store.DogStoreImpl
import ru.totowka.mvvm.presentation.utils.scheduler.SchedulersProviderImpl
import javax.inject.Named
import javax.inject.Singleton

@Module
class DogModule {
    companion object {
        private const val PREFS_NAME = "PREFS"
    }

    @Provides
    @Named("PREFS")
    @Singleton
    fun providePreferenceName(): String = PREFS_NAME

    @Provides
    @Singleton
    fun  provideSharedPreferences(activity: Activity, @Named("PREFS") prefs: String): SharedPreferences =
        activity.getSharedPreferences(prefs, AppCompatActivity.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideJson() = Json { ignoreUnknownKeys = true }

    @Provides
    @Singleton
    fun provideSchedulersProvider() = SchedulersProviderImpl()

    @Provides
    @Singleton
    fun provideDogStore(prefs: SharedPreferences, json: Json) = DogStoreImpl(prefs, json)

    @Provides
    @Singleton
    fun provideDogInfoRepository(provider: DogInfoProvider, storage: DogStore) =
        DogInfoRepositoryImpl(provider, storage)

    @Provides
    @Singleton
    fun provideDogInfoProvider() = DogInfoProviderImpl()
}