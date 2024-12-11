package com.example.renderyuv.program

import android.content.Context
import android.opengl.GLES30
import com.opengllib.programs.ShaderProgram

class YUVShaderProgram(
    context: Context, vertexShaderResourceId: Int?,
    fragmentShaderResourceId: Int?
) : ShaderProgram(
    context, vertexShaderResourceId,
    fragmentShaderResourceId
) {
    var vTextureYLocation = 0
    var vTextureULocation = 0
    var vTextureVLocation = 0

    init {
        vTextureYLocation = GLES30.glGetUniformLocation(program, "sampler_y")
        vTextureULocation = GLES30.glGetUniformLocation(program, "sampler_u")
        vTextureVLocation = GLES30.glGetUniformLocation(program, "sampler_v")
    }


    fun getTextureYLocation(): Int {
        return vTextureYLocation
    }

    fun getTextureULocation(): Int {
        return vTextureULocation
    }

    fun getTextureVLocation(): Int {
        return vTextureVLocation
    }
}