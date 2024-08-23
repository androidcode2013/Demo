package com.particles.android.programs

import android.content.Context
import android.opengl.GLES20
import com.particles.android.R

class ParticleShaderProgram(
    context: Context, vertexShaderResourceId: Int?,
    fragmentShaderResourceId: Int?
) : ShaderProgram(context, vertexShaderResourceId, fragmentShaderResourceId) {

    //uniform location
    var uMatrixLocation = 0
    var uTimeLocation = 0
    var uTextureUnitLocation = 0

    //attribute location
    var aPositionLocation = 0
    var aColorLocation = 0
    var aDirectionVectorLocation = 0
    var aParticleStartTimeLocation = 0

    constructor(context: Context) : this(
        context,
        R.raw.particle_vertex_shader,
        R.raw.particle_fragment_shader
    ) {
        //retrieve uniform location for the shader program
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX)
        uTimeLocation = GLES20.glGetUniformLocation(program, U_TIME)
        uTextureUnitLocation = GLES20.glGetUniformLocation(program, U_TEXTURE_UNIT)
        //retrieve attribute location for the shader program
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION)
        aColorLocation = GLES20.glGetAttribLocation(program, A_COLOR)
        aDirectionVectorLocation = GLES20.glGetAttribLocation(program, A_DIRECTION_VECTOR)
        aParticleStartTimeLocation = GLES20.glGetAttribLocation(program, A_PARTICLE_START_TIME)
    }

    fun setUniforms(matrix: FloatArray, elapsedTime: Float, textureId: Int){
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
        GLES20.glUniform1f(uTimeLocation, elapsedTime)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
        GLES20.glUniform1i(uTextureUnitLocation, 0)
    }

    fun getPositionAttributeLocation(): Int{
        return aPositionLocation
    }
    fun getColorAttributeLocation(): Int{
        return aColorLocation
    }
    fun getDirectionVectorAttributeLocation (): Int{
        return aDirectionVectorLocation
    }
    fun getParticleStartTimeAttributeLocation(): Int{
        return aParticleStartTimeLocation
    }
}