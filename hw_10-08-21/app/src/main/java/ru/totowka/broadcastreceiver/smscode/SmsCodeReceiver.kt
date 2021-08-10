package ru.totowka.broadcastreceiver.smscode

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.provider.Telephony.Sms.Intents.getMessagesFromIntent
import java.lang.ref.WeakReference


class SmsCodeReceiver(var listener: SmsCodeListener) : BroadcastReceiver() {
    companion object {
        private const val ERROR_MESSAGE = "Error while handling SMS."
        private const val PLACEHOLDER = "Никому не сообщайте код: "
        private const val PLACE_TO_SEEK = ": "
        private const val CODE_DIGITS = 7
        private const val EXAMPLE = "Никому не сообщайте код: 8789789. После подтверждения произойдет авторизация в системе"
    }

    private val smsCodeListener: WeakReference<SmsCodeListener> = WeakReference(listener)
    private val exception = SmsCodeException(ERROR_MESSAGE)

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null || intent.action != (Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            error()
            return
        } else {
            val message = getCode(intent)
            message?.let { smsCodeListener.get()?.onCodeReceived(it) } ?: error()
        }
    }

    private fun error() {
        smsCodeListener.get()?.onWrongCodeReceived(exception)
    }

    private fun getCode(intent: Intent): String? {
        val pdus = getMessagesFromIntent(intent)
        val message = pdus[0].messageBody
        return if(message.contains(PLACEHOLDER)) {
            val startIndex = message.lastIndexOf(PLACE_TO_SEEK) + PLACE_TO_SEEK.length
            val endIndex = startIndex + CODE_DIGITS
            message.substring( startIndex, endIndex)
        } else null
    }
}