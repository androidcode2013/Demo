package com.example.roundrect.renderers

import android.content.Context
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class CirclarRectRenderer(context: Context):CirclarRenderer(context) {

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
    }

    override fun onDrawFrame(gl: GL10?) {
        super.onDrawFrame(gl)
    }
}