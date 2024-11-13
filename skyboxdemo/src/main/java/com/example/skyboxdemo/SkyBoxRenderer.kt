package com.example.skyboxdemo

import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView.Renderer
import android.opengl.Matrix
import android.util.Log
import com.example.skyboxdemo.objects.Skybox
import com.example.skyboxdemo.program.SkyBoxShaderProgram
import com.opengllib.util.MatrixHelper
import com.opengllib.util.loadCubemap
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class SkyBoxRenderer(context: Context) : Renderer {
    private var mContext = context
    private var mSkyBoxShaderProgram: SkyBoxShaderProgram? = null
    private var mSkybox: Skybox? = null
    private var viewMatrix = FloatArray(16)
    private var projectionMatrix = FloatArray(16)
    private var viewProjectionMatrix = FloatArray(16)
    private var mCubeMapTexture = 0
    private var mImgIds = intArrayOf(
        R.mipmap.right,
        R.mipmap.left,
        R.mipmap.top,
        R.mipmap.bottom,
        R.mipmap.back,
        R.mipmap.front,
    )

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES30.glClearColor(0.8f, 0.8f, 0.8f, 1.0f)

        mCubeMapTexture = loadCubemap(context = mContext, mImgIds)
        mSkyBoxShaderProgram = SkyBoxShaderProgram(
            mContext, R.raw.skybox_vertex_shader,
            R.raw.skybox_fragment_shader
        )
        mSkyBoxShaderProgram?.useProgram()
        mSkybox = Skybox()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
        MatrixHelper.perspectiveM(
            projectionMatrix, 45f,
            (width.toFloat() / height), 1f, 10f
        )
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
        drawSkybox()
    }

    fun drawSkybox() {
        Matrix.setIdentityM(viewMatrix, 0)
        Matrix.rotateM(viewMatrix, 0, -yRotation, 1f, 0f, 0f)
        Matrix.rotateM(viewMatrix, 0, -xRotation, 0f, 1f, 0f)
        Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
        mSkyBoxShaderProgram?.setUniforms(viewProjectionMatrix, mCubeMapTexture)
        mSkybox?.bindData(mSkyBoxShaderProgram!!)
        mSkybox?.draw()
    }

    private var xRotation = 0f
    private var yRotation = 0f
    fun handleTouchDrag(deltaX: Float, deltaY: Float) {
        xRotation += deltaX / 16f
        yRotation += deltaY / 16f
        if (yRotation < -90) {
            yRotation = -90f
        } else if (yRotation > 90) {
            yRotation = 90f
        }
    }
}