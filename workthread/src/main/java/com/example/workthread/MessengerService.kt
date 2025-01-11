package com.example.workthread

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.util.Log

class MessengerService : Service() {
    private val TAG = "MessengerService"
    private val MESSENGER_UPDATE = 666
    override fun onBind(intent: Intent?): IBinder? {
        Log.d(TAG,"onBind:${intent?.getStringExtra("data")}")
        return Messenger(MyHandler()).binder
    }

    inner class MyHandler : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            Log.d(TAG,"handleMessage")
            when (msg.what) {
                MESSENGER_UPDATE -> Log.d(TAG, "data:${msg.arg1},${msg.arg2}")
            }
        }
    }
}