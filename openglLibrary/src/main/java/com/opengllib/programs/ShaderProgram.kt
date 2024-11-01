package com.opengllib.programs

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES20.glUseProgram
import android.util.Log
import com.opengllib.util.ShaderHelper
import com.opengllib.util.TextResourceReader.Companion.readTextFileFromResource

open class ShaderProgram(
    context: Context, vertexShaderResourceId: Int?,
    fragmentShaderResourceId: Int?
) {
    companion object {
        const val TAG = "ShaderProgram"
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

    init {
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION)
        aColorLocation = GLES20.glGetAttribLocation(program, A_COLOR)
        aTextureCoordinatesLocation = GLES20.glGetAttribLocation(program, A_TEXTURE_COORDINATES)
        uColorLocation = GLES20.glGetUniformLocation(program, U_COLOR)
        uTextureUnitLocation = GLES20.glGetUniformLocation(program, U_TEXTURE_UNIT)
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX)
    }

    fun useProgram() {
        glUseProgram(program)
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

    fun getTextureCoordinatesAttribLocation(): Int {
        return aTextureCoordinatesLocation
    }
}