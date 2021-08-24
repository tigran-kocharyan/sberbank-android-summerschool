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
import ru.totowka.voicerecorder.model.PlayerState
import ru.totowka.voicerecorder.service.PlayerService

class PlayerNotificationHelper(private val context: Context) {

    private val notificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private val remoteViews by lazy {
        RemoteViews(context.packageName, R.layout.player_notification).apply {
            setTextViewText(R.id.description, "Waiting for user...")
            setOnClickPendingIntent(R.id.root, intentOpenActivity)
            setOnClickPendingIntent(R.id.controller, getIntentPausePlaying())
            setOnClickPendingIntent(R.id.stop, getIntentStopPlaying())
        }
    }

    private val notificationBuilder: NotificationCompat.Builder by lazy {
        NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_music)
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

    private fun getIntentStartPlaying(): PendingIntent {
        val startPlaying = Intent(context, PlayerService::class.java)
        startPlaying.action = INTENT_START_PLAYING
        return PendingIntent.getService(context, 0, startPlaying, PendingIntent.FLAG_ONE_SHOT)
    }

    private fun getIntentPausePlaying(): PendingIntent {
        val pausePlaying = Intent(context, PlayerService::class.java)
        pausePlaying.action = INTENT_PAUSE_PLAYING
        return PendingIntent.getService(context, 0, pausePlaying, PendingIntent.FLAG_ONE_SHOT)
    }

    private fun getIntentStopPlaying(): PendingIntent {
        val stopPlaying = Intent(context, PlayerService::class.java)
        stopPlaying.action = INTENT_STOP_PLAYING
        return PendingIntent.getService(context, 0, stopPlaying, PendingIntent.FLAG_ONE_SHOT)
    }

    fun getNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(createChannel())
        }
        return notificationBuilder.build()
    }

    fun updateNotification(state: PlayerState) {
        when (state) {
            PlayerState.START -> {
                remoteViews.apply {
                    setTextViewText(R.id.description, TEXT_START_PLAYING)
                    setImageViewResource(R.id.controller, R.drawable.ic_pause)
                    setOnClickPendingIntent(R.id.controller, getIntentPausePlaying())
                    setOnClickPendingIntent(R.id.stop, getIntentStopPlaying())
                }
                notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
            }
            PlayerState.PAUSE -> {
                remoteViews.apply {
                    setTextViewText(R.id.description, TEXT_PAUSE_PLAYING)
                    setImageViewResource(R.id.controller, R.drawable.ic_start)
                    setOnClickPendingIntent(R.id.stop, getIntentStopPlaying())
                    setOnClickPendingIntent(R.id.controller, getIntentStartPlaying())
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
        private const val CHANNEL_ID = "PlayerID"
        private const val CHANNEL_NAME = "PlayerName"
        private const val CHANNEL_DESCRIPTION = "Player"
        const val NOTIFICATION_ID = 100
        const val INTENT_START_PLAYING = "INTENT_START_PLAYING"
        const val INTENT_PAUSE_PLAYING = "INTENT_PAUSE_PLAYING"
        const val INTENT_STOP_PLAYING = "INTENT_STOP_PLAYING"
        const val TEXT_START_PLAYING = "Playing..."
        const val TEXT_PAUSE_PLAYING = "Pause..."
    }
}