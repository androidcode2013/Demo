package com.example.fbodemo.renderer

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES20.GL_TEXTURE0
import android.opengl.GLES20.GL_TEXTURE1
import android.opengl.GLES30
import android.opengl.GLSurfaceView.Renderer
import com.example.fbodemo.R
import com.example.fbodemo.data.Image
import com.example.fbodemo.programs.TextureShaderProgram
import com.opengllib.util.loadTexture
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 图片纹理渲染到默认缓冲
 */
class TextureRenderer(context: Context) : Renderer {
    private val TAG = "TextureRenderer"
    private var mContext = context
    private var mTextureShaderProgram: TextureShaderProgram? = null
    private var mGirl: Image? = null
    private var mTextureId = 0
    private var mTextureMeiNvId = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES30.glClearColor(0.0f, 1.0f, 0.0f, 1.0f)
        mTextureId = loadTexture(mContext, R.mipmap.scenery)
        mTextureMeiNvId = loadTexture(mContext, R.mipmap.meinv)

        mTextureShaderProgram =
            TextureShaderProgram(
                mContext,
                R.raw.fbo_vertex_shader,
                R.raw.multitexture_fragment_shader
            )
        mTextureShaderProgram!!.useProgram()
        mGirl = Image()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)

        GLES20.glActiveTexture(GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId)
        mTextureShaderProgram!!.setUniforms(
            mTextureShaderProgram!!.getTextureUniformLocation(),
            0
        )
        GLES20.glActiveTexture(GL_TEXTURE1)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureMeiNvId)
        mTextureShaderProgram!!.setUniforms(
            mTextureShaderProgram!!.getTextureUniform1Location(),
            1
        )
        mGirl!!.enableVertexAttribPointer(mTextureShaderProgram!!)
        mGirl!!.draw()
    }
}