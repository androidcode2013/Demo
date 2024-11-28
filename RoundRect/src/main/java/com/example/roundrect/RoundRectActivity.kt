package com.example.roundrect

import android.app.Activity
import android.opengl.GLSurfaceView
import android.os.Bundle
import com.example.roundrect.renderers.CirclarRectRenderer
import com.example.roundrect.renderers.CirclarRenderer
import com.example.roundrect.renderers.RectRenderer

class RoundRectActivity : Activity() {
    private var mGLSurfaceView: GLSurfaceView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mGLSurfaceView = GLSurfaceView(this)
        mGLSurfaceView?.setEGLContextClientVersion(3)
        mGLSurfaceView?.setRenderer(CirclarRectRenderer(this))
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