package com.particles.android

import android.content.Context
import android.graphics.Color
import android.opengl.GLES20.GL_BLEND
import android.opengl.GLES20.GL_COLOR_BUFFER_BIT
import android.opengl.GLES20.GL_ONE
import android.opengl.GLES20.glBlendFunc
import android.opengl.GLES20.glClear
import android.opengl.GLES20.glClearColor
import android.opengl.GLES20.glDisable
import android.opengl.GLES20.glEnable
import android.opengl.GLES20.glViewport
import android.opengl.GLSurfaceView.Renderer
import android.opengl.Matrix.multiplyMM
import android.opengl.Matrix.setIdentityM
import android.opengl.Matrix.translateM
import android.os.Build
import androidx.annotation.RequiresApi
import com.particles.android.objects.ParticleShooter
import com.particles.android.objects.ParticleSystem
import com.particles.android.objects.Skybox
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

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

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
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)

        MatrixHelper.perspectiveM(projectionMatrix, 45f,
            (width.toFloat() / height), 1f, 10f)
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onDrawFrame(gl: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT)

        drawSkybox()
        drawParticles()
    }

    fun drawSkybox() {
        setIdentityM(viewMatrix, 0)
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
        skyboxShaderProgram?.useProgram()
        skyboxShaderProgram?.setUniforms(viewProjectionMatrix, skyboxTexture)
        skybox?.bindData(skyboxShaderProgram!!)
        skybox?.draw()
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun drawParticles(){
        val currentTime = (System.nanoTime() - globalStartTime) / 1000000000f
        redParticleShooter?.addParticles(particleSystem!!, currentTime, 1)
        greenParticleShooter?.addParticles(particleSystem!!, currentTime, 1)
        blueParticleShooter?.addParticles(particleSystem!!, currentTime, 1)

        setIdentityM(viewMatrix, 0)
        translateM(viewMatrix, 0, 0f, -1.5f, -5f)
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        glEnable(GL_BLEND)
        glBlendFunc(GL_ONE, GL_ONE)

        particleShaderProgram?.useProgram()
        particleShaderProgram?.setUniforms(viewProjectionMatrix, currentTime, particleTexture)
        particleSystem?.bindData(particleShaderProgram!!)
        particleSystem?.draw()

        glDisable(GL_BLEND)
    }
}