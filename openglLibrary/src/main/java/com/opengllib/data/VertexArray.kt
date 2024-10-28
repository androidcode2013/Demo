package com.opengllib.data

import android.opengl.GLES20.GL_FLOAT
import android.opengl.GLES20.glDisableVertexAttribArray
import android.opengl.GLES20.glEnableVertexAttribArray
import android.opengl.GLES20.glVertexAttribPointer
import com.opengllib.util.BYTES_PER_FLOAT
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
        //将顶点位置数据送入渲染管线
        glVertexAttribPointer(
            attributeLocation, componentCount, GL_FLOAT,
            false, stride, floatBuffer
        )
        //启用顶点位置属性
        glEnableVertexAttribArray(attributeLocation)
        floatBuffer.position(0)
    }

    fun disableVertexAttribPointer(attributeLocation: Int) {
        glDisableVertexAttribArray(attributeLocation)
    }

    fun updateBuffer(vertexData: FloatArray, start: Int, count: Int) {
        floatBuffer.position(start)
        floatBuffer.put(vertexData, start, count)
        floatBuffer.position(0)
    }
}