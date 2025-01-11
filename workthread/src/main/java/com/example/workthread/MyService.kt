package com.example.workthread

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

class MyService : Service() {
    val TAG = "MyService"
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var action = intent?.action
        var data = intent?.getStringExtra("data")
        Log.d(
            TAG,
            "onStartCommand#action: $action , data : $data , flags : $flags, startId: $startId"
        )
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        var action = intent?.action
        var data = intent?.getStringExtra("data")
        Log.d(TAG, "onBind#action: $action , data : $data")

        return null
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "onUnbind")
        return super.onUnbind(intent)
    }

    override fun onRebind(intent: Intent?) {
        Log.d(TAG, "onRebind")
        super.onRebind(intent)
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        stopSelf()
        super.onDestroy()
    }
}