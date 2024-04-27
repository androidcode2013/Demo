package com.airhockey.android

import android.content.Context
import android.opengl.GLES20.GL_COLOR_BUFFER_BIT
import android.opengl.GLES20.glClear
import android.opengl.GLES20.glClearColor
import android.opengl.GLES20.glViewport
import android.opengl.GLSurfaceView.Renderer
import android.opengl.Matrix.multiplyMM
import android.opengl.Matrix.rotateM
import android.opengl.Matrix.setIdentityM
import android.opengl.Matrix.translateM
import android.util.Log
import com.airhockey.android.objects.Mallet
import com.airhockey.android.objects.Table
import com.airhockey.android.programs.ColorShaderProgram
import com.airhockey.android.programs.TextureShaderProgram
import com.airhockey.android.util.loadTexture
import com.airhockey.android.util.perspectiveM
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

private val TAG = "AirHockeyRenderer"

class AirHockeyRenderer(context: Context) : Renderer {
    private var mContext: Context? = context;

    private val projectionMatrix = FloatArray(16)
    private val modelMatrix = FloatArray(16)

    private var table: Table? = null
    private var mallet: Mallet? = null

    private var textureShaderProgram: TextureShaderProgram? = null
    private var colorShaderProgram: ColorShaderProgram? = null

    private var textureId = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        table = Table()
        mallet = Mallet()

        textureShaderProgram = TextureShaderProgram(mContext!!)
        colorShaderProgram = ColorShaderProgram(mContext!!)

        textureId = loadTexture(mContext!!, R.mipmap.air_hockey_surface)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)
        /*var aspectRatio: Float = 0.0f
        if (width > height) {//landscape
            aspectRatio = (width.toFloat() / height.toFloat())
        } else {//portrait
            aspectRatio = (height.toFloat() / width.toFloat())
        }
        if (width > height) {//landscape
            orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
        } else {//portrait
            orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
        }*/
        perspectiveM(projectionMatrix,45f,(width.toFloat())/(height.toFloat()),
            1f,10f)
        setIdentityM(modelMatrix,0);
        translateM(modelMatrix,0,0f,0f,-2.5f);
        rotateM(modelMatrix,0,-60f,1f,0f,0f)
        val temp = FloatArray(16)
        multiplyMM(temp,0,projectionMatrix,0,modelMatrix,0);
        System.arraycopy(temp,0,projectionMatrix,0,temp.size);
    }

    override fun onDrawFrame(gl: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT)

        textureShaderProgram?.useProgram()
        textureShaderProgram?.setUniforms(projectionMatrix,textureId)
        table?.bindData(textureShaderProgram!!)
        table?.draw()

        colorShaderProgram?.useProgram()
        colorShaderProgram?.setUniforms(projectionMatrix)
        mallet?.bindData(colorShaderProgram!!)
        mallet?.draw()
    }
}