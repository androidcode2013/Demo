package com.example.vaodemo

import android.app.Activity
import android.opengl.GLSurfaceView
import android.os.Bundle

class VAOActivity : Activity() {
    private var mGlSurfaceView: GLSurfaceView? = null
    private var mVAORenderer: VAORenderer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mGlSurfaceView = GLSurfaceView(this)
        mVAORenderer = VAORenderer(this)
        /*VAO是OpenGL ES 3.0之后才推出的新特性，
        所以在使用VAO前需要确定OpenGL ES的版本是否是3.0之后的版本。*/
        mGlSurfaceView?.setEGLContextClientVersion(3)
        mGlSurfaceView?.setRenderer(mVAORenderer)
        setContentView(mGlSurfaceView)
    }

    override fun onResume() {
        super.onResume()
        mGlSurfaceView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mGlSurfaceView?.onPause()
        mVAORenderer?.unbindVAO()
    }
}