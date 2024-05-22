package com.airhockey.android.programs

import android.content.Context
import android.opengl.GLES20
import com.airhockey.android.R
import com.airhockey.android.util.TextResourceReader

class ColorShaderProgram : ShaderProgram {

    var uColorLocation = 0;

    constructor(context: Context) : super(
        context,
        vertexShaderResourceSource =
        "uniform mat4 u_Matrix;\n" +
                "attribute vec4 a_Position;  \n" +
                "void main()                    \n" +
                "{                                \t  \t  \n" +
                "    gl_Position = u_Matrix * a_Position;            \n" +
                "}     ",
        fragmentShaderResourceSource =
        "precision mediump float; \n" +
                "      \t \t\t\t\t\t\t\t\t\n" +
                "uniform vec4 u_Color;      \t   \t\t\t\t\t\t\t\t\n" +
                "  \n" +
                "void main()                    \t\t\n" +
                "{                              \t\n" +
                "    gl_FragColor = u_Color;                                  \t\t\n" +
                "}"
    ) {
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX)
        aPositionLocation =
            GLES20.glGetAttribLocation(program, A_POSITION)//获取属性a_Position的位置
        uColorLocation = GLES20.glGetUniformLocation(program, U_COLOR)
    }

    fun setUniforms(matrix: FloatArray, r: Float, g: Float, b: Float) {
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
        GLES20.glUniform4f(uColorLocation, r, g, b, 1f)
    }

    fun getPositionAttribLocation(): Int {
        return aPositionLocation
    }
}