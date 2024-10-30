package com.example.fbodemo

import android.app.Activity
import android.opengl.GLSurfaceView
import android.os.Bundle
import com.example.fbodemo.renderer.FBORenderer

class RBOActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var glSurfaceView = GLSurfaceView(this)
        glSurfaceView.setEGLContextClientVersion(3)
        glSurfaceView.setRenderer(FBORenderer(this))
        setContentView(glSurfaceView)
    }
}