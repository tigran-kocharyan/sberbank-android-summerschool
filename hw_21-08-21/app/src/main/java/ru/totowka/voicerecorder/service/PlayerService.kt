package ru.totowka.voicerecorder.service

import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import ru.totowka.voicerecorder.helper.PlayerNotificationHelper
import ru.totowka.voicerecorder.model.PlayerState
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.*


class PlayerService: Service() {
    private var binder = PlayerBinder()
    private var playerState: PlayerState = PlayerState.INITIALIZED
    private lateinit var record : File
    private lateinit var fileName : String
    private lateinit var fileInputStream: FileInputStream
    private var player : MediaPlayer? = null
    private val helper by lazy { PlayerNotificationHelper(this) }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.run {
            when (action) {
                INTENT_START_PLAYING -> startPlaying()
                INTENT_PAUSE_PLAYING -> pausePlaying()
                INTENT_STOP_PLAYING -> stopPlaying()
                INTENT_INIT -> init(intent)
                else -> return START_NOT_STICKY
            }
        }
        return START_NOT_STICKY
    }

    private fun init(intent: Intent) {
        if(player != null) {
            stopPlaying()
        }
        fileName = intent.getStringExtra(FILENAME).toString()
        record = File(fileName)
        fileInputStream = FileInputStream(record)
        player = MediaPlayer()
        player!!.setDataSource(fileInputStream.fd)
        player!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
        player!!.prepare()
        player!!.setOnCompletionListener { stopPlaying() }
        startPlaying()
        startForeground(PlayerNotificationHelper.NOTIFICATION_ID, helper.getNotification())
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    private fun startPlaying() {
        playerState = PlayerState.START
        try {
            player!!.start()
        } catch (e: IOException) {
            Log.e(TAG, e.message?: "error")
        }
        broadcastUpdate()
    }

    private fun pausePlaying() {
        playerState = PlayerState.PAUSE
        player!!.pause()
        broadcastUpdate()
    }

    private fun stopPlaying() {
        playerState = PlayerState.STOP
        player!!.stop()
        stopService()
    }

    private fun broadcastUpdate() {
        helper.updateNotification(playerState)
    }

    fun endService() {
        stopService()
    }

    private fun stopService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true)
        } else {
            stopSelf()
        }
    }

    inner class PlayerBinder : Binder() {
        fun getService() = this@PlayerService
    }

    companion object {
        private const val TAG = "RecordPlayerService"
        private const val FILENAME = "FILENAME"
        const val INTENT_START_PLAYING = "INTENT_START_PLAYING"
        const val INTENT_PAUSE_PLAYING = "INTENT_PAUSE_PLAYING"
        const val INTENT_STOP_PLAYING = "INTENT_STOP_PLAYING"
        const val INTENT_INIT = "INTENT_INIT"
    }
}