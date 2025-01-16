package com.example.workthread

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button

class ClientActivity : Activity() {
    private val TAG = "ClientActivity"
    private var mIsBinded = false
    private var mIntent: Intent? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        setContentView(R.layout.main)
        findViewById<Button>(R.id.bindButton).setOnClickListener {
            Log.d(TAG, "bindService")
            mIntent = Intent(
                this,
                RemoteService::class.java
            ).run {
                putExtra("data", "123456")
            }
            bindService(
                mIntent!!, mMyServiceConnection, BIND_AUTO_CREATE
            )
        }
        findViewById<Button>(R.id.unbindButton).setOnClickListener {
            Log.d(TAG, "unbindService")
            if (mIsBinded) {
                unbindService(mMyServiceConnection)
            }
        }
        findViewById<Button>(R.id.startButton).setOnClickListener {
            Log.d(TAG, "startService")
            mIntent = Intent(
                this,
                RemoteService::class.java
            ).run {
                putExtra("data", "123456")
            }
            startService(mIntent)
        }
        findViewById<Button>(R.id.stopButton).setOnClickListener {
            if (mIntent != null) {
                Log.d(TAG, "stopService")
                stopService(mIntent)
            }
        }
    }

    private var mMyServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(TAG, "onServiceConnected")
            var iRemoteService = IRemoteService.Stub.asInterface(service)
            iRemoteService?.print("20250112")
            mIsBinded = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "onServiceDisconnected")
            mIsBinded = false
        }

    }
}