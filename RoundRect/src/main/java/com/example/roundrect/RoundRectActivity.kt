package com.example.roundrect

import android.app.Activity
import android.opengl.GLSurfaceView
import android.os.Bundle

class RoundRectActivity : Activity() {
    private var mGLSurfaceView: GLSurfaceView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mGLSurfaceView = GLSurfaceView(this)
        mGLSurfaceView?.setEGLContextClientVersion(3)
        mGLSurfaceView?.setRenderer(RoundRectRenderer(this))
        setContentView(mGLSurfaceView)
    }

    override fun onResume() {
        super.onResume()
        mGLSurfaceView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mGLSurfaceView?.onPause()
    }
}