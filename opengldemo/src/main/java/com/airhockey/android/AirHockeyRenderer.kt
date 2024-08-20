package com.airhockey.android

import android.content.Context
import android.opengl.GLES20.GL_COLOR_BUFFER_BIT
import android.opengl.GLES20.glClear
import android.opengl.GLES20.glClearColor
import android.opengl.GLES20.glViewport
import android.opengl.GLSurfaceView.Renderer
import android.opengl.Matrix.invertM
import android.opengl.Matrix.multiplyMM
import android.opengl.Matrix.multiplyMV
import android.opengl.Matrix.rotateM
import android.opengl.Matrix.setIdentityM
import android.opengl.Matrix.setLookAtM
import android.opengl.Matrix.translateM
import com.airhockey.android.objects.Mallet
import com.airhockey.android.objects.Puck
import com.airhockey.android.objects.Table
import com.airhockey.android.programs.ColorShaderProgram
import com.airhockey.android.programs.TextureShaderProgram
import com.airhockey.android.util.Geometry
import com.airhockey.android.util.Geometry.Companion
import com.airhockey.android.util.loadTexture
import com.airhockey.android.util.perspectiveM
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

private val TAG = "AirHockeyRenderer"

class AirHockeyRenderer(context: Context) : Renderer {
    private var mContext: Context? = context;

    private val viewMatrix = FloatArray(16)
    private val viewProjectMatrix = FloatArray(16)
    private val modelViewProjectionMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val modelMatrix = FloatArray(16)

    private var table: Table? = null
    private var mallet: Mallet? = null
    private var puck: Puck? = null

    private var textureShaderProgram: TextureShaderProgram? = null
    private var colorShaderProgram: ColorShaderProgram? = null

    private var textureId = 0
//    private var textureIdImage = 0

    private var malletPressed = false
    private var blueMalletPosition: Companion.Point? = null
    private var previousBlueMalletPosition: Companion.Point? = null

    private var puckPosition: Companion.Point? = null
    private var puckVector: Companion.Vector? = null
    private var invertedViewProjectionMatrix = FloatArray(16)

    private var leftBound = -0.5f
    private var rightBound = 0.5f
    private var farBound = -0.8f
    private var nearBound = 0.8f
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        table = Table()
        mallet = Mallet(0.08f,0.15f,32)
        puck = Puck(0.06f,0.02f,32)

        blueMalletPosition = Companion.Point(0f, mallet!!.height/2f,0.4f)
        puckPosition = Companion.Point(0f, puck!!.height / 2f,0f)
        puckVector = Companion.Vector(0f,0f, 0f)

        textureShaderProgram = TextureShaderProgram(mContext!!)
        colorShaderProgram = ColorShaderProgram(mContext!!)

        textureId = loadTexture(mContext!!, R.mipmap.air_hockey_surface)
//        textureIdImage = loadTexture(mContext!!, R.mipmap.red)
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
        setLookAtM(viewMatrix,0,0.4f,1f,1.2f,
            0f,0f,0f,0f,1f,0f)
//        setIdentityM(modelMatrix,0);
//        translateM(modelMatrix,0,0f,0f,-2.5f);
//        rotateM(modelMatrix,0,-60f,1f,0f,0f)
//        val temp = FloatArray(16)
//        multiplyMM(temp,0,projectionMatrix,0,modelMatrix,0);
//        System.arraycopy(temp,0,projectionMatrix,0,temp.size);
    }

    override fun onDrawFrame(gl: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT)

        puckPosition = puckPosition!!.tranlate(puckVector!!)

        if (puckPosition!!.x < leftBound + puck!!.radius
            || puckPosition!!.x > rightBound - puck!!.radius) {
            puckVector = Companion.Vector(-puckVector!!.x, puckVector!!.y, puckVector!!.z)
            puckVector = puckVector!!.scale(0.9f)
        }
        if (puckPosition!!.z < farBound + puck!!.radius
            || puckPosition!!.z > nearBound - puck!!.radius) {
            puckVector = Companion.Vector(puckVector!!.x, puckVector!!.y, -puckVector!!.z)
            puckVector = puckVector!!.scale(0.9f)
        }
        puckPosition = Companion.Point(
            clamp(puckPosition!!.x, leftBound + puck!!.radius, rightBound - puck!!.radius),
            puckPosition!!.y,
            clamp(puckPosition!!.z, farBound + puck!!.radius, nearBound - puck!!.radius)
        )
        // Friction factor
        puckVector = puckVector!!.scale(0.99f)

        multiplyMM(viewProjectMatrix,0,projectionMatrix,0,viewMatrix,0)
        invertM(invertedViewProjectionMatrix,0, viewProjectMatrix, 0)

        positionTableInScene()
        textureShaderProgram?.useProgram()
        textureShaderProgram?.setUniforms(modelViewProjectionMatrix, textureId)
