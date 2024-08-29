package com.particles.android

import android.content.Context
import android.graphics.Color
import android.opengl.GLES20.GL_BLEND
import android.opengl.GLES20.GL_COLOR_BUFFER_BIT
import android.opengl.GLES20.GL_CULL_FACE
import android.opengl.GLES20.GL_CW
import android.opengl.GLES20.GL_DEPTH_BUFFER_BIT
import android.opengl.GLES20.GL_DEPTH_TEST
import android.opengl.GLES20.GL_LEQUAL
import android.opengl.GLES20.GL_LESS
import android.opengl.GLES20.GL_ONE
import android.opengl.GLES20.glBlendFunc
import android.opengl.GLES20.glClear
import android.opengl.GLES20.glClearColor
import android.opengl.GLES20.glDepthFunc
import android.opengl.GLES20.glDepthMask
import android.opengl.GLES20.glDisable
import android.opengl.GLES20.glEnable
import android.opengl.GLES20.glFrontFace
import android.opengl.GLES20.glViewport
import android.opengl.GLSurfaceView.Renderer
import android.opengl.Matrix.multiplyMM
import android.opengl.Matrix.rotateM
import android.opengl.Matrix.scaleM
import android.opengl.Matrix.setIdentityM
import android.opengl.Matrix.translateM
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toBitmap
import com.particles.android.objects.Heightmap
import com.particles.android.objects.ParticleShooter
import com.particles.android.objects.ParticleSystem
import com.particles.android.objects.Skybox
import com.particles.android.programs.HeightmapShaderProgram
import com.particles.android.programs.ParticleShaderProgram
import com.particles.android.programs.SkyboxShaderProgram
import com.particles.android.util.Geometry
import com.particles.android.util.MatrixHelper
import com.particles.android.util.loadCudeMap
import com.particles.android.util.loadTexture

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

private val TAG = "ParticlesRenderer"

class ParticlesRenderer(private var context: Context) : Renderer {

    private var projectionMatrix = FloatArray(16)
    private var viewMatrix = FloatArray(16)
    private var viewProjectionMatrix = FloatArray(16)
    private var modelMatrix = FloatArray(16)
    private var viewMatrixForSkybox = FloatArray(16)
    private var tempMatrix = FloatArray(16)
    private var modelViewProjectionMatrix = FloatArray(16)

    private var particleShaderProgram: ParticleShaderProgram? = null
    private var particleSystem: ParticleSystem? = null
    private var redParticleShooter: ParticleShooter? = null
    private var greenParticleShooter: ParticleShooter? = null
    private var blueParticleShooter: ParticleShooter? = null

    private var globalStartTime = 0L

    private var angleVarianceInDegrees = 5f
    private var speedVariance = 1f

    private var particleTexture = 0

    private var skyboxShaderProgram: SkyboxShaderProgram? = null
    private var skybox: Skybox? = null
    private var skyboxTexture = 0

    private var heightmapShaderProgram: HeightmapShaderProgram? = null
    private var heightmap: Heightmap? = null

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        glEnable(GL_CULL_FACE)//开启剔除功能，默认剔除背面
//        glFrontFace(GL_CW)

        particleShaderProgram = ParticleShaderProgram(context)
        particleSystem = ParticleSystem(10000)
        globalStartTime = System.nanoTime()

        val particleDirection = Geometry.Companion.Vector(0.0f, 0.5f, 0.0f)
        redParticleShooter = ParticleShooter(
            Geometry.Companion.Point(-1f, 0f, 0f),
            particleDirection,
            Color.rgb(255, 0, 0),
            angleVarianceInDegrees,
            speedVariance
        )
        greenParticleShooter = ParticleShooter(
            Geometry.Companion.Point(0f, 0f, 0f),
            particleDirection,
            Color.rgb(0, 255, 0),
            angleVarianceInDegrees,
            speedVariance
        )
        blueParticleShooter = ParticleShooter(
            Geometry.Companion.Point(1f, 0f, 0f),
            particleDirection,
            Color.rgb(0, 0, 255),
            angleVarianceInDegrees,
            speedVariance
        )

