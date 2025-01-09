package com.example.workthread

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.TextView

class HandlerThreadActivity : Activity() {
    private val TAG = "HandlerThreadActivity"
    private var mUiHandler: Handler? = null
    private var mWorkerThread: HandlerThread? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        mWorkerThread = HandlerThread("workThread")
        Log.d(TAG,"onCreate:"+mWorkerThread?.quitSafely())
        mWorkerThread!!.start()
        //workHandler在主线程里创建的
        Log.d(TAG,"looper:${mWorkerThread!!.looper}")
        var workHandler = object : Handler(mWorkerThread!!.looper) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                //运行在子线程workThread中，可以做耗时操作
                Log.d(TAG, "workHandler#arg1: ${msg.arg1}, thread: ${Thread.currentThread()}")
                var msg = Message()
                msg.what = 0
                msg.arg1 = 666
                mUiHandler?.sendMessage(msg)
            }
        }

        mUiHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                Log.d(TAG, "mUiHandler#arg1: ${msg.arg1}, thread: ${Thread.currentThread()}")
                when (msg.what) {
                    0 -> {
                        findViewById<TextView>(R.id.showtext).text = msg.arg1.toString()
                    }

                    1 -> "bbbb"
                    else -> "111111"
                }
            }
        }
        var msg = Message()
        msg.what = 0
        msg.arg1 = 888
        workHandler.sendMessage(msg)
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG,"onPause:"+mWorkerThread?.quitSafely())
    }
}