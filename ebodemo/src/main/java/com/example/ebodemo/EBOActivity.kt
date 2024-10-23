package com.example.ebodemo

import android.app.Activity
import android.opengl.GLSurfaceView
import android.os.Bundle

class EBOActivity : Activity() {

    private var mGLSurfaceView: GLSurfaceView? = null
    private var mEBORenderer: EBORenderer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mGLSurfaceView = GLSurfaceView(this)
        mGLSurfaceView!!.setEGLContextClientVersion(3)
        mEBORenderer = EBORenderer(this)
        mGLSurfaceView!!.setRenderer(mEBORenderer)
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

    override fun onDestroy() {
        super.onDestroy()
        mEBORenderer?.release()
    }
}