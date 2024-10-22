package com.opengllib.data

import android.opengl.GLES30
import com.opengllib.util.BYTES_PER_FLOAT
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class VAOManager(vertexData: FloatArray) {
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

    fun bindVAO(vaoIds: IntArray) {
        //1.生成VAO
        GLES30.glGenVertexArrays(vaoIds.size, vaoIds, 0)
        //2.绑定VAO
        GLES30.glBindVertexArray(vaoIds[0])
    }

    fun bindVBO(vboIds: IntArray) {
        //3.生成VBO
        GLES30.glGenBuffers(vboIds.size, vboIds, 0)
        //4.绑定VBO
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vboIds[0])
    }

    fun putDataTOVBO() {
        //5.输入数据到VBO
        GLES30.glBufferData(
            GLES30.GL_ARRAY_BUFFER,
            mVertexData.size * BYTES_PER_FLOAT,
            mFloatBuffer,
            GLES30.GL_STATIC_DRAW
        )
    }

    fun unbindVBO() {
        //解绑VBO
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0)
    }

    fun unbindVAO() {
        //解绑VAO
        GLES30.glBindVertexArray(0)
    }

    fun deleteVBO(vboIds: IntArray) {
        GLES30.glDeleteBuffers(vboIds.size, vboIds, 0)
    }

    fun deleteVAO(vaoIds: IntArray) {
        GLES30.glDeleteVertexArrays(vaoIds.size, vaoIds, 0)
    }

    fun setVertexAttributePointer(attributeLocation: Int, size: Int, stride: Int) {
        //4.将顶点位置数据送入渲染管线. 用于将当前的顶点属性与顶点缓冲对象（VBO）关联起来
        GLES30.glVertexAttribPointer(
            attributeLocation,
            size,
            GLES30.GL_FLOAT,
            false,
            stride,
            0
        )
        //5.启用顶点位置属性
        GLES30.glEnableVertexAttribArray(attributeLocation)
    }
}