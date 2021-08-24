package ru.totowka.voicerecorder.helper

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import ru.totowka.voicerecorder.MainActivity
import ru.totowka.voicerecorder.R
import ru.totowka.voicerecorder.model.RecorderState
import ru.totowka.voicerecorder.service.RecorderService

class RecorderNotificationHelper(private val context: Context) {

    private val notificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private val remoteViews by lazy {
        RemoteViews(context.packageName, R.layout.recorder_notification).apply {
            setTextViewText(R.id.description, "Waiting for user...")
            setOnClickPendingIntent(R.id.root, intentOpenActivity)
            setOnClickPendingIntent(R.id.controller, getIntentStartRecording())
        }
    }

    private val notificationBuilder: NotificationCompat.Builder by lazy {
        NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_recorder)
            .setCustomContentView(remoteViews)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
    }

    private val intentOpenActivity by lazy {
        PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun getIntentStartRecording() : PendingIntent {
        val startRecording = Intent(context, RecorderService::class.java)
        startRecording.action = INTENT_START_RECORDING
        return PendingIntent.getService(context, 0, startRecording, PendingIntent.FLAG_ONE_SHOT)
    }

    private fun getIntentStopRecording() : PendingIntent {
        val stopRecording = Intent(context, RecorderService::class.java)
        stopRecording.action = INTENT_STOP_RECORDING
        return PendingIntent.getService(context, 0, stopRecording, PendingIntent.FLAG_ONE_SHOT)
    }

    fun getNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(createChannel())
        }
        return notificationBuilder.build()
    }

    fun updateNotification(state: RecorderState) {
        when(state) {
            RecorderState.START -> {
                remoteViews.apply {
                    setTextViewText(R.id.description, TEXT_START_RECORDING)
                    setImageViewResource(R.id.controller, R.drawable.ic_stop)
                    setOnClickPendingIntent(R.id.controller, getIntentStopRecording())
                }
                notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
            }
            RecorderState.STOP -> {
                remoteViews.apply {
                    setTextViewText(R.id.description, TEXT_STOP_RECORDING)
                    setImageViewResource(R.id.controller, R.drawable.ic_start)
                    setOnClickPendingIntent(R.id.controller, getIntentStartRecording())
                }
                notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
            }
            else -> return
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() =
        NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = CHANNEL_DESCRIPTION
            setSound(null, null)
        }

    companion object {
        private const val CHANNEL_ID = "RecorderID"
        private const val CHANNEL_NAME = "RecorderName"
        private const val CHANNEL_DESCRIPTION = "Recorder"
        const val NOTIFICATION_ID = 99
        const val INTENT_START_RECORDING = "INTENT_START_RECORDING"
        const val INTENT_STOP_RECORDING = "INTENT_STOP_RECORDING"
        const val TEXT_STOP_RECORDING = "Recording saved!"
        const val TEXT_START_RECORDING = "Recording in progress..."
    }
}