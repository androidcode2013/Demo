package com.airhockey.android.programs

import android.content.Context
import android.opengl.GLES20.glUseProgram
import com.airhockey.android.util.ShaderHelper

val U_MATRIX = "u_Matrix"
val U_TEXTURE_UNIT = "u_TextureUnit"

val A_POSITION = "a_Position"
val A_COLOR = "a_Color"
val A_TEXTURE_COORDINATES = "a_TextureCoordinates"
val U_COLOR = "u_Color"

open class ShaderProgram(
    context: Context, vertexShaderResourceSource: String?,
    fragmentShaderResourceSource: String?
) {

    var uMatrixLocation = 0
    var uTextureUnitLocation = 0

    var aPositionLocation = 0
    var aColorLocation = 0
    var aTextureCoordinatesLocation = 0

    var program = ShaderHelper.bindProgram(
        vertexShaderResourceSource!!, fragmentShaderResourceSource!!
    )

    fun useProgram() {
        glUseProgram(program)
    }
}