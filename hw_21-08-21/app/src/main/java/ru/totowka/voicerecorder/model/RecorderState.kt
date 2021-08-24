package ru.totowka.voicerecorder.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class RecorderState : Parcelable {
    INITIALIZED,
    START,
    PAUSE,
    STOP
}