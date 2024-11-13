package com.example.skyboxdemo

import android.annotation.SuppressLint
import android.app.Activity
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.MotionEvent

class SkyBoxActivity : Activity() {
    private var mGLSurfaceView: GLSurfaceView? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mGLSurfaceView= GLSurfaceView(this)
        var skyBoxRenderer = SkyBoxRenderer(this)
        mGLSurfaceView!!.setEGLContextClientVersion(3)
        mGLSurfaceView!!.setRenderer(skyBoxRenderer)
        var previousX = 0f
        var previousY = 0f
        mGLSurfaceView!!.setOnTouchListener { v, event ->
            if (event != null) {
                if (event.action == MotionEvent.ACTION_DOWN) {
                    previousX = event.x
                    previousY = event.y
                } else if (event.action == MotionEvent.ACTION_MOVE) {
                    val deltaX = event.x - previousX
                    val deltaY = event.y - previousY
                    previousX = event.x
                    previousY = event.y
                    mGLSurfaceView!!.queueEvent {
                        skyBoxRenderer.handleTouchDrag(deltaX, deltaY)
                    }
                }
                true
            } else false
        }
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