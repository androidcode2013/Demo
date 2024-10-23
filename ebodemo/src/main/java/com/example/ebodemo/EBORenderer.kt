package com.example.ebodemo

import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView.Renderer
import com.opengllib.data.EBOManager
import com.opengllib.programs.ShaderProgram
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class EBORenderer(context: Context) : Renderer {
    private var mShaderProgram: ShaderProgram? = null
    private var mContext = context
    private var mEBOManager: EBOManager? = null
    private val mVertexPoints = floatArrayOf(
        -0.25f, -0.25f, 0.0f,
        0.25f, -0.25f, 0.0f,
        -0.25f, 0.25f, 0.0f,
        0.25f, 0.25f, 0.0f,
    )
    private var indices = shortArrayOf(
        0, 1, 2,
        1, 2, 3
    )

    private var mEboIds = IntArray(1)//索引缓冲区对象EBO id标识
    private var mVaoIds = IntArray(1)//顶点缓冲对象VAO id标识
    private var mVboIds = IntArray(1)//顶点缓冲对象VBO id标识

    private val POSITION_COMPONENT_COUNT = 3

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES30.glClearColor(0.2f, 3.0f, 5.0f, 6.0f)
        mShaderProgram = ShaderProgram(mContext, R.raw.ebo_vertex_shader, R.raw.ebo_fragment_shader)
        mShaderProgram?.useProgram()
        mEBOManager = EBOManager(mVertexPoints, indices)
        mEBOManager?.allocateBuffer()
        mEBOManager?.bindVAO(mVaoIds)
        //注意：EBO绑定顺序要在VAO后面，否则绘制不出图形
        mEBOManager?.bindEBO(mEboIds)
        mEBOManager?.bindVBO(mVboIds)
        mEBOManager?.putData()
        mEBOManager?.setVertexAttributePointer(
            mShaderProgram!!.getPositionAttributeLocation(),
            POSITION_COMPONENT_COUNT,
            0
        )
        mEBOManager?.unbindVBO()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
        //索引缓冲区数据填充，类型必须为short/byte,GL_UNSIGNED_SHORT
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, 6, GLES30.GL_UNSIGNED_SHORT, 0)
    }

    fun release() {
        GLES30.glDisableVertexAttribArray(mShaderProgram!!.getPositionAttributeLocation())
        if (mEBOManager != null) {
            mEBOManager?.unbindVAO()
            mEBOManager?.unbindEBO()
            mEBOManager = null
        }
    }
}