        particleTexture = loadTexture(context, R.drawable.particle_texture)

        skyboxShaderProgram = SkyboxShaderProgram(context)
        skybox = Skybox()
        skyboxTexture = loadCudeMap(
            context,
            arrayOf(
                R.drawable.left, R.drawable.right,
                R.drawable.bottom, R.drawable.top,
                R.drawable.front, R.drawable.back
            ).toIntArray()
        )
        heightmapShaderProgram = HeightmapShaderProgram(context)
        heightmap = Heightmap(context.resources.getDrawable(R.drawable.heightmap).toBitmap())
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)

        MatrixHelper.perspectiveM(projectionMatrix, 45f,
            (width.toFloat() / height), 1f, 100f)
        updateViewMatrices()
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onDrawFrame(gl: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)//清空深度缓冲区

        glEnable(GL_DEPTH_TEST)//开启深度测试功能
        drawHeightmap()
        drawSkybox()
        drawParticles()
    }

    private fun drawSkybox() {
        setIdentityM(modelMatrix, 0)
        updateMvpMatrixForSkybox()

        glDepthFunc(GL_LEQUAL)//设置新的深度值<深度缓冲中深度数值，通过深度测试，更新深度缓冲中数值为新值
        skyboxShaderProgram?.useProgram()
        skyboxShaderProgram?.setUniforms(modelViewProjectionMatrix, skyboxTexture)
        skybox?.bindData(skyboxShaderProgram!!)
        skybox?.draw()
        glDepthFunc(GL_LESS)//重置为默认值GL_LESS
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun drawParticles(){
        val currentTime = (System.nanoTime() - globalStartTime) / 1000000000f
        redParticleShooter?.addParticles(particleSystem!!, currentTime, 1)
        greenParticleShooter?.addParticles(particleSystem!!, currentTime, 1)
        blueParticleShooter?.addParticles(particleSystem!!, currentTime, 1)

        setIdentityM(modelMatrix, 0)
        updateMvpMatrix()

        glDepthMask(false)
        glEnable(GL_BLEND)
        glBlendFunc(GL_ONE, GL_ONE)

        particleShaderProgram?.useProgram()
        particleShaderProgram?.setUniforms(modelViewProjectionMatrix, currentTime, particleTexture)
        particleSystem?.bindData(particleShaderProgram!!)
        particleSystem?.draw()

        glDisable(GL_BLEND)
        glDepthMask(true)
    }

    private fun drawHeightmap() {
        setIdentityM(modelMatrix, 0)
        scaleM(modelMatrix, 0, 100f, 10f, 100f)
        updateMvpMatrix()
        heightmapShaderProgram?.useProgram()
        heightmapShaderProgram?.setUniforms(modelViewProjectionMatrix)
        heightmap?.bindData(heightmapShaderProgram!!)
        heightmap?.draw()
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
        updateViewMatrices()
    }
    private fun updateViewMatrices() {
        setIdentityM(viewMatrix, 0)
        rotateM(viewMatrix, 0, -yRotation, 1f, 0f, 0f)
        rotateM(viewMatrix, 0, -xRotation, 0f, 1f, 0f)
        System.arraycopy(viewMatrix, 0, viewMatrixForSkybox, 0, viewMatrix.size)
        translateM(viewMatrix, 0, 0f, -1.5f, -5f)
    }

    private fun updateMvpMatrix() {
        multiplyMM(tempMatrix, 0, viewMatrix, 0, modelMatrix, 0)
        multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, tempMatrix, 0)
    }

    private fun updateMvpMatrixForSkybox(){
        multiplyMM(tempMatrix, 0, viewMatrixForSkybox, 0, modelMatrix, 0)
        multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, tempMatrix, 0)
    }
}