package com.example.vbodemo

import android.content.Context
import com.opengllib.programs.ShaderProgram

class VBOShaderProgram(
    context: Context, vertexShaderResourceId: Int?,
    fragmentShaderResourceId: Int?
) : ShaderProgram(
    context, vertexShaderResourceId,
    fragmentShaderResourceId
) {
}