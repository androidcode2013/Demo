package com.example.roundrect.renderers

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES30
import com.example.roundrect.Constant
import com.example.roundrect.R
import com.example.roundrect.program.RoundRectShaderProgram
import com.opengllib.data.VertexArray
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class RectRenderer(context: Context) : BaseRenderer(context) {
    private var mVertices = floatArrayOf(

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
    private var mVertexArray: VertexArray? = null

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        mVertexArray = VertexArray(mVertices)
        mVertexArray?.setVertexAttribPointer(
            0,
            mShaderProgram?.getPositionAttributeLocation()!!,
            Constant.POSITION_COMPONENT_COUNT,
            0
        )
    }


    override fun onDrawFrame(gl: GL10?) {
        super.onDrawFrame(gl)
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES30.glUniform4f(mShaderProgram!!.getColorUniformLocation(), 0.0f, 1.0f, 0.0f, 0.5f)
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_FAN, 0, 6)
        GLES30.glUniform4f(mShaderProgram!!.getColorUniformLocation(), 1.0f, 0.0f, 0.0f, 0.5f)
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_FAN, 7, 6)
        GLES20.glDisable(GLES20.GL_BLEND)

    }
}