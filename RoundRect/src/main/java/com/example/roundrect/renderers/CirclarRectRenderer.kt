package com.example.roundrect.renderers

import android.content.Context
import android.opengl.GLES30
import com.example.roundrect.Constant
import com.example.roundrect.R
import com.example.roundrect.program.RoundRectShaderProgram
import com.opengllib.data.VertexArray
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.cos
import kotlin.math.sin

class CirclarRectRenderer(context: Context) : CirclarRenderer(context) {
    private val CIRLAR_COUNT = 100
    private val RADIUS = 0.05f
    private var mVertices = floatArrayOf(
        -0.5f, -0.8f,
        0.5f, 0.8f,
        -0.5f, 0.8f,

        -0.5f, -0.8f,
        0.5f, -0.8f,
        0.5f, 0.8f
    )

    init {
        mVerticesCircle = createCirclarRect(RADIUS, CIRLAR_COUNT)
    }

    fun createCirclarRect(radius: Float, n: Int): FloatArray? {
        val data = ArrayList<Float>()
        for (index in mVertices.indices) {
            data.add(mVertices[index])
        }
        //右圆形
        data.add(0.5f - radius) //设置圆心坐标
        data.add(0.85f - radius)
        val angDegSpan = 360f / n
        var i = 0f
        while (i < 360 + angDegSpan) {
            data.add((radius * sin(i * Math.PI / 180f)).toFloat() + (0.5f - radius))
            data.add((radius * cos(i * Math.PI / 180f)).toFloat() + (0.5f - radius))
            i += angDegSpan
        }
        //左圆形
        data.add(-0.5f + radius) //设置圆心坐标
        data.add(-0.8f + radius)
        val angDegSpanL = 360f / n
        var iL = 0f
        while (iL < 360 + angDegSpanL) {
            data.add((radius * sin(iL * Math.PI / 180f)).toFloat() + (-0.5f + radius))
            data.add((radius * cos(iL * Math.PI / 180f)).toFloat() + (-0.5f + radius))
            iL += angDegSpanL
        }
        data.add(-0.5f + radius) //设置圆心坐标
        data.add(0.8f - radius)
        val angDegSpanL2 = 360f / n
        var iL2 = 0f
        while (iL2 < 360 + angDegSpanL2) {
            data.add((radius * sin(iL2 * Math.PI / 180f)).toFloat() + (-0.5f + radius))
            data.add((radius * cos(iL2 * Math.PI / 180f)).toFloat() + (0.5f - radius))
            iL2 += angDegSpanL2
        }
        data.add(0.5f - radius) //设置圆心坐标
        data.add(-0.8f + radius)
        val angDegSpanR = 360f / n
        var iR = 0f
        while (iR < 360 + angDegSpanR) {
            data.add((radius * sin(iR * Math.PI / 180f)).toFloat() + (0.5f - radius))
            data.add((radius * cos(iR * Math.PI / 180f)).toFloat() + (-0.5f + radius))
            iR += angDegSpanR
        }
        val f = FloatArray(data.size)
        for (i in f.indices) {
            f[i] = data[i]
        }
        return f
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES30.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
        mShaderProgram = RoundRectShaderProgram(
            mContext,
            R.raw.roundrect_vertex_shader,
            R.raw.roundrect_fragment_shader
        )
        mShaderProgram?.useProgram()

        mCirclarVertexArray = VertexArray(mVerticesCircle!!)
        mCirclarVertexArray?.setVertexAttribPointer(
            0,
            mShaderProgram?.getPositionAttributeLocation()!!,
            Constant.POSITION_COMPONENT_COUNT,
            0
        )
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        super.onSurfaceChanged(gl, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
        GLES30.glUniform4f(mShaderProgram!!.getColorUniformLocation(), 0.0f, 1.0f, 0.0f, 0.5f)
        //绘制矩形
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_FAN, 0, 6)
        //绘制圆形
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_FAN, 7, CIRLAR_COUNT)
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_FAN, 7 + CIRLAR_COUNT + 2, CIRLAR_COUNT)
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_FAN, 7 + CIRLAR_COUNT * 2 + 4, CIRLAR_COUNT)
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_FAN, 7 + CIRLAR_COUNT * 3 + 6, CIRLAR_COUNT)
    }
}