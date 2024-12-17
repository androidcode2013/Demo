package com.example.workthread

import android.app.Activity
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class AsyncTaskActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        var myAsyncTask = MyAsyncTask()
        myAsyncTask.execute()

    }

    inner class MyAsyncTask : AsyncTask<String, Int, Long>() {

        override fun onPostExecute(result: Long?) {
            super.onPostExecute(result)
            //运行在主线程
            Log.d("tag", "whx#result:" + result)
            Log.d("tag", "whx#onPostExecute#thread: ${Thread.currentThread()}")
            this@AsyncTaskActivity.findViewById<TextView>(R.id.showtext).text = result.toString()
        }

        override fun doInBackground(vararg params: String?): Long {
            //运行在子线程
            Log.d("tag", "whx#doInBackground#thread: ${Thread.currentThread()}")
            return 1000
        }
    }
}