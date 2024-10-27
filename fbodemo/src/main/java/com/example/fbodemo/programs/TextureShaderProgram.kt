package com.example.fbodemo.programs

import android.content.Context
import android.opengl.GLES20
import com.opengllib.programs.ShaderProgram

class TextureShaderProgram(
    context: Context, vertexShaderResourceId: Int?,
    fragmentShaderResourceId: Int?
) : ShaderProgram(context, vertexShaderResourceId, fragmentShaderResourceId) {
    fun setUniforms(textureId: Int) {
        GLES20.glActiveTexture(textureId)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
        GLES20.glUniform1f(uTextureUnitLocation, 0f)
    }
}