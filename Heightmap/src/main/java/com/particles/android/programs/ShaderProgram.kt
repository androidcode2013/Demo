package com.particles.android.programs

import android.content.Context
import android.opengl.GLES20.glUseProgram
import android.util.Log
import com.particles.android.util.ShaderHelper
import com.particles.android.util.TextResourceReader.Companion.readTextFileFromResource

open class ShaderProgram(
    context: Context, vertexShaderResourceId: Int?,
    fragmentShaderResourceId: Int?
) {
    companion object{
        const val TAG = "ShaderProgram"
        const val U_MATRIX = "u_Matrix"
        const val U_TIME = "u_Time"
        const val U_TEXTURE_UNIT = "u_TextureUnit"
        const val A_POSITION = "a_Position"
        const val A_COLOR = "a_Color"
        const val A_DIRECTION_VECTOR = "a_DirectionVector"
        const val A_PARTICLE_START_TIME = "a_ParticleStartTime"
    }
    var program = ShaderHelper.bindProgram(
        readTextFileFromResource(context, vertexShaderResourceId!!)!!,
        readTextFileFromResource(context, fragmentShaderResourceId!!)!!
    )

    fun useProgram() {
        glUseProgram(program)
    }
}