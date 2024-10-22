package com.opengllib.data

import android.opengl.GLES30
import com.opengllib.util.BYTES_PER_FLOAT
import com.opengllib.util.BYTES_PER_SHORT
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

/**
 * EBO(Element Buffer Object)索引缓冲区对象管理类
 */
class EBOManager(vertexData: FloatArray, indices: ShortArray) {
    private var mVertexData = vertexData
    private var mFloatBuffer: FloatBuffer? = null
    private var mIndicesData = indices
    private var mShortBuffer: ShortBuffer? = null
    fun allocateBuffer() {
        //分配内存空间,每个浮点型占4字节空间
        mFloatBuffer = ByteBuffer.allocateDirect(mVertexData.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        mFloatBuffer?.put(mVertexData)
        mFloatBuffer?.position(0)
        // 索引缓冲区数据填充，类型必须为short/byte
        mShortBuffer = ByteBuffer.allocateDirect(mIndicesData.size * BYTES_PER_SHORT)
            .order(ByteOrder.nativeOrder())
            .asShortBuffer()
        mShortBuffer?.put(mIndicesData)
        mShortBuffer?.position(0)
    }

    fun bindEBO(eboIds: IntArray) {
        //1.生成EBO
        GLES30.glGenBuffers(eboIds.size, eboIds, 0)
        //2.绑定EBO
        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, eboIds[0])
    }

    fun bindVAO(vaoIds: IntArray) {
        //3.生成VAO
        GLES30.glGenVertexArrays(vaoIds.size, vaoIds, 0)
        //4.绑定VAO
        GLES30.glBindVertexArray(vaoIds[0])
    }

    fun bindVBO(vboIds: IntArray) {
        //5.生成VBO
        GLES30.glGenBuffers(vboIds.size, vboIds, 0)
        //6.绑定VBO
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vboIds[0])
    }

    fun putData() {
        //7.输入数据到VBO
        GLES30.glBufferData(
            GLES30.GL_ARRAY_BUFFER,
            mVertexData.size * BYTES_PER_FLOAT,
            mFloatBuffer,
            GLES30.GL_STATIC_DRAW
        )
        //8.绑定EBO并上传索引数据
        GLES30.glBufferData(
            GLES30.GL_ELEMENT_ARRAY_BUFFER,
            mIndicesData.size * BYTES_PER_SHORT,
            mShortBuffer,
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

    fun unbindEBO() {
        //解绑EBO
        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    fun deleteVBO(vboIds: IntArray) {
        GLES30.glDeleteBuffers(vboIds.size, vboIds, 0)
    }

    fun deleteVAO(vaoIds: IntArray) {
        GLES30.glDeleteVertexArrays(vaoIds.size, vaoIds, 0)
    }

    fun setVertexAttributePointer(attributeLocation: Int, size: Int, stride: Int) {
        //将顶点位置数据送入渲染管线. 用于将当前的顶点属性与顶点缓冲对象（VBO）关联起来
        GLES30.glVertexAttribPointer(
            attributeLocation,
            size,
            GLES30.GL_FLOAT,
            false,
            stride,
            0
        )
        //启用顶点位置属性
        GLES30.glEnableVertexAttribArray(attributeLocation)
    }
}