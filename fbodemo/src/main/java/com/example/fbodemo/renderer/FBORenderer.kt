package com.example.fbodemo.renderer

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES30
import android.opengl.GLSurfaceView.Renderer
import android.util.Log
import com.example.fbodemo.R
import com.example.fbodemo.data.Girl
import com.example.fbodemo.programs.TextureShaderProgram
import com.opengllib.data.FBOManager
import com.opengllib.util.generateBitmap
import com.opengllib.util.getImgTexture
import com.opengllib.util.loadTexture
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 纹理附加、RBO附加到FBO
 */
class FBORenderer(context: Context) : Renderer {
    private val TAG = "FBORenderer"
    private var mFBOManager: FBOManager? = null
//    private var mFBOIds = intArrayOf(1)
//    private var mRBOIds = intArrayOf(1)
    private var mTextureIds = intArrayOf(1)
    private var mContext = context
    private var mTextureShaderProgram: TextureShaderProgram? = null
    private var mImageTextureId = 0
    private var mBitmap: Bitmap? = null
    private var mGirl: Girl? = null

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES30.glClearColor(1.0f, 0.0f, 0.0f, 1.0f)
        mTextureShaderProgram =
            TextureShaderProgram(mContext, R.raw.fbo_vertex_shader, R.raw.fbo_fragment_shader)
        mTextureShaderProgram!!.useProgram()
        mBitmap = generateBitmap(mContext,R.mipmap.girl)
        mImageTextureId = getImgTexture(mBitmap!!)
        mGirl = Girl()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
        mFBOManager = FBOManager()
//        mFBOManager!!.bindFBO(mFBOIds)
        mFBOManager!!.bindTexture(mTextureIds)
//        mFBOManager!!.attachTextureToFBO(mTextureIds, mBitmap!!.width, mBitmap!!.height)
//        mFBOManager!!.bindRBO(mRBOIds)
        //注意，在指定渲染缓冲对象格式时，是GL_DEPTH24_STENCIL8，它封装了24位的深度和8位的模板缓冲
//        mFBOManager!!.putRBOToFBO(
//            GLES30.GL_DEPTH24_STENCIL8,
//            30, 30,
//            GLES30.GL_DEPTH_STENCIL_ATTACHMENT,
//            mRBOIds
//        )
//        if (mFBOManager!!.isCheckFBO()) {
//            Log.d("", "FBO complete")
//        } else Log.d("", "FBO incomplete")
//        mFBOManager!!.unbindTexture()
//        mFBOManager!!.unbindFBO()
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
//        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, mFBOIds[0])
        mTextureShaderProgram!!.setUniforms(mImageTextureId)
        mGirl!!.setVertexAttribPointer(mTextureShaderProgram!!)
        mGirl!!.draw()
        //解绑纹理
//        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);

        //解绑fbo
//        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);
    }
}