package com.example.fbodemo

import android.app.Activity
import android.opengl.GLSurfaceView
import android.os.Bundle
import com.example.fbodemo.renderer.TextureRenderer

class FBOActivity : Activity() {

    private var mTextureGLSurfaceView: GLSurfaceView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)
        mTextureGLSurfaceView = findViewById(R.id.texture)
        mTextureGLSurfaceView!!.setEGLContextClientVersion(3)
        mTextureGLSurfaceView!!.setRenderer(TextureRenderer(this))
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}