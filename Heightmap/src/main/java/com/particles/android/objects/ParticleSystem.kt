package com.particles.android.objects

import android.opengl.GLES20
import android.util.Log
import com.particles.android.BYTES_PER_FLOAT
import com.particles.android.data.VertexArray
import com.particles.android.programs.ParticleShaderProgram
import com.particles.android.util.Geometry

class ParticleSystem (maxParticleCount:Int){
    val TAG = "ParticleSystem"
    companion object {
        private const val POSITION_COMPONENT_COUNT = 3
        private const val COLOR_COMPONENT_COUNT = 3
        private const val VECTOR_COMPONENT_COUNT = 3
        private const val PARTICLE_START_TIME_COMPONENT_COUNT = 1
        private const val TOTAL_COMPONENT_COUNT = (POSITION_COMPONENT_COUNT
                + COLOR_COMPONENT_COUNT
                + VECTOR_COMPONENT_COUNT
                + PARTICLE_START_TIME_COMPONENT_COUNT)
        private const val BYTES_PER_FLOAT = 4
        private const val STRIDE = TOTAL_COMPONENT_COUNT * BYTES_PER_FLOAT
    }

    var particles: FloatArray? = null
    var vertexArray: VertexArray? = null
    private var maxParticleCount = 0
    var currentParticleCount = 0
    var nextParticle = 0

    init {
        particles = FloatArray(maxParticleCount * TOTAL_COMPONENT_COUNT)
        vertexArray = VertexArray(particles!!)
        this.maxParticleCount = maxParticleCount
    }

    fun addParticle(
        position: Geometry.Companion.Point,
        color: Int,
        direction: Geometry.Companion.Vector,
        particleStartTime: Float
    ) {
        val particleOffset = nextParticle * TOTAL_COMPONENT_COUNT
        var currentOffset = particleOffset
        nextParticle++
        if (currentParticleCount < maxParticleCount) {
            currentParticleCount++
        }
        if (nextParticle == maxParticleCount) {
            nextParticle = 0
        }

        particles!![currentOffset++] = position.x
        particles!![currentOffset++] = position.y
        particles!![currentOffset++] = position.z

        particles!![currentOffset++] = android.graphics.Color.red(color) / 255f
        particles!![currentOffset++] = android.graphics.Color.green(color) / 255f
        particles!![currentOffset++] = android.graphics.Color.blue(color) / 255f

        particles!![currentOffset++] = direction.x
        particles!![currentOffset++] = direction.y
        particles!![currentOffset++] = direction.z

        particles!![currentOffset++] = particleStartTime

        vertexArray?.updateBuffer(particles!!, particleOffset, TOTAL_COMPONENT_COUNT)
    }


    fun bindData(particleShaderProgram: ParticleShaderProgram) {
        var dataOffset = 0
        vertexArray?.setVertexAttribPointer(
            dataOffset,
            particleShaderProgram.getPositionAttributeLocation(),
            POSITION_COMPONENT_COUNT,
            STRIDE
        )
        dataOffset += POSITION_COMPONENT_COUNT
        vertexArray?.setVertexAttribPointer(
            dataOffset,
            particleShaderProgram.getColorAttributeLocation(),
            COLOR_COMPONENT_COUNT,
            STRIDE
        )
        dataOffset += COLOR_COMPONENT_COUNT
        vertexArray?.setVertexAttribPointer(
            dataOffset,
            particleShaderProgram.getDirectionVectorAttributeLocation(),
            VECTOR_COMPONENT_COUNT,
            STRIDE
        )
        dataOffset += VECTOR_COMPONENT_COUNT
        vertexArray?.setVertexAttribPointer(
            dataOffset,
            particleShaderProgram.getParticleStartTimeAttributeLocation(),
            PARTICLE_START_TIME_COMPONENT_COUNT,
            STRIDE
        )
    }

    fun draw(){
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, currentParticleCount)
    }
}