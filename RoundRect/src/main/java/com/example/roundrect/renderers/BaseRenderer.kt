package com.example.roundrect.renderers

import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView.Renderer
import com.example.roundrect.R
import com.example.roundrect.program.RoundRectShaderProgram
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

open class BaseRenderer(context: Context) : Renderer {
    var mShaderProgram: RoundRectShaderProgram? = null
    var mContext = context

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES30.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
        mShaderProgram = RoundRectShaderProgram(
            mContext,
            R.raw.roundrect_vertex_shader,
            R.raw.roundrect_fragment_shader
        )
        mShaderProgram?.useProgram()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)

    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)

    }
}