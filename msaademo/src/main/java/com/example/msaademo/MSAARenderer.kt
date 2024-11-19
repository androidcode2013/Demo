package com.example.msaademo

import android.content.Context
import android.opengl.GLES10.GL_MULTISAMPLE
import android.opengl.GLES10.glFlush
import android.opengl.GLES20
import android.opengl.GLES30
import android.opengl.GLSurfaceView.Renderer
import android.util.Log
import com.opengllib.data.VertexArray
import com.opengllib.programs.ShaderProgram
import com.opengllib.util.OpenGLUtil
import java.nio.IntBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 多重采样抗锯齿MSAA演示demo
 */
class MSAARenderer(context: Context) : Renderer {
    private var mContext = context
    private var mShaderProgram: ShaderProgram? = null
    private var mVertexArray: VertexArray? = null
    private val POSITION_COMPONENT_COUNT = 3

    //顶点数据
    var mVertices = floatArrayOf(
        0.5f, 0.5f, 0.0f,  // 蓝色顶点
        -0.5f, 0.5f, 0.0f,  // 红色顶点
        0.0f, -0.5f, 0.0f // 绿色顶点
    )
    private var mFrameBuffer = IntArray(1)
    private var mRenderBuffer = IntArray(1)
    private var mCountBuffer = IntArray(1)
    private var mWidth = 0
    private var mHeight = 0
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES30.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
        //开启多采样
        GLES30.glEnable(GL_MULTISAMPLE)
        mVertexArray = VertexArray(mVertices)
        mShaderProgram = ShaderProgram(
            mContext,
            R.raw.msaa_vertex_shader,
            R.raw.msaa_fragment_shader
        )
        mShaderProgram!!.useProgram()
        mVertexArray!!.setVertexAttribPointer(
            0,
            mShaderProgram!!.getPositionAttributeLocation(),
            POSITION_COMPONENT_COUNT, 0
        )
        /*
        var counts =  OpenGLUtil.createIntBuffer(mCountBuffer)
        GLES30.glGetInternalformativ(
            GLES30.GL_RENDERBUFFER,
            GLES30.GL_RGBA8,
            GLES30.GL_NUM_SAMPLE_COUNTS,
            1, counts)
        var samples = OpenGLUtil.createIntBuffer(IntArray(counts.capacity()))
        //读取一下当前驱动支持的采样值，若开发商设置的值不支持，会就近选一个近邻的采样值，不然产生黑屏的话很难查Bug。
        GLES30.glGetInternalformativ(
            GLES30.GL_RENDERBUFFER,
            GLES30.GL_RGBA8,
            GLES30.GL_SAMPLES, counts.capacity(), samples);
        for (i in 0 until  samples.capacity()){
            Log.d("log","support sample count:"+samples[i].toDouble());
        }*/
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        mWidth = width
        mHeight = height
        GLES30.glViewport(0, 0, width, height)
        //生成多重采样缓冲区
        GLES30.glGenFramebuffers(1, mFrameBuffer, 0)
        //生成多重采样渲染缓冲对象
        GLES30.glGenRenderbuffers(1, mRenderBuffer, 0)
        GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER, mRenderBuffer[0])
        GLES30.glRenderbufferStorageMultisample(
            GLES30.GL_RENDERBUFFER,
            4,
            GLES30.GL_RGBA8,
            width,
            height
        )
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, mFrameBuffer[0])
        GLES30.glFramebufferRenderbuffer(
            GLES30.GL_FRAMEBUFFER,
            GLES30.GL_COLOR_ATTACHMENT0,
            GLES30.GL_RENDERBUFFER,
            mRenderBuffer[0]
        )
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
        GLES30.glBindFramebuffer(GLES30.GL_READ_FRAMEBUFFER, mFrameBuffer[0])
        GLES30.glBindFramebuffer(GLES30.GL_DRAW_FRAMEBUFFER, 0);
        GLES30.glBlitFramebuffer(
            0, 0, mWidth, mHeight,
            0, 0, mWidth, mHeight,
            GLES30.GL_COLOR_BUFFER_BIT, GLES30.GL_NEAREST
        )
        GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER, mRenderBuffer[0])
        GLES20.glUniform4f(
            mShaderProgram!!.getColorUniformLocation(),
            0.5f, 0.6f, 0.8f, 1.0f
        );
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0)
        GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER, 0)
    }
}