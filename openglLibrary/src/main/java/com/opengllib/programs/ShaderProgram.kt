package com.opengllib.programs

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES20.glUseProgram
import android.opengl.GLES30
import com.opengllib.util.ShaderHelper
import com.opengllib.util.TextResourceReader.Companion.readTextFileFromResource
import java.nio.Buffer

open class ShaderProgram(
    context: Context, vertexShaderResourceId: Int?,
    fragmentShaderResourceId: Int?
) {
    companion object {
        const val TAG = "ShaderProgram"
        const val U_MVP_MATRIX = "u_MVPMatrix"
        const val U_MATRIX = "u_Matrix"
        const val U_TIME = "u_Time"
        const val U_COLOR = "u_Color"
        const val U_TEXTURE_UNIT = "u_TextureUnit"
        const val A_POSITION = "a_Position"
        const val A_COLOR = "a_Color"
        const val A_DIRECTION_VECTOR = "a_DirectionVector"
        const val A_PARTICLE_START_TIME = "a_ParticleStartTime"
        const val A_TEXTURE_COORDINATES = "a_TextureCoordinates"
    }

    var program = ShaderHelper.bindProgram(
        readTextFileFromResource(context, vertexShaderResourceId!!)!!,
        readTextFileFromResource(context, fragmentShaderResourceId!!)!!
    )
    var aPositionLocation = 0
    var aColorLocation = 0
    var aTextureCoordinatesLocation = 0
    var uColorLocation = 0
    var uTextureUnitLocation = 0
    var uMatrixLocation = 0
    var uMVPMatrix = 0

    init {
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION)
        aColorLocation = GLES20.glGetAttribLocation(program, A_COLOR)
        aTextureCoordinatesLocation = GLES20.glGetAttribLocation(program, A_TEXTURE_COORDINATES)
        uColorLocation = GLES20.glGetUniformLocation(program, U_COLOR)
        uTextureUnitLocation = GLES20.glGetUniformLocation(program, U_TEXTURE_UNIT)
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX)
        uMVPMatrix = GLES20.glGetUniformLocation(program, U_MVP_MATRIX)
    }

    fun useProgram() {
        glUseProgram(program)
    }

    fun getMvpMatrixUniformLocation(): Int {
        return uMVPMatrix
    }

    fun getPositionAttributeLocation(): Int {
        return aPositionLocation
    }

    fun getColorAttributeLocation(): Int {
        return aColorLocation
    }

    fun getColorUniformLocation(): Int {
        return uColorLocation
    }

    fun getTextureUniformLocation(): Int {
        return uTextureUnitLocation
    }

    fun getTextureCoordinatesAttribLocation(): Int {
        return aTextureCoordinatesLocation
    }

    fun setUniforms(uniformLocation: Int) {
        GLES20.glUniform1f(uniformLocation, 0f)
    }

    fun bindTexture(textureId: Int) {
        GLES20.glActiveTexture(textureId)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
    }

    fun enableVertexAttributePointer(
        attributeLocation: Int,
        size: Int,
        type: Int,
        normalize: Boolean,
        stride: Int,
        ptr: Buffer
    ) {
        //将顶点位置数据送入渲染管线. 用于将当前的顶点属性与顶点缓冲对象（VBO）关联起来
        GLES30.glVertexAttribPointer(
            attributeLocation,
            size,
            type,
            normalize,
            stride,
            ptr
        )
        //启用顶点位置属性
        GLES30.glEnableVertexAttribArray(attributeLocation)
    }

    fun draw(mode: Int, first: Int, count: Int) {
        GLES30.glDrawArrays(mode, first, count)
    }
}