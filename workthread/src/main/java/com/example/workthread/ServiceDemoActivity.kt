package com.example.workthread

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button

class ServiceDemoActivity : Activity() {
    private val TAG = "ServiceDemoActivity"
    private var mIntent: Intent? = null
    private var mIsBinded = false

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        setContentView(R.layout.main)
        findViewById<Button>(R.id.startButton).setOnClickListener {
            startService()
        }
        findViewById<Button>(R.id.stopButton).setOnClickListener {
            if (mIntent != null) {
                Log.d(TAG, "stopService")
                stopService(mIntent)
            }
        }
        findViewById<Button>(R.id.bindButton).setOnClickListener {
            Log.d(TAG, "bindService")
            val intent = Intent(this, MyService::class.java)
            intent.action = "send action"
            intent.putExtra("data", "999999")
            bindService(intent, mMyServiceConnection, BIND_AUTO_CREATE)
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

    fun startService() {
        Log.d(TAG, "startService")
        mIntent = Intent()
        mIntent?.`package` = "com.example.workthread"
        mIntent?.action = "com.custom.myservice"
        mIntent?.putExtra("data", "123456")
        startService(mIntent)
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
        if (mIntent != null) {
            Log.d(TAG, "stopService")
            stopService(mIntent)
            mIntent = null
        }
    }

    private var mMyServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(TAG, "onServiceConnected")
            Log.d(TAG, "service:$service")
            var myService = (service as? MyService.MyLocalBinder)?.getService()
            Log.d(TAG, "myService:$myService")

            myService?.doSomeThings()?.run {
                Log.d(TAG, "name: ${getStringExtra("name")}, year:${getIntExtra("year", -1)}")
            }
            mIsBinded = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "onServiceDisconnected")
            mIsBinded = false
        }

    }
}