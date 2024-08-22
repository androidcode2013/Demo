package com.particles.android.programs

import android.content.Context
import android.opengl.GLES20.glUseProgram
import android.util.Log
import com.particles.android.util.ShaderHelper
import com.particles.android.util.TextResourceReader.Companion.readTextFileFromResource

val TAG = "ShaderProgram"
val U_MATRIX = "u_Matrix"
val U_TIME = "u_Time"
val U_TEXTUREUNIT = "u_TextureUnit"
val A_POSITION = "a_Position"
val A_COLOR = "a_Color"
val A_DIRECTION_VECTOR = "a_DirectionVector"
val A_PARTICLE_START_TIME = "a_ParticleStartTime"

open class ShaderProgram(
    context: Context, vertexShaderResourceId: Int?,
    fragmentShaderResourceId: Int?
) {
    var program = ShaderHelper.bindProgram(
        readTextFileFromResource(context, vertexShaderResourceId!!)!!,
        readTextFileFromResource(context, fragmentShaderResourceId!!)!!
    )

    fun useProgram() {
        glUseProgram(program)
    }
}