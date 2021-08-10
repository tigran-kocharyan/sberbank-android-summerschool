package ru.totowka.broadcastreceiver

import android.Manifest
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Telephony
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ru.totowka.broadcastreceiver.smscode.SmsCodeListener
import ru.totowka.broadcastreceiver.smscode.SmsCodeReceiver


class MainActivity : AppCompatActivity(), SmsCodeListener {
    companion object {
        const val MY_PERMISSIONS_REQUEST_SMS = 1337
        const val CODE_EXAMPLE = "8789789"
        const val PERMISSIONS_GRANTED = "Granted!"
        const val PERMISSIONS_NOT_GRANTED = "Denied!"
        const val SUCCESS_MESSAGE = "Successfully got the code!"
        const val SEND_CODE = "Pretend SMS to be sent"
        const val RIGHT_CODE = "Right code!"
        const val WRONG_CODE = "Wrong!"
    }

    private var smsCodeReceiver: SmsCodeReceiver? = null
    private lateinit var send: Button
    private lateinit var approve: Button
    private lateinit var code: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        checkPermissions()
        setListeners()
    }

    private fun setListeners() {
        send.setOnClickListener {
            send.isEnabled = false
            SEND_CODE.makeToast()
            approve.isEnabled = true
        }
        approve.setOnClickListener {
            if (code.text.toString().trim() == CODE_EXAMPLE) {
                RIGHT_CODE.makeToast()
                approve.isEnabled = false
            } else {
                WRONG_CODE.makeToast()
            }
        }
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_SMS
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECEIVE_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_SMS,
                    Manifest.permission.RECEIVE_SMS,
                ),
                MY_PERMISSIONS_REQUEST_SMS
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (checkPermissionGranted(requestCode, permissions, grantResults)) {
            Toast.makeText(this, PERMISSIONS_GRANTED, Toast.LENGTH_SHORT).show()
        } else {
            send.isEnabled = false
            approve.isEnabled = false
            PERMISSIONS_NOT_GRANTED.makeToast()
        }
    }

    private fun checkPermissionGranted(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ): Boolean {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_SMS -> {
                return (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            }
        }
        return false
    }

    private fun init() {
        send = findViewById(R.id.sendcode)
        approve = findViewById(R.id.approve)
        code = findViewById(R.id.code)
        approve.isEnabled = false
    }

    override fun onStart() {
        super.onStart()
        setupBroadcastReceiver()
    }

    override fun onStop() {
        super.onStop()
        destroyBroadCastReceiver()
    }

    private fun setupBroadcastReceiver() {
        val filter = IntentFilter().apply {
            addAction(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
        }
        smsCodeReceiver = SmsCodeReceiver(this)
        registerReceiver(smsCodeReceiver, filter)
    }

    private fun destroyBroadCastReceiver() {
        unregisterReceiver(smsCodeReceiver)
        smsCodeReceiver = null
    }


    override fun onCodeReceived(code: String) {
        SUCCESS_MESSAGE.makeToast()
        this.code.setText(code)
    }

    override fun onWrongCodeReceived(exception: RuntimeException) {
        exception.message?.makeToast()
    }

    fun String.makeToast() {
        Toast.makeText(this@MainActivity, this, Toast.LENGTH_SHORT).show()
    }
}