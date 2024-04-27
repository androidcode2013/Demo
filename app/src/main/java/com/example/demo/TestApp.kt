package com.example.demo

import android.app.Application
import android.content.Context
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher

class TestApp: Application() {
    var mRefWatcher: RefWatcher? = null
    companion object{
        fun getRefWatcher(context: Context): RefWatcher? {
            val application: TestApp = context.applicationContext as TestApp
            return application.mRefWatcher
        }

    }
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }
    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) return
        mRefWatcher = LeakCanary.install(this)
//        leakCanaryConfig()
    }
//
//    private fun leakCanaryConfig() {
//        //App 处于前台时检测保留对象的阈值，默认是 5
//        LeakCanary.config = LeakCanary.config.copy(retainedVisibleThreshold = 3)
//        //自定义要检测的保留对象类型，默认监测 Activity，Fragment，FragmentViews 和 ViewModels
//        AppWatcher.config= AppWatcher.config.copy(watchFragmentViews = false)
//        //隐藏泄漏显示活动启动器图标，默认为 true
//        LeakCanary.showLeakDisplayActivityLauncherIcon(false)
//    }

}

