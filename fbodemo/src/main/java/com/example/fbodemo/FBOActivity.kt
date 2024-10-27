package com.example.fbodemo

import android.app.Activity
import android.opengl.GLSurfaceView
import android.os.Bundle
import com.example.fbodemo.renderer.FBORenderer
import com.example.fbodemo.renderer.TextureRenderer

class FBOActivity : Activity() {

    private var mFBOGLSurfaceView: GLSurfaceView? = null
    private var mTextureGLSurfaceView: GLSurfaceView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)
        mFBOGLSurfaceView = findViewById(R.id.fbo)
        mFBOGLSurfaceView!!.setEGLContextClientVersion(3)
        mFBOGLSurfaceView!!.setRenderer(FBORenderer(this))
        mTextureGLSurfaceView = findViewById(R.id.texture)
        mTextureGLSurfaceView!!.setEGLContextClientVersion(3)
        mTextureGLSurfaceView!!.setRenderer(TextureRenderer(this))
    }

    override fun onResume() {
        super.onResume()
        mFBOGLSurfaceView!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        mFBOGLSurfaceView!!.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}