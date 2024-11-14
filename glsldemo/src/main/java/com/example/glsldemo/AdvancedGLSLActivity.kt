package com.example.glsldemo

import android.app.Activity
import android.opengl.GLSurfaceView
import android.os.Bundle

class AdvancedGLSLActivity : Activity() {
    private var mGLSurfaceView: GLSurfaceView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mGLSurfaceView = GLSurfaceView(this)
        mGLSurfaceView!!.setEGLContextClientVersion(3)
        mGLSurfaceView!!.setRenderer(AdvancedGLSLRenderer(this))
        setContentView(mGLSurfaceView)
    }

    override fun onResume() {
        super.onResume()
        mGLSurfaceView!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        mGLSurfaceView!!.onPause()
    }
}