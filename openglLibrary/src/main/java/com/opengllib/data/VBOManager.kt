package com.opengllib.data

import android.opengl.GLES20
import com.opengllib.util.BYTES_PER_FLOAT
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class VBOManager(vertexData: FloatArray) {
    private var mVertexData = vertexData
    private var mFloatBuffer: FloatBuffer? = null

    fun allocateBuffer() {
        //分配内存空间,每个浮点型占4字节空间
        mFloatBuffer = ByteBuffer.allocateDirect(mVertexData.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        mFloatBuffer?.put(mVertexData)
        mFloatBuffer?.position(0)
    }

    fun bindVBO(vboIds: IntArray) {
        //1.生成缓冲区对象
        GLES20.glGenBuffers(vboIds.size, vboIds, 0)
        //2.绑定缓冲区
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboIds[0])
        //3.输入数据到缓冲区
        GLES20.glBufferData(
            GLES20.GL_ARRAY_BUFFER,
            mVertexData.size * BYTES_PER_FLOAT,
            mFloatBuffer,
            GLES20.GL_STATIC_DRAW
        )
    }

    fun unbindVBO() {
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
    }

    fun deleteVBO(vboIds: IntArray) {
        GLES20.glDeleteBuffers(vboIds.size, vboIds, 0)
    }

    fun setVertexAttributePointer(attributeLocation: Int, size: Int, stride: Int) {
        //4.将顶点位置数据送入渲染管线
        GLES20.glVertexAttribPointer(
            attributeLocation,
            size,
            GLES20.GL_FLOAT,
            false,
            stride,
            0
        )
        //5.启用顶点位置属性
        GLES20.glEnableVertexAttribArray(attributeLocation)
    }
}