package ru.totowka.voicerecorder

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.totowka.voicerecorder.service.PlayerService
import ru.totowka.voicerecorder.service.RecorderService


class MainActivity : AppCompatActivity() {

    // Recycler
    private val recycler: RecyclerView by lazy { findViewById(R.id.recyclerView) }
    private val refresher: SwipeRefreshLayout by lazy { findViewById(R.id.swipeRefreshLayout) }
    private lateinit var adapter: RecordingsAdapter
    private val listener = object : ItemCallback {
        override fun onClick(filename: String) {
            ContextCompat.startForegroundService(
                this@MainActivity,
                getPlayerServiceIntent("${basePath?.absolutePath}/${filename}").setAction(INTENT_INIT)
            )
        }
    }

    // Recorder
    private lateinit var recorderService: RecorderService
    private val recorderConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            recorderService = (service as RecorderService.RecorderBinder).getService()
            isRecorderBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isRecorderBound = false
        }
    }
    private var isRecorderBound = false

    // Player
    private lateinit var playerService: PlayerService
    private val playerConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            playerService = (service as PlayerService.PlayerBinder).getService()
            isPlayerBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isPlayerBound = false
        }
    }
    private var isPlayerBound = false

    // Utils
    private var audioRecordingPermissionGranted = false
    private val basePath by lazy { (this as Context).externalCacheDir }
    private val permissions = arrayOf(Manifest.permission.RECORD_AUDIO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)

        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = basePath?.let { RecordingsAdapter(it.listFiles(), listener) }!!
        recycler.adapter = adapter
        refresher.setOnRefreshListener {
            adapter.updaterecordings(basePath!!.listFiles())
            refresher.isRefreshing = false
        }
    }

    override fun onStart() {
        super.onStart()
        bindService(getRecorderServiceIntent(), recorderConnection, Context.BIND_AUTO_CREATE)
        sendCommandToForegroundService()
    }

    override fun onStop() {
        super.onStop()
        recorderService.endService()
        playerService.endService()
        if (isRecorderBound) unbindService(recorderConnection)
        if (isPlayerBound) unbindService(playerConnection)
        isRecorderBound = false
        isPlayerBound = false
    }

    private fun getRecorderServiceIntent() =
        Intent(this, RecorderService::class.java)

    private fun getPlayerServiceIntent(filename: String) =
        Intent(this, PlayerService::class.java).apply {
            putExtra(FILENAME, filename)
        }

    private fun sendCommandToForegroundService() {
        ContextCompat.startForegroundService(
            this,
            getRecorderServiceIntent().setAction(INTENT_INIT)
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_RECORD_AUDIO_PERMISSION -> audioRecordingPermissionGranted =
                grantResults[0] == PackageManager.PERMISSION_GRANTED
        }
        if (!audioRecordingPermissionGranted) {
            finish()
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val FILENAME = "FILENAME"
        private const val INTENT_INIT = "INTENT_INIT"
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
    }

    interface ItemCallback {
        fun onClick(filename: String)
    }
}