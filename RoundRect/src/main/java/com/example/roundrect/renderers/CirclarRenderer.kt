package com.example.roundrect.renderers

import android.content.Context
import android.opengl.GLES30
import com.example.roundrect.Constant
import com.opengllib.data.VertexArray
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.cos
import kotlin.math.sin

open class CirclarRenderer(context: Context) : BaseRenderer(context) {
    var mVerticesCircle: FloatArray? = null
    var mCirclarVertexArray: VertexArray? = null

    init {
        mVerticesCircle = createCircle(0.5f, 100)
    }

    fun createCircle(radius: Float, n: Int): FloatArray? {
        val data = ArrayList<Float>()
        data.add(0.45f) //设置圆心坐标
        data.add(0.45f)
        val angDegSpan = 360f / n
        var i = 0f
        while (i < 360 + angDegSpan) {
            data.add((radius * sin(i * Math.PI / 180f)).toFloat()+0.45f)
            data.add((radius * cos(i * Math.PI / 180f)).toFloat()+0.45f)
            i += angDegSpan
        }
        val f = FloatArray(data.size)
        for (i in f.indices) {
            f[i] = data[i]
        }
        return f
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        super.onSurfaceCreated(gl, config)
        mCirclarVertexArray = VertexArray(mVerticesCircle!!)
        mCirclarVertexArray?.setVertexAttribPointer(
            0,
            mShaderProgram?.getPositionAttributeLocation()!!,
            Constant.POSITION_COMPONENT_COUNT,
            0
        )
    }

    override fun onDrawFrame(gl: GL10?) {
        super.onDrawFrame(gl)
        //绘制圆形
        GLES30.glUniform4f(mShaderProgram!!.getColorUniformLocation(), 0.0f, 0.0f, 1.0f, 0.5f)
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_FAN, 0, mVerticesCircle?.size!! / 2);
    }
}