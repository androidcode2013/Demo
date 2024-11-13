package com.example.skyboxdemo.program

import android.content.Context
import android.opengl.GLES20
import com.opengllib.programs.ShaderProgram

class SkyBoxShaderProgram(
    context: Context, vertexShaderResourceId: Int?,
    fragmentShaderResourceId: Int?
) : ShaderProgram(
    context, vertexShaderResourceId,
    fragmentShaderResourceId
) {

    fun setUniforms(matrix: FloatArray, textureId: Int) {
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, textureId)
        GLES20.glUniform1i(uTextureUnitLocation, 0)
    }
}