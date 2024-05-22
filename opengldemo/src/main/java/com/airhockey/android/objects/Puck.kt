package com.airhockey.android.objects

import com.airhockey.android.data.VertexArray
import com.airhockey.android.programs.ColorShaderProgram
import com.airhockey.android.util.Geometry

class Puck(radius: Float, height: Float, numPointsAroundPuck: Int) {
    val POSITION_COMPONENT_COUNT = 3;
    val radius = radius
    var height = height
    var generatedData = createPuck(
        Geometry.Companion.Cylinder(
            Geometry.Companion.Point(
                0f,
                0f,
                0f
            ), radius, height
        ), numPointsAroundPuck
    )

    val vertexArray = VertexArray(generatedData.vertexData)
    val drawList: List<DrawCommand> = generatedData.drawList
    fun bindData(colorProgram: ColorShaderProgram) {
        vertexArray.setVertexAttribPointer(
            0,
            colorProgram.getPositionAttribLocation(), POSITION_COMPONENT_COUNT, 0
        )
    }

    fun draw() {
        for (index in drawList.indices) {
            drawList.get(index).draw()
        }
    }
}
