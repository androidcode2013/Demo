package com.example.workthread

import android.app.IntentService
import android.content.Intent
import android.util.Log

class MyIntentService(name: String = "MyIntentService") : IntentService(name) {
    private val TAG = "MyIntentService"

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
    }

    override fun onHandleIntent(intent: Intent?) {//子线程处理的事务的地方
        intent?.extras.run {
            var type = intent?.getStringExtra("type")
            var date = intent?.getStringExtra("date")
            var language = intent?.getStringExtra("language")
            when (type) {
                "0" -> {
                    Log.d(TAG, "type#0:$type,date:$date,language:$language")
                }

                "1" -> {
                    Log.d(TAG, "type#1:$type,date:$date,language:$language")
                }

                else -> {
                    Log.d(TAG, "onHandleIntent#get intent fail")
                }
            }
        }

    }
}