package com.example.glsldemo

import android.content.Context
import android.opengl.GLES20.GL_POINTS
import android.opengl.GLES20.glDrawArrays
import android.opengl.GLES20.glUniform4f
import android.opengl.GLES30
import android.opengl.GLSurfaceView.Renderer
import com.opengllib.data.VertexArray
import com.opengllib.programs.ShaderProgram
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class AdvancedGLSLRenderer(context: Context) : Renderer {
    private var mContext = context
    private var mShaderProgram: ShaderProgram? = null
    private var mVertexArray: VertexArray? = null
    private val POSITION_COMPONENT_COUNT = 3

    //顶点数据
    var mVertices = floatArrayOf(
        /*
        注意：顶点坐标数值设置较小一些。
        如果坐标数值太大，可能存在顶点位置超出屏幕位置的情况，
        导致在屏幕上看不到画出的顶点
         */
        0.5f, 0.5f, 0.0f,  // 蓝色顶点
        -0.5f, -0.5f, 0.0f,  // 红色顶点
        0.0f, -0.0f, 0.0f // 绿色顶点
    )

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES30.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
        mVertexArray = VertexArray(mVertices)
        mShaderProgram = ShaderProgram(
            mContext,
            R.raw.advanced_vertex_shader,
            R.raw.advanced_fragment_shader
        )
        mShaderProgram!!.useProgram()
        mVertexArray!!.setVertexAttribPointer(
            0,
            mShaderProgram!!.getPositionAttributeLocation(),
            POSITION_COMPONENT_COUNT, 0
        )
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)

        glUniform4f(
            mShaderProgram!!.getColorUniformLocation(),
            0.0f, 0.0f, 1.0f, 1.0f
        );
        glDrawArrays(GL_POINTS, 0, 1);
        glUniform4f(
            mShaderProgram!!.getColorUniformLocation(),
            1.0f, 0.0f, 0.0f, 1.0f
        );
        glDrawArrays(GL_POINTS, 1, 1);
        glUniform4f(
            mShaderProgram!!.getColorUniformLocation(),
            0.0f, 1.0f, 0.0f, 1.0f
        );
        glDrawArrays(GL_POINTS, 2, 1);
    }
}
