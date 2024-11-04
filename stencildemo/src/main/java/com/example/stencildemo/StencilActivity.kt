package com.example.stencildemo

import android.app.Activity
import android.opengl.GLSurfaceView
import android.os.Bundle
import com.example.stencildemo.renderer.StencilTestRenderer

class StencilActivity : Activity() {

    private var mGLSurfaceView: GLSurfaceView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mGLSurfaceView = GLSurfaceView(this)
        mGLSurfaceView!!.setEGLContextClientVersion(3)
        mGLSurfaceView!!.setRenderer(StencilTestRenderer(this))
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

    override fun onDestroy() {
        super.onDestroy()
    }
}