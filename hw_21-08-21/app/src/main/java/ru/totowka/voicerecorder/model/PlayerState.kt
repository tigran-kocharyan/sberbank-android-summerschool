package ru.totowka.voicerecorder.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class PlayerState : Parcelable {
    INITIALIZED,
    START,
    PAUSE,
    STOP
}