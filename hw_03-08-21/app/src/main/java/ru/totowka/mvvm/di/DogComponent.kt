package ru.totowka.mvvm.di

import android.app.Activity
import dagger.BindsInstance
import dagger.Component
import ru.totowka.mvvm.presentation.view.dogfact.DogFactActivity
import ru.totowka.mvvm.presentation.view.dogs.MainActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [DogModule::class, DogBindModule::class])
interface DogComponent {
    fun inject(activity: MainActivity)
    fun inject(activity: DogFactActivity)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun activity(activity: Activity): Builder

        fun build(): DogComponent
    }
}