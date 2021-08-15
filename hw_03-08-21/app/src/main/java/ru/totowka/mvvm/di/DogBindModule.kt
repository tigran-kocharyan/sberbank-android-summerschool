package ru.totowka.mvvm.di

import dagger.Binds
import dagger.Module
import ru.totowka.mvvm.data.provider.DogInfoProvider
import ru.totowka.mvvm.data.provider.DogInfoProviderImpl
import ru.totowka.mvvm.data.repository.DogInfoRepository
import ru.totowka.mvvm.data.repository.DogInfoRepositoryImpl
import ru.totowka.mvvm.data.store.DogStore
import ru.totowka.mvvm.data.store.DogStoreImpl
import ru.totowka.mvvm.presentation.utils.scheduler.SchedulersProvider
import ru.totowka.mvvm.presentation.utils.scheduler.SchedulersProviderImpl

@Module
interface DogBindModule {
    @Binds
    fun bindDogInfoProvider(impl: DogInfoProviderImpl): DogInfoProvider

    @Binds
    fun bindDogStore(impl: DogStoreImpl): DogStore

    @Binds
    fun bindDogInfoRepository(impl: DogInfoRepositoryImpl): DogInfoRepository

    @Binds
    fun bindSchedulersProvider (impl: SchedulersProviderImpl): SchedulersProvider
}