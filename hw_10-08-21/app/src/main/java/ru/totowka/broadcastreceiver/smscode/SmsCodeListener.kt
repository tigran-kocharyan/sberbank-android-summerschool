package ru.totowka.broadcastreceiver.smscode

import java.lang.RuntimeException

interface SmsCodeListener {
    fun onCodeReceived(code: String)
    fun onWrongCodeReceived(exception: RuntimeException)
}