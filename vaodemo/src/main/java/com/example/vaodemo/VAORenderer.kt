package com.example.vaodemo

import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView.Renderer
import com.opengllib.data.VAOManager
import com.opengllib.programs.ShaderProgram
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class VAORenderer(context: Context) : Renderer {
    private var mShaderProgram: ShaderProgram? = null
    private var mContext = context
    private var mVaoIds = IntArray(1)//顶点缓冲对象VAO id标识
    private var mVboIds = IntArray(1)//顶点缓冲对象VBO id标识
    private var mVaoManager: VAOManager? = null
    private val POSITION_COMPONENT_COUNT = 3
    private val mVertexPoints = floatArrayOf(
        0.0f, 0.5f, 0.0f,
        -0.5f, -0.5f, 0.0f,
        0.5f, -0.5f, 0.0f
    )

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        //设置背景颜色
        GLES30.glClearColor(0.3f, 0.7f, 0.5f, 1.0f);
        mShaderProgram =
            ShaderProgram(mContext, R.raw.vao_vertex_shader, R.raw.vao_fragment_shader)
        mShaderProgram?.useProgram()
        mVaoManager = VAOManager(mVertexPoints)
        mVaoManager?.allocateBuffer()
        mVaoManager?.bindVAO(mVaoIds)
        mVaoManager?.bindVBO(mVboIds)
        mVaoManager?.putDataTOVBO()
        //通过着色器中layout关键字，指定的location位置索引数值0，获取顶点位置索引
        mVaoManager?.setVertexAttributePointer(
            mShaderProgram!!.getPositionAttributeLocation(),
            POSITION_COMPONENT_COUNT,
            0
        )
        mVaoManager?.unbindVBO()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 3)
    }

    fun unbindVAO() {
        mVaoManager?.unbindVAO()
    }
}