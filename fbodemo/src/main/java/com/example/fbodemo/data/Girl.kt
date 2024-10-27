package com.example.fbodemo.data

import android.opengl.GLES20
import com.example.fbodemo.programs.TextureShaderProgram
import com.opengllib.data.VertexArray
import com.opengllib.util.BYTES_PER_FLOAT

class Girl {
    private val VERTEX_DATA = floatArrayOf(
        //x,y,s,t
        //x,y为顶点位置坐标，s,t为纹理坐标
        0.0f, 0.0f, 0.5f, 0.5f,
        -0.5f, -0.8f, 0.0f, 0.9f,
        0.5f, -0.8f, 1.0f, 0.9f,
        0.5f, 0.8f, 1.0f, 0.1f,
        -0.5f, 0.8f, 0.0f, 0.1f,
        -0.5f, -0.8f, 0.0f, 0.9f,
    )

    private val vertexArray: VertexArray = VertexArray(VERTEX_DATA)
    private val POSITION_COMPONENT_COUNT = 2
    private val TEXTURE_COORDINATES_COMPONENT_COUNT = 2
    private val STRIDE =
        (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT

    fun setVertexAttribPointer(textureProgram: TextureShaderProgram) {
        vertexArray.setVertexAttribPointer(
            0,
            textureProgram.getPositionAttributeLocation(),
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

    fun draw() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 6)
    }
}