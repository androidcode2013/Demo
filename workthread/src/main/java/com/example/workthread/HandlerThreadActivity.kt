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
    private var mUiHandler: Handler? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        var workThread = HandlerThread("workThread")
        workThread.start()
        //workHandler在主线程里创建的
        var workHandler = object : Handler(workThread.looper) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                //运行在子线程workThread中，可以做耗时操作
                Log.d("tag", "workHandler#arg1: ${msg.arg1}, thread: ${Thread.currentThread()}")
                var msg = Message()
                msg.what = 0
                msg.arg1 = 666
                mUiHandler?.sendMessage(msg)
            }
        }

        mUiHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                Log.d("tag", "mUiHandler#arg1: ${msg.arg1}, thread: ${Thread.currentThread()}")
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
}