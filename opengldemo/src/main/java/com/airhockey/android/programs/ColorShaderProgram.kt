package com.airhockey.android.programs

import android.content.Context
import android.opengl.GLES20
import com.airhockey.android.R
import com.airhockey.android.util.TextResourceReader

class ColorShaderProgram : ShaderProgram {

      constructor(context: Context) : super(
        context,
        vertexShaderResourceSource =
        "uniform mat4 u_Matrix;\n" +
                "attribute vec4 a_Position;\n" +
                "attribute vec4 a_Color;\n" +
                "varying vec4 v_Color;\n" +
                "void main(){\n" +
                "v_Color = a_Color;\n" +
                "gl_Position = u_Matrix * a_Position;\n" +
                "gl_PointSize = 10.0;\n" +
                "}",
        fragmentShaderResourceSource =
        "precision mediump float;\n" +
                "varying vec4 v_Color;\n" +
                "void main(){\n" +
                "gl_FragColor =v_Color;\n" +
                "}"
    ) {
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX)
        aPositionLocation =
            GLES20.glGetAttribLocation(program, A_POSITION)//获取属性a_Position的位置
        aColorLocation = GLES20.glGetAttribLocation(program, A_COLOR)
    }

    fun setUniforms(matrix: FloatArray) {
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
    }

    fun getPositionAttribLocation(): Int {
        return aPositionLocation
    }

    fun getColorAttribLocation(): Int {
        return aColorLocation
    }
}