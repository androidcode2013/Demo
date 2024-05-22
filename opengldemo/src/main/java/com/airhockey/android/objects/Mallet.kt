package com.airhockey.android.objects

import com.airhockey.android.data.VertexArray
import com.airhockey.android.programs.ColorShaderProgram
import com.airhockey.android.util.Geometry

class Mallet() {
    val POSITION_COMPONENT_COUNT = 3
    var radius = 0f
    var height = 0f
    var generatedData : GeneratedData? = null
    var vertexArray : VertexArray? = null
    var drawList : List<DrawCommand>? = null

    constructor(radius: Float, height: Float, numPointsAroundMallet: Int) : this() {
        this.radius = radius
        this.height = height
        this.generatedData =
            createMallet(
                Geometry.Companion.Point(0f, 0f, 0f),
                radius,
                height,
                numPointsAroundMallet)
        this.vertexArray = VertexArray(generatedData!!.vertexData!!)
        this.drawList = generatedData!!.drawList
    }
    fun bindData(colorProgram: ColorShaderProgram) {
        vertexArray?.setVertexAttribPointer(
            0,
            colorProgram.getPositionAttribLocation(),
            POSITION_COMPONENT_COUNT,
            0
        )
    }

    fun draw() {
        for (index in drawList?.indices!!) {
            drawList?.get(index)?.draw()
        }

    }
}