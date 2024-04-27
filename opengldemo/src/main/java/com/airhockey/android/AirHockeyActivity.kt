package com.airhockey.android

import android.app.Activity
import android.opengl.GLSurfaceView
import android.os.Bundle

class AirHockeyActivity: Activity() {
    var mGLSurfaceView: GLSurfaceView? = null
    var renderSet:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mGLSurfaceView = GLSurfaceView(this)
        mGLSurfaceView?.setEGLContextClientVersion(2)
        mGLSurfaceView?.setRenderer(AirHockeyRenderer(this))
        renderSet = true
        setContentView(mGLSurfaceView)
    }

    override fun onResume() {
        super.onResume()
        if (renderSet) mGLSurfaceView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        if(renderSet) mGLSurfaceView?.onPause()
    }
}