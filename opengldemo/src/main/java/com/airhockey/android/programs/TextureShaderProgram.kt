package com.airhockey.android.programs

import android.content.Context
import android.opengl.GLES20.GL_TEXTURE0
import android.opengl.GLES20.GL_TEXTURE_2D
import android.opengl.GLES20.glActiveTexture
import android.opengl.GLES20.glBindTexture
import android.opengl.GLES20.glGetAttribLocation
import android.opengl.GLES20.glGetUniformLocation
import android.opengl.GLES20.glUniform1f
import android.opengl.GLES20.glUniformMatrix4fv


class TextureShaderProgram : ShaderProgram {

    constructor(context: Context) : super(
        context, vertexShaderResourceSource =
        "uniform mat4 u_Matrix;\n" +
                "\n" +
                "attribute vec4 a_Position;  \n" +
                "attribute vec2 a_TextureCoordinates;\n" +
                "\n" +
                "varying vec2 v_TextureCoordinates;\n" +
                "\n" +
                "void main()                    \n" +
                "{                            \n" +
                "    v_TextureCoordinates = a_TextureCoordinates;\t  \t  \n" +
                "    gl_Position = u_Matrix * a_Position;    \n" +
                "}          ",
            fragmentShaderResourceSource =
            "precision mediump float; \n" +
                    "      \t \t\t\t\t\n" +
                    "uniform sampler2D u_TextureUnit;      \t \t\t\t\t\t\t\t\n" +
                    "varying vec2 v_TextureCoordinates;      \t   \t\t\t\t\t\t\t\t\n" +
                    "  \n" +
                    "void main()                    \t\t\n" +
                    "{                              \t\n" +
                    "    gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates);                           \t\t\n" +
                    "}"
    ) {
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX)
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT)

        aPositionLocation = glGetAttribLocation(program, A_POSITION)
        aTextureCoordinatesLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES)
    }

    fun setUniforms(matrix: FloatArray, textureId: Int) {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
        glActiveTexture(GL_TEXTURE0)//GL_TEXTURE0
        glBindTexture(GL_TEXTURE_2D, textureId)
        glUniform1f(uTextureUnitLocation, 0f)
    }

    fun getPositionAttribLocation(): Int {
        return aPositionLocation
    }

    fun getTextureCoordinatesAttribLocation(): Int {
        return aTextureCoordinatesLocation
    }
}