package com.example.renderyuv

import android.app.Activity
import android.opengl.GLSurfaceView
import android.os.Bundle
import com.example.renderyuv.util.getAssertData


class RenderYUVActivity : Activity() {
    private var mGLSurfaceView: GLSurfaceView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mGLSurfaceView = GLSurfaceView(this)
        mGLSurfaceView?.setEGLContextClientVersion(3)
        var yuvRenderer = YUVRenderer(this)
        mGLSurfaceView?.setRenderer(yuvRenderer)
        setContentView(mGLSurfaceView)
        yuvRenderer.setYuvData(
            getAssertData(this, "image_640_480.yuv")!!,
            640, 480
        )
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