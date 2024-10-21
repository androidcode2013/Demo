package com.example.vbodemo

import android.content.Context
import android.opengl.GLES20.GL_COLOR_BUFFER_BIT
import android.opengl.GLES20.GL_TRIANGLES
import android.opengl.GLES20.glClear
import android.opengl.GLES20.glClearColor
import android.opengl.GLES20.glDrawArrays
import android.opengl.GLES20.glViewport
import android.opengl.GLSurfaceView.Renderer
import com.opengllib.data.VBOManager
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class VBORenderer(context: Context) : Renderer {

    private val TAG = "VBORenderer"
    private val POSITION_LOCATION_INDEX = 0//index位置对应着色器layout (location = 0)
    private val POSITION_COMPONENT_COUNT = 3

    private val mVertexPoints = floatArrayOf(
        0.0f, 0.5f, 0.0f,
        -0.5f, -0.5f, 0.0f,
        0.5f, -0.5f, 0.0f
    )

    private var mVboIds = IntArray(1)//顶点缓冲对象VBO id标识
    private var mContext = context
    private var mShaderProgram: VBOShaderProgram? = null

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        //设置背景颜色
        glClearColor(0.7f, 0.6f, 0.5f, 1.0f);
        mShaderProgram =
            VBOShaderProgram(mContext, R.raw.vbo_vertex_shader, R.raw.vbo_fragment_shader)
        mShaderProgram?.useProgram()
        var vboManager = VBOManager(mVertexPoints)
        vboManager.allocateBuffer()
        vboManager.bindVBO(mVboIds)
        //通过着色器中layout关键字，指定的location位置索引数值0，获取顶点位置索引
        vboManager.setVertexAttributePointer(POSITION_LOCATION_INDEX, POSITION_COMPONENT_COUNT, 0)
        //通过getPositionAttributeLocation方法，获取顶点位置索引
        /*vboManager.setVertexAttributePointer(
            mShaderProgram!!.getPositionAttributeLocation(),
            POSITION_COMPONENT_COUNT,
            0
        )*/
        vboManager.unbindVBO()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT)
        glDrawArrays(GL_TRIANGLES, 0, 3)
    }
}