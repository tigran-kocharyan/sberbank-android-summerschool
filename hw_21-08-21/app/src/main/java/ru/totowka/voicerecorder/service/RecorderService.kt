package ru.totowka.voicerecorder.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaRecorder
import android.os.*
import ru.totowka.voicerecorder.helper.RecorderNotificationHelper
import ru.totowka.voicerecorder.model.RecorderState
import java.util.*


class RecorderService : Service() {
    private val binder = RecorderBinder()
    private var recorderState: RecorderState = RecorderState.INITIALIZED
    private val basePath by lazy { (this as Context).externalCacheDir?.absolutePath }
    private val helper by lazy { RecorderNotificationHelper(this) }
    private var recorder: MediaRecorder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.run {
            when (action) {
                INTENT_START_RECORDING -> startRecording()
                INTENT_STOP_RECORDING -> stopRecording()
                INTENT_INIT -> startForeground(RecorderNotificationHelper.NOTIFICATION_ID, helper.getNotification())
                else -> return START_NOT_STICKY
            }
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    private fun startRecording() {
        recorderState = RecorderState.START
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(basePath + "/" + Calendar.getInstance().time.toString() + ".3gp")
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        }
        recorder!!.prepare()
        recorder!!.start()
        broadcastUpdate()
    }

    private fun stopRecording() {
        recorderState = RecorderState.STOP
        if (recorder != null) {
            recorder!!.release();
            recorder = null;
        }
        broadcastUpdate()
    }

    fun endService() {
        broadcastUpdate()
        stopService()
    }

    private fun broadcastUpdate() {
        helper.updateNotification(recorderState)
    }

    private fun stopService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true)
        } else {
            stopSelf()
        }
    }

    inner class RecorderBinder : Binder() {
        fun getService() = this@RecorderService
    }

    companion object {
        private const val TAG = "RecorderService"
        const val RECORDER_SERVICE_COMMAND = "RECORDER"
        const val INTENT_START_RECORDING = "INTENT_START_RECORDING"
        const val INTENT_STOP_RECORDING = "INTENT_STOP_RECORDING"
        const val INTENT_INIT = "INTENT_INIT"
    }
}