package com.example.fbodemo.renderer

import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView.Renderer
import android.util.Log
import com.example.fbodemo.R
import com.example.fbodemo.data.Image
import com.example.fbodemo.programs.TextureShaderProgram
import com.opengllib.data.FBOManager
import com.opengllib.util.loadTexture
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 纹理附件关联FBO
 */
class FBORenderer(context: Context) : Renderer {
    private val TAG = "FBORenderer"
    private var mFBOManager: FBOManager? = null
    private var mFBOIds = intArrayOf(1)
    private var mFrameTextures = intArrayOf(1)
    private var mRBOIds = intArrayOf(1)
    private var mContext = context
    private var mProgram: TextureShaderProgram? = null
    private var mFilterShaderProgram: TextureShaderProgram? = null
    private var mImageTextureId = 0
    private var mGirl: Image? = null

    //是否使用FBO
    private var mIsUseFBo = true
    private var mIsRBO = false

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES30.glClearColor(1.0f, 0.0f, 0.0f, 1.0f)
        mProgram =
            TextureShaderProgram(
                mContext,
                R.raw.fbo_vertex_shader,
                R.raw.fbo_fragment_shader
            )
        mProgram!!.useProgram()
        if (mIsUseFBo) {
            mFilterShaderProgram = TextureShaderProgram(
                mContext,
                R.raw.fbo_vertex_shader,
                R.raw.fbo_fragment_edge_shader
            )
            mFilterShaderProgram!!.useProgram()
        }
        mImageTextureId = loadTexture(mContext, R.mipmap.girl)
        mGirl = Image()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
        if (mIsUseFBo) {
            mFBOManager = FBOManager()
            mFBOManager!!.genFBO(mFBOIds)
            mFBOManager!!.genTexture(mFrameTextures)
            mFBOManager!!.bindFBO(mFBOIds)
            mFBOManager!!.bindTexture(mFrameTextures)
            mFBOManager!!.attachTextureToFBO(mFrameTextures, width, height)
            if (mIsRBO) {
                mFBOManager!!.genRBO(mRBOIds)
                mFBOManager!!.bindRBO(mRBOIds)
                mFBOManager!!.attachRBOToFBO(
                    GLES30.GL_DEPTH24_STENCIL8,
                    width,
                    height,
                    GLES30.GL_DEPTH_STENCIL_ATTACHMENT,
                    mRBOIds
                )
            }
            if (!mFBOManager!!.isCheckFBO()) {
                Log.d("", "FBO incomplete")
            }
            mFBOManager!!.unbindTexture()
            mFBOManager!!.unbindFBO()
            if (mIsRBO) {
                mFBOManager!!.unbindRBO()
            }
            mFBOManager!!.deleteFBO(mFBOIds)
        }
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
        //绘制图片
        mProgram!!.bindTexture(mImageTextureId)
        mGirl!!.enableVertexAttribPointer(mProgram!!)
        mGirl!!.draw()
        mGirl!!.disableVertexAttribPointer(mProgram!!)

        if (mIsUseFBo) {
            //绑定FBO
            mFBOManager!!.bindFBO(mFBOIds)
            if (mIsRBO) {
                mFBOManager!!.bindRBO(mRBOIds)
                GLES30.glEnable(GLES30.GL_DEPTH_TEST)
            }
            mFBOManager!!.bindTexture(mFrameTextures)
            //对FBO纹理附件做滤镜处理
            mFilterShaderProgram!!.bindTexture(mFrameTextures[0])
            mFilterShaderProgram!!.setUniforms(mFilterShaderProgram!!.getTextureUniformLocation())
            mGirl!!.enableVertexAttribPointer(mFilterShaderProgram!!)
            mGirl!!.draw()
            mGirl!!.disableVertexAttribPointer(mFilterShaderProgram!!)
            //解绑FBO
            mFBOManager!!.unbindTexture()
            mFBOManager!!.unbindFBO()
            if (mIsRBO) {
                mFBOManager!!.unbindRBO()
                GLES30.glDisable(GLES30.GL_DEPTH_TEST)
            }
            mFBOManager!!.deleteFBO(mFBOIds)
        }
    }
}