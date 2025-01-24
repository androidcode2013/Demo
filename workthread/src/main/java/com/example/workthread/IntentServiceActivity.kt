package com.example.workthread

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button

class IntentServiceActivity : Activity() {

    private val TAG = "IntentServiceActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        findViewById<Button>(R.id.startButton).setOnClickListener {
            Log.d(TAG, "startButton")
            startService(Intent(this@IntentServiceActivity, MyIntentService::class.java).apply {
                putExtras(Bundle().apply {
                    putExtra("type", "0")
                    putExtra("date", "20250123")
                    putExtra("language", "C#")
                })
            })
            Log.d(TAG, "startService")
            startService(Intent(this@IntentServiceActivity, MyIntentService::class.java).apply {
                putExtras(Bundle().apply {
                    putExtra("type", "1")
                    putExtra("date", "20250124")
                    putExtra("language", "Kotlin")
                })
            })
        }

    }
}