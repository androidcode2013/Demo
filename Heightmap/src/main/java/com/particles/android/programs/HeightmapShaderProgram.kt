package com.particles.android.programs

import android.content.Context
import android.opengl.GLES20
import com.particles.android.R

class HeightmapShaderProgram(context: Context) : ShaderProgram(
    context, R.raw.heightmap_vertex_shader,
    R.raw.heightmap_fragment_shader
) {
    //attribute location
    var aPositionLocation = 0
    var uMatrixLocation = 0
    init {
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX)
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION)
    }
    fun setUniforms(matrix: FloatArray){
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
    }
    fun getPositionAttributeLocation(): Int{
        return aPositionLocation
    }
}