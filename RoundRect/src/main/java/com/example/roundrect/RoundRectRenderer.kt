package com.example.roundrect

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES30
import android.opengl.GLSurfaceView.Renderer
import com.example.roundrect.program.RoundRectShaderProgram
import com.opengllib.data.VertexArray
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class RoundRectRenderer(context: Context) : Renderer {
    private var mVerticesBig = floatArrayOf(

        -0.4f, -0.46f,
        0.4f, 0.46f,
        -0.4f, 0.46f,

        -0.4f, -0.46f,
        0.4f, -0.46f,
        0.4f, 0.46f,


        -0.5f, -0.5f,
        0.5f, 0.5f,
        -0.5f, 0.5f,
        -0.5f, -0.5f,
        0.5f, -0.5f,
        0.5f, 0.5f

    )
    private var POSITION_COMPONENT_COUNT = 2

    private var mShaderProgram: RoundRectShaderProgram? = null
    private var mContext = context
    private var mVertexArrayBig: VertexArray? = null

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES30.glClearColor(0.6f, 0.7f, 0.8f, 1.0f)
        mVertexArrayBig = VertexArray(mVerticesBig)

        mShaderProgram = RoundRectShaderProgram(
            mContext,
            R.raw.roundrect_vertex_shader,
            R.raw.roundrect_fragment_shader
        )
        mShaderProgram?.useProgram()
        mVertexArrayBig?.setVertexAttribPointer(
            0,
            mShaderProgram?.getPositionAttributeLocation()!!,
            POSITION_COMPONENT_COUNT,
            0
        )
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES30.glUniform4f(mShaderProgram!!.getColorUniformLocation(), 0.0f, 1.0f, 0.0f, 0.5f)
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_FAN, 0, 6)
        GLES30.glUniform4f(mShaderProgram!!.getColorUniformLocation(), 1.0f, 0.0f, 0.0f, 0.5f)
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_FAN, 7, 6)
        GLES20.glDisable(GLES20.GL_BLEND)
    }
}