package com.example.workthread

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.util.Log
import android.widget.Button

class MessengerActivity : Activity() {
    private val TAG = "MessengerActivity"
    private var mMessenger: Messenger? = null
    private var mIsBinded = false
    private val MESSENGER_UPDATE = 666
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        findViewById<Button>(R.id.bindButton).setOnClickListener {
            Log.d(TAG, "bindService")
            bindService(Intent(this, MessengerService::class.java).run {
                putExtra("data", "123456")
            }, mMyServiceConnection, BIND_AUTO_CREATE)
        }
        findViewById<Button>(R.id.unbindButton).setOnClickListener {
            Log.d(TAG, "unbindService")
            mMyServiceConnection?.let {
                if (mIsBinded) {
                    unbindService(it)
                }
            }
        }
    }

    private var mMyServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mMessenger = Messenger(service)
            mMessenger?.send(Message.obtain().also {
                it.what = MESSENGER_UPDATE
                it.arg1 = 2025
                it.arg2 = 1
            })
            mIsBinded = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mMessenger = null
            mIsBinded = false
        }

    }
}