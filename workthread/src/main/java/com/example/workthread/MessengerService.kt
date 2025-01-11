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
    private val CLIENT_UPDATE = 999
    override fun onBind(intent: Intent?): IBinder? {
        Log.d(TAG,"onBind:${intent?.getStringExtra("data")}")
        return Messenger(MyHandler()).binder
    }

    inner class MyHandler : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            Log.d(TAG,"handleMessage")
            when (msg.what) {
                MESSENGER_UPDATE -> {
                    Log.d(TAG, "service:${msg.arg1},${msg.arg2}")
                    msg.replyTo.send(Message().apply {
                        what = CLIENT_UPDATE
                        arg1 = 2026
                        arg2 = 12
                    })
                }
            }
        }
    }
}