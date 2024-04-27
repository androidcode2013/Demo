package com.airhockey.android.data

import android.opengl.GLES20.GL_FLOAT
import android.opengl.GLES20.glEnableVertexAttribArray
import android.opengl.GLES20.glVertexAttribPointer
import com.airhockey.android.BYTES_PER_FLOAT
import java.nio.ByteBuffer
import java.nio.ByteOrder

class VertexArray(vertexData: FloatArray) {

    val floatBuffer = ByteBuffer
        .allocateDirect(vertexData.size * BYTES_PER_FLOAT)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer()
        .put(vertexData)

    fun setVertexAttribPointer(
        dataOffSet: Int, attributeLocation: Int,
        componentCount: Int, stride: Int
    ) {
        floatBuffer.position(dataOffSet)
        glVertexAttribPointer(
            attributeLocation, componentCount, GL_FLOAT,
            false, stride, floatBuffer
        )
        glEnableVertexAttribArray(attributeLocation)
        floatBuffer.position(0)
    }
}