package com.example.stencildemo.renderer

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView.Renderer
import android.opengl.Matrix
import com.example.stencildemo.R
import com.example.stencildemo.program.StencilShaderProgram
import com.opengllib.util.BYTES_PER_FLOAT
import com.opengllib.util.OpenGLUtil
import com.opengllib.util.loadTexture
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 模板测试
 */
class StencilTestRenderer(context: Context) : Renderer {
    private var mContext = context
    private val type = 0
    private val mViewPos = floatArrayOf(0f, 0f, 4.5f, 1f)
    private val POSITION_COMPONENT_COUNT = 3
    private val TEXTURE__COMPONENT_COUNT = 2
    private val COMPONENT_PER_VERTEXT = (POSITION_COMPONENT_COUNT + TEXTURE__COMPONENT_COUNT)
    private val STRIDE = COMPONENT_PER_VERTEXT * BYTES_PER_FLOAT
    private var START_POSITION = 0
    private var START_TEXTURE = POSITION_COMPONENT_COUNT
    private val cubeVertices = floatArrayOf(
        //x,y,z,s,t
        //x,y,z是positions    //s,t是texture Coords
        -0.5f, -0.5f, -0.5f, 0.0f, 0.0f,
        0.5f, -0.5f, -0.5f, 1.0f, 0.0f,
        0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
        0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
        -0.5f, 0.5f, -0.5f, 0.0f, 1.0f,
        -0.5f, -0.5f, -0.5f, 0.0f, 0.0f,
        -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
        0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
        0.5f, 0.5f, 0.5f, 1.0f, 1.0f,
        0.5f, 0.5f, 0.5f, 1.0f, 1.0f,
        -0.5f, 0.5f, 0.5f, 0.0f, 1.0f,
        -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
        -0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
        -0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
        -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
        -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
        -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
        -0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
        0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
        0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
        0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
        0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
        0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
        0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
        -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
        0.5f, -0.5f, -0.5f, 1.0f, 1.0f,
        0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
        0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
        -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
        -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
        -0.5f, 0.5f, -0.5f, 0.0f, 1.0f,
        0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
        0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
        0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
        -0.5f, 0.5f, 0.5f, 0.0f, 0.0f,
        -0.5f, 0.5f, -0.5f, 0.0f, 1.0f
    )
    private val mMVPMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val modelMatrix = FloatArray(16)
    private var cubeTexture = 0
    private var mStencilShaderProgram: StencilShaderProgram? = null
    private var mColorShaderProgram: StencilShaderProgram? = null
    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        GLES20.glClearColor(0.9f, 0.7f, 0.3f, 1.0f)
        cubeTexture = loadTexture(mContext, R.mipmap.scenery)
        mStencilShaderProgram = StencilShaderProgram(
            mContext,
            R.raw.stencil_test_vertex,
            R.raw.stencil_test_fragment
        )
        mStencilShaderProgram?.useProgram()
        mColorShaderProgram = StencilShaderProgram(
            mContext,
            R.raw.single_color_vertex,
            R.raw.single_color_fragment
        )
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, width)
        GLES20.glDepthFunc(GLES20.GL_LESS)
        GLES20.glEnable(GLES20.GL_STENCIL_TEST)
        GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_KEEP, GLES20.GL_REPLACE)
        val ratio = width.toFloat() / height
        // 设置透视投影矩阵，近点是3，远点是7
        Matrix.frustumM(
            projectionMatrix,
            0, -ratio, ratio, -1f, 1f, 1f, 10f
        )
        Matrix.setLookAtM(
            viewMatrix, 0, mViewPos[0], mViewPos[1], mViewPos[2],
            0f, 0f, 0f,
            0f, 1.0f, 0.0f
        )
    }

    override fun onDrawFrame(gl: GL10) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        // 清屏
        GLES20.glClear(
            GLES20.GL_COLOR_BUFFER_BIT
                    or GLES20.GL_DEPTH_BUFFER_BIT
                    or GLES20.GL_STENCIL_BUFFER_BIT
        )
        //一直通过模板测试，模板值是1，写入
        GLES20.glStencilFunc(GLES20.GL_ALWAYS, 1, 0xFF)
        GLES20.glStencilMask(0xFF)
        //绘制大小立法体
        drawCube()//源物体对应的模板缓冲区位置写入1
        //模板缓冲区中模板值是1的地方不写入，不等于1的地方写入
        GLES20.glStencilFunc(GLES20.GL_NOTEQUAL, 1, 0xFF)
        GLES20.glStencilMask(0x00)
        GLES20.glDisable(GLES20.GL_DEPTH_TEST)
        //绘制大小立法体描边
        drawScaleCube()
        GLES20.glStencilMask(0xFF)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
    }

    private fun drawCube() {
        mStencilShaderProgram?.useProgram()
        // 传入顶点坐标
        val vertexBuffer: FloatBuffer = OpenGLUtil.createFloatBuffer(cubeVertices)
        //vertexBuffer.position(START_POSITION)//创建后默认初始位置为0
        GLES20.glVertexAttribPointer(
            mStencilShaderProgram?.getPositionAttributeLocation()!!,
            POSITION_COMPONENT_COUNT,
            GLES20.GL_FLOAT,
            false,
            STRIDE,
            vertexBuffer
        )
        GLES20.glEnableVertexAttribArray(mStencilShaderProgram?.getPositionAttributeLocation()!!)
        // 纹理坐标
        vertexBuffer.position(START_TEXTURE)
        GLES20.glVertexAttribPointer(
            mStencilShaderProgram?.getTextureCoordinatesAttribLocation()!!,
            TEXTURE__COMPONENT_COUNT,
            GLES20.GL_FLOAT,
            false,
            STRIDE,
            vertexBuffer
        )
        GLES20.glEnableVertexAttribArray(mStencilShaderProgram?.getTextureCoordinatesAttribLocation()!!)

        GLES20.glUniform1i(
            mStencilShaderProgram?.getTypeUniformLocation()!!,
            type
        ) //type为绘制类型（图像纹理或颜色）
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.translateM(modelMatrix, 0, -3.0f, -2.0f, -1.0f)//x水平方向移动，y垂直方向移动，z前后移动
        Matrix.multiplyMM(mMVPMatrix, 0, viewMatrix, 0, modelMatrix, 0)
        Matrix.multiplyMM(mMVPMatrix, 0, projectionMatrix, 0, mMVPMatrix, 0)
        //设置矩阵
        GLES20.glUniformMatrix4fv(
            mStencilShaderProgram?.getMvpMatrixUniformLocation()!!,
            1,
            false,
            mMVPMatrix,
            0
        )
        //绘制小立法体
        OpenGLUtil.bindTexture(mStencilShaderProgram?.getTextureUniformLocation()!!, cubeTexture, 0)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, cubeVertices.size / COMPONENT_PER_VERTEXT)
        //位置坐标
        vertexBuffer.position(START_POSITION)
        GLES20.glVertexAttribPointer(
            mStencilShaderProgram?.getPositionAttributeLocation()!!,
            POSITION_COMPONENT_COUNT,
            GLES20.GL_FLOAT,
            false,
            STRIDE,
            vertexBuffer
        )
        //纹理坐标
        vertexBuffer.position(POSITION_COMPONENT_COUNT)
        GLES20.glVertexAttribPointer(
            mStencilShaderProgram?.getTextureCoordinatesAttribLocation()!!,
            TEXTURE__COMPONENT_COUNT,
            GLES20.GL_FLOAT,
            false,
            STRIDE,
            vertexBuffer
        )
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.translateM(modelMatrix, 0, 1.0f, -1.0f, 2.0f)
        Matrix.multiplyMM(mMVPMatrix, 0, viewMatrix, 0, modelMatrix, 0)
        Matrix.multiplyMM(mMVPMatrix, 0, projectionMatrix, 0, mMVPMatrix, 0)
        GLES20.glUniformMatrix4fv(
            mStencilShaderProgram?.getMvpMatrixUniformLocation()!!,
            1,
            false,
            mMVPMatrix,
            0
        )
        //绘制大立法体
        OpenGLUtil.bindTexture(mStencilShaderProgram?.getTextureUniformLocation()!!, cubeTexture, 0)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, cubeVertices.size / COMPONENT_PER_VERTEXT)

        GLES20.glDisableVertexAttribArray(mStencilShaderProgram?.getPositionAttributeLocation()!!)
        GLES20.glDisableVertexAttribArray(mStencilShaderProgram?.getTextureCoordinatesAttribLocation()!!)
    }

    private fun drawScaleCube() {
        mColorShaderProgram?.useProgram()
        // 传入顶点坐标
        val vertexBuffer: FloatBuffer = OpenGLUtil.createFloatBuffer(cubeVertices)
        vertexBuffer.position(START_POSITION)
        GLES20.glVertexAttribPointer(
            mColorShaderProgram?.getPositionAttributeLocation()!!,
            POSITION_COMPONENT_COUNT,
            GLES20.GL_FLOAT,
            false,
            STRIDE,
            vertexBuffer
        )
        GLES20.glEnableVertexAttribArray(mColorShaderProgram?.getPositionAttributeLocation()!!)

        val scale = 1.1f//x,y,z必须等比缩放才能形成描边，且缩放数值不能小于1。如果小于1，缩放后的描边坐标会小于原立法体的坐标，导致描边不可见。
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.translateM(modelMatrix, 0, -3.0f, -2.0f, -1.0f)//x水平方向移动，y垂直方向移动，z前后移动
        Matrix.scaleM(modelMatrix, 0, scale, scale, scale)//x,y,z一起缩放scale倍
        Matrix.multiplyMM(mMVPMatrix, 0, viewMatrix, 0, modelMatrix, 0)
        Matrix.multiplyMM(mMVPMatrix, 0, projectionMatrix, 0, mMVPMatrix, 0)
        GLES20.glUniformMatrix4fv(
            mColorShaderProgram?.getMvpMatrixUniformLocation()!!,
            1,
            false,
            mMVPMatrix,
            0
        )
        //绘制小立法体描边
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, cubeVertices.size / COMPONENT_PER_VERTEXT)

        vertexBuffer.position(START_POSITION)
        GLES20.glVertexAttribPointer(
            mColorShaderProgram?.getPositionAttributeLocation()!!,
            POSITION_COMPONENT_COUNT,
            GLES20.GL_FLOAT,
            false,
            STRIDE,
            vertexBuffer
        )
        val scaleBig = 1.05f//不能小于1。如果小于1，缩放后的描边坐标会小于原立法体的坐标，导致描边不可见。
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.translateM(modelMatrix, 0, 1.0f, -1.0f, 2.0f)
        Matrix.scaleM(modelMatrix, 0, scaleBig, scaleBig, scaleBig)
        Matrix.multiplyMM(mMVPMatrix, 0, viewMatrix, 0, modelMatrix, 0)
        Matrix.multiplyMM(mMVPMatrix, 0, projectionMatrix, 0, mMVPMatrix, 0)
        GLES20.glUniformMatrix4fv(
            mColorShaderProgram?.getMvpMatrixUniformLocation()!!,
            1,
            false,
            mMVPMatrix,
            0
        )
        //绘制大立法体描边
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, cubeVertices.size / COMPONENT_PER_VERTEXT)
        GLES20.glDisableVertexAttribArray(mColorShaderProgram?.getPositionAttributeLocation()!!)
    }
}