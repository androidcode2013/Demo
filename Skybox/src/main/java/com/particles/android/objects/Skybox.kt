package com.particles.android.objects

import android.opengl.GLES20
import com.particles.android.data.VertexArray
import com.particles.android.programs.SkyboxShaderProgram
import java.nio.ByteBuffer

class Skybox {
    companion object {
        private const val POSITION_COMPONENT_COUNT = 3
    }

    private var vertexArray: VertexArray = VertexArray(
        floatArrayOf(
            -1f, 1f, 1f,
            1f, 1f, 1f,
            -1f, -1f, 1f,
            1f, -1f, 1f,
            -1f, 1f, -1f,
            1f, 1f, -1f,
            -1f, -1f, -1f,
            1f, -1f, -1f
        )
    )
    private var indexArray: ByteBuffer = ByteBuffer.allocateDirect(6 * 6).put(
        byteArrayOf(
            //front
            1, 3, 0,
            0, 3, 2,
            //back
            4, 6, 5,
            5, 6, 7,
            //left
            0, 2, 4,
            4, 2, 6,
            //right
            5, 7, 1,
            1, 7, 3,
            //top
            5, 1, 4,
            4, 1, 0,
            //bottom
            6, 2, 7,
            7, 2, 3
        )
    )

    init {
        indexArray.position(0)
    }

    fun bindData(skyboxShaderProgram: SkyboxShaderProgram) {
        vertexArray.setVertexAttribPointer(
            0, skyboxShaderProgram.getPositionAttributeLocation(),
            POSITION_COMPONENT_COUNT, 0
        )
    }

    fun draw() {
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, 36, GLES20.GL_UNSIGNED_BYTE, indexArray)
    }
}