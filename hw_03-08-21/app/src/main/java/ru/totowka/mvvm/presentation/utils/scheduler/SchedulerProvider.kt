package ru.totowka.mvvm.presentation.utils.scheduler

import io.reactivex.Scheduler

interface SchedulersProvider {
    fun io(): Scheduler
    fun ui(): Scheduler
}