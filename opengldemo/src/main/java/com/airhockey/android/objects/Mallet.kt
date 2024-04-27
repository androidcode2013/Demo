package com.airhockey.android.objects

import android.opengl.GLES20
import com.airhockey.android.BYTES_PER_FLOAT
import com.airhockey.android.COLOR_COMPONENT_COUNT
import com.airhockey.android.POSITION_COMPONENT_COUNT
import com.airhockey.android.data.VertexArray
import com.airhockey.android.programs.ColorShaderProgram

private val STRIDE =
    (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT

class Mallet {
    private var VERTEX_DATA = floatArrayOf(
        //x,y,r,g,b
        //木槌
        0.0f, -0.4f, 0.0f, 0.0f, 1.0f,
        0.0f, 0.4f, 1.0f, 0.0f, 0.0f
    )

    val vertexArray: VertexArray = VertexArray(VERTEX_DATA)

    fun bindData(textureProgram: ColorShaderProgram) {
        vertexArray.setVertexAttribPointer(
            0,
            textureProgram.getPositionAttribLocation(),
            POSITION_COMPONENT_COUNT,
            STRIDE
        )//把位置数据绑定到着色器属性aPosition上
        vertexArray.setVertexAttribPointer(
            POSITION_COMPONENT_COUNT,
            textureProgram.getColorAttribLocation(),
            COLOR_COMPONENT_COUNT,
            STRIDE
        )//绑定纹理坐标数据到着色器属性aTextureCoordinates上
    }

    fun draw(){
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 2)
    }
}