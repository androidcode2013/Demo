package com.airhockey.android.objects

import android.opengl.GLES20.GL_TRIANGLE_FAN
import android.opengl.GLES20.glDrawArrays
import com.airhockey.android.BYTES_PER_FLOAT
import com.airhockey.android.POSITION_COMPONENT_COUNT
import com.airhockey.android.TEXTURE_COORDINATES_COMPONENT_COUNT
import com.airhockey.android.data.VertexArray
import com.airhockey.android.programs.TextureShaderProgram


private val STRIDE =
    (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT


class Table {
    private var VERTEX_DATA = floatArrayOf(
        //x,y,s,t
        //桌面三角形
        0.0f, 0.0f, 0.5f, 0.5f,
        -0.5f, -0.8f, 0.0f, 0.9f,
        0.5f, -0.8f, 1.0f, 0.9f,
        0.5f, 0.8f, 1.0f, 0.1f,
        -0.5f, 0.8f, 0.0f, 0.1f,
        -0.5f, -0.8f, 0.0f, 0.9f,
    )
    val vertexArray: VertexArray = VertexArray(VERTEX_DATA)

    fun bindData(textureProgram: TextureShaderProgram) {
        vertexArray.setVertexAttribPointer(
            0,
            textureProgram.getPositionAttribLocation(),
            POSITION_COMPONENT_COUNT,
            STRIDE
        )//把位置数据绑定到着色器属性aPosition上
        vertexArray.setVertexAttribPointer(
            POSITION_COMPONENT_COUNT,
            textureProgram.getTextureCoordinatesAttribLocation(),
            TEXTURE_COORDINATES_COMPONENT_COUNT,
            STRIDE
        )//绑定纹理坐标数据到着色器属性aTextureCoordinates上
    }

    fun draw(){
        glDrawArrays(GL_TRIANGLE_FAN,0,6)
    }
}