//        textureShaderProgram?.setUniforms(projectionMatrix, textureIdImage, GL_TEXTURE1)
        table?.bindData(textureShaderProgram!!)
        table?.draw()

        positionObjectInScene(0f, mallet?.height?.toFloat()!! /2f,-0.4f)
        colorShaderProgram?.useProgram()
        colorShaderProgram?.setUniforms(modelViewProjectionMatrix,1f,0f,0f)
        mallet?.bindData(colorShaderProgram!!)
        mallet?.draw()

        positionObjectInScene(blueMalletPosition!!.x, blueMalletPosition!!.y,blueMalletPosition!!.z)
        colorShaderProgram?.setUniforms(modelViewProjectionMatrix,0f,0f,1f)
        mallet?.draw()

        positionObjectInScene(puckPosition!!.x, puckPosition!!.y,puckPosition!!.z)
        colorShaderProgram?.setUniforms(modelViewProjectionMatrix,0.8f,0.8f,1f)
        puck?.bindData(colorShaderProgram!!)
        puck?.draw()

    }

    private fun positionTableInScene(){
        setIdentityM(modelMatrix,0)
        rotateM(modelMatrix,0,-90f,1f,0f,0f)
        multiplyMM(modelViewProjectionMatrix,0,viewProjectMatrix,0,modelMatrix,0)
    }

    private fun positionObjectInScene(x: Float,y:Float,z:Float){
        setIdentityM(modelMatrix,0)
        translateM(modelMatrix,0,x,y,z)
        multiplyMM(modelViewProjectionMatrix,0,viewProjectMatrix,
            0,modelMatrix,0)
    }

    fun handleTouchPress(normalizedX: Float, normalizedY: Float) {
        var ray = convertNormalized2DPointToRay(normalizedX, normalizedY)
        var malletBoundingSphere = Companion.Sphere(
            Companion.Point(
                blueMalletPosition!!.x,
                blueMalletPosition!!.y,
                blueMalletPosition!!.z
            ),
           mallet!!.height /2f //0.15f/2
        )
        malletPressed = Geometry.intersects(malletBoundingSphere, ray)
    }

    fun handleTouchDrag(normalizeX: Float, normalizeY: Float) {
        if(malletPressed){
            var ray = convertNormalized2DPointToRay(normalizeX, normalizeY)
            var plane = Companion.Plane(
                Companion.Point(0f, 0f, 0f),
                Companion.Vector(0f, 1f, 0f)
            )
            var touchedPoint = Geometry.intersectionPoint(ray, plane)
            previousBlueMalletPosition = blueMalletPosition
            blueMalletPosition =
                Companion.Point(
                    clamp(touchedPoint.x, leftBound + mallet!!.radius, rightBound - mallet!!.radius),
                    mallet!!.height / 2f,
                    clamp(touchedPoint.z, 0f + mallet!!.radius, nearBound - mallet!!.radius)
                )

            var distance = Geometry.vectorBetween(blueMalletPosition!!, puckPosition!!).length()
            if (distance < (puck!!.radius + mallet!!.radius)) {
                puckVector = Geometry.vectorBetween(
                    previousBlueMalletPosition!!, blueMalletPosition!!
                )
            }
        }
    }

    fun clamp(value: Float, min: Float, max: Float): Float{
        return Math.min(max, Math.max(value, min))
    }

    fun convertNormalized2DPointToRay(normalizeX: Float, normalizeY: Float) : Companion.Ray{
        var nearPointNdc:FloatArray = floatArrayOf(normalizeX, normalizeY, -1f, 1f )
        var farPointNdc:FloatArray = floatArrayOf(normalizeX, normalizeY, 1f, 1f )

        var nearPointWorld = FloatArray(4)
        var farPointWorld = FloatArray(4)

        multiplyMV(nearPointWorld, 0, invertedViewProjectionMatrix, 0, nearPointNdc, 0)
        multiplyMV(farPointWorld, 0, invertedViewProjectionMatrix, 0, farPointNdc, 0)
        divideByW(nearPointWorld)
        divideByW(farPointWorld)

        var nearPointRay =
            Companion.Point(nearPointWorld[0], nearPointWorld[1], nearPointWorld[2])
        var farPointRay =
            Companion.Point(farPointWorld[0], farPointWorld[1], farPointWorld[2])

        return Companion.Ray(
            nearPointRay,
            Geometry.vectorBetween(nearPointRay, farPointRay)
        )
    }
    fun divideByW(vector: FloatArray){
        vector[0] /= vector[3]
        vector[1] /= vector[3]
        vector[2] /= vector[3]
    }
}