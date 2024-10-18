package com.example.blenddemo

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES20.GL_BLEND
import android.opengl.GLES20.GL_ONE
import android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA
import android.opengl.GLES20.GL_ONE_MINUS_SRC_COLOR
import android.opengl.GLES20.GL_SRC_ALPHA
import android.opengl.GLES20.GL_SRC_COLOR
import android.opengl.GLES20.GL_TRIANGLES
import android.opengl.GLES20.GL_ZERO
import android.opengl.GLES20.glBlendFunc
import android.opengl.GLES20.glClearColor
import android.opengl.GLES20.glDrawArrays
import android.opengl.GLES20.glDrawElements
import android.opengl.GLES20.glEnable
import android.opengl.GLES20.glUniform4f
import android.opengl.GLES20.glViewport
import android.opengl.GLSurfaceView.Renderer
import android.util.Log
import com.example.blenddemo.programs.BlendShaderProgram
import com.opengllib.data.VertexArray
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class BlendRenderer(context: Context) : Renderer {
    private val TAG = "BlendRenderer"

    var mContext = context
    var mShaderProgram: BlendShaderProgram? = null
    var mVertexArray: VertexArray? = null

    private val POSITION_COMPONENT_COUNT = 2

    private var rectangleVertices = floatArrayOf(
        // Triangle 1
        -0.5f, -0.5f,
        0.5f, 0.5f,
        -0.5f, 0.5f,

        -0.5f, -0.5f,
        0.5f, -0.5f,
        0.5f, 0.5f,
        // Triangle 2
        0f, 0f,
        9f, 14f,
        0f, 14f,


        0f, 0f,
        9f, 0f,
        9f, 14f
    )


    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
        mVertexArray = VertexArray(rectangleVertices)
        mShaderProgram =
            BlendShaderProgram(mContext, R.raw.blend_vertex_shader, R.raw.blend_fragment_shader)
        mShaderProgram?.useProgram()
        mVertexArray?.setVertexAttribPointer(
            0,
            mShaderProgram!!.getPositionAttributeLocation(),
            POSITION_COMPONENT_COUNT, 0
        )
        mVertexArray?.setVertexAttribPointer(
            0,
            mShaderProgram!!.getColorUniformLocation(),
            POSITION_COMPONENT_COUNT, 0
        )
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glUniform4f(mShaderProgram!!.getColorUniformLocation(), 1.0f, 0.0f, 0.0f, 0.8f);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        glUniform4f(mShaderProgram!!.getColorUniformLocation(), 0.0f, 1.0f, 0.0f, 0.4f);
        glDrawArrays(GL_TRIANGLES, 7, 13);

        GLES20.glDisable(GL_BLEND)
    }
}