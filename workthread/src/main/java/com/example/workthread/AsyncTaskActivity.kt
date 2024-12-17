package com.example.workthread

import android.app.Activity
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import java.util.concurrent.Executor
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


class AsyncTaskActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        var myAsyncTask = MyAsyncTask()
        //默认使用线程池THREAD_POOL_EXECUTOR
//        myAsyncTask.execute()
        //创建自定义线程池
        val executor: Executor = ThreadPoolExecutor(
            10, 50, 10,
            TimeUnit.SECONDS, LinkedBlockingDeque<Runnable>(100)
        )
        myAsyncTask.executeOnExecutor(executor)
    }

    inner class MyAsyncTask : AsyncTask<String, Int, Long>() {

        override fun onPostExecute(result: Long?) {
            super.onPostExecute(result)
            //运行在主线程
            Log.d("tag", "result:" + result)
            Log.d("tag", "onPostExecute#thread: ${Thread.currentThread()}")
            this@AsyncTaskActivity.findViewById<TextView>(R.id.showtext).text = result.toString()
        }

        override fun doInBackground(vararg params: String?): Long {
            //运行在子线程
            Log.d("tag", "doInBackground#thread: ${Thread.currentThread()}")
            return 1000
        }
    }
}