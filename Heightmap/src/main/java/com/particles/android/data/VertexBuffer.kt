package com.particles.android.data

import android.opengl.GLES20
import android.util.Log
import java.nio.ByteBuffer
import java.nio.ByteOrder

class VertexBuffer(vertexData: FloatArray) {
    val TAG = "VertexBuffer"
    private var bufferId = 0
    companion object{
        const val BYTES_PER_FLOAT = 4
    }
    init {
        val buffers = IntArray(1)
        GLES20.glGenBuffers(buffers.size, buffers, 0)
        if (buffers[0] == 0) {
            Log.d(TAG, "can not create a new vertex buffer object.")
        }
        bufferId = buffers[0]
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0])
        val vertexArray = ByteBuffer
            .allocateDirect(vertexData.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertexData)
        vertexArray.position(0)
        GLES20.glBufferData(
            GLES20.GL_ARRAY_BUFFER,
            vertexArray.capacity() * BYTES_PER_FLOAT,
            vertexArray,
            GLES20.GL_STATIC_DRAW
        )
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
    }

    fun setVertexAttribPointer(
        dataOffset: Int,
        attributeLocation: Int,
        componentCount: Int,
        stride: Int
    ) {
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferId)
        GLES20.glVertexAttribPointer(
            attributeLocation,
            componentCount,
            GLES20.GL_FLOAT,
            false,
            stride,
            dataOffset
        )
        GLES20.glEnableVertexAttribArray(attributeLocation)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0)
    }
}