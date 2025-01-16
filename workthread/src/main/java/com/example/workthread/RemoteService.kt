package com.example.workthread

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Parcel
import android.util.Log

class RemoteService : Service() {
    private val TAG = "RemoteService"
//    private val channelId = "1"


    private var binder = object : IRemoteService.Stub() {
        override fun print(date: String?) {
            Log.d(TAG, "date:$date")
        }

        override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {
            Log.d(TAG, "transact:${data.readString()}")
            return true
        }
    }

    override fun onCreate() {
        Log.d(TAG, "onCreate")
        super.onCreate()
//        showServiceNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onRebind(intent: Intent?) {
        Log.d(TAG, "onRebind")
        super.onRebind(intent)
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d(TAG, "onBind")
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "onUnbind")
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }

//    @SuppressLint("ForegroundServiceType")
//    fun showServiceNotification() {
//        Log.d(TAG,"showServiceNotification")
//        //创建channel
//        val name = "my channel."
//        val importance = NotificationManager.IMPORTANCE_DEFAULT
//        val mChannel = NotificationChannel(channelId, name, importance)
//        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.createNotificationChannel(mChannel)
//
//        //创建一个PendingIntent，当点击通知时，可以跳到指定Activity
//        val pendingIntent: PendingIntent = Intent(this, MainActivity::class.java).let {
//            PendingIntent.getActivity(this, 0, it, PendingIntent.FLAG_MUTABLE)
//        }
//
//        //创建通知对象
//        val notification: Notification = Notification.Builder(this, channelId)
//            .setContentTitle("标题")
//            .setContentText("内容")
//            .setSmallIcon(R.drawable.ic_launcher_background)
//            .setContentIntent(pendingIntent)
//            .build()
//
//        //注意这个id不能是0，否则会报异常
//        startForeground(1, notification)
//    }
}