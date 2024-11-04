package com.example.stencildemo.program

import android.content.Context
import android.opengl.GLES20
import com.opengllib.programs.ShaderProgram

class StencilShaderProgram(
    context: Context,
    vertexShaderResourceId: Int?,
    fragmentShaderResourceId: Int?
) : ShaderProgram(
    context,
    vertexShaderResourceId,
    fragmentShaderResourceId
) {

    companion object {
        const val U_TYPE = "u_Type"
    }

    var uType = 0

    init {
        uType = GLES20.glGetUniformLocation(program, U_TYPE)
    }


    fun getTypeUniformLocation(): Int {
        return uType
    }
}