package com.example.vbodemo

import android.app.Activity
import android.opengl.GLSurfaceView
import android.os.Bundle

class VBOActivity : Activity() {
    private var mGlSurfaceView: GLSurfaceView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mGlSurfaceView = GLSurfaceView(this)
        mGlSurfaceView?.setEGLContextClientVersion(2)
        mGlSurfaceView?.setRenderer(VBORenderer(this))
        setContentView(mGlSurfaceView)
    }
}