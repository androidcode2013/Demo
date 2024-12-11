package com.example.fbodemo.programs

import android.content.Context
import android.opengl.GLES20
import com.opengllib.programs.ShaderProgram

class TextureShaderProgram(
    context: Context, vertexShaderResourceId: Int?,
    fragmentShaderResourceId: Int?
) : ShaderProgram(context, vertexShaderResourceId, fragmentShaderResourceId) {
    var uTextureUnit1Location = 0

    init {
        uTextureUnit1Location = GLES20.glGetUniformLocation(program, "u_TextureUnit1")
    }

    fun getTextureUniform1Location(): Int {
        return uTextureUnit1Location
    }

    fun setUniforms(uniformLocation: Int, x: Int) {
        //x为纹理单元索引
        GLES20.glUniform1i(uniformLocation, x)
    }

}