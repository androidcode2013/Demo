package com.airhockey.android.objects

import android.opengl.GLES20.GL_TRIANGLE_FAN
import android.opengl.GLES20.GL_TRIANGLE_STRIP
import android.opengl.GLES20.glDrawArrays
import com.airhockey.android.util.Geometry

class ObjectBuilder(sizeInVertices: Int) {
    val FLOATS_PER_VERTEX = 3;
    val vertexData = FloatArray(sizeInVertices * FLOATS_PER_VERTEX);
    var offset = 0;
    var drawList = ArrayList<DrawCommand>()

        fun appendCircle(circle: Geometry.Companion.Circle, numPoints: Int) {
        var startVertex = offset / FLOATS_PER_VERTEX
        var numVertices = size0fCircleInVertices(numPoints)
        // Center point of fan
        vertexData[offset++] = circle.center.x
        vertexData[offset++] = circle.center.y
        vertexData[offset++] = circle.center.z
        //Fan around center point,<= is used because we want to generate
        // the point at the starting angle twice to complete the fan.

        for (i in 1 until numPoints step 1) {
            var angleInRadians =
                (i.toFloat() / numPoints.toFloat()) * (Math.PI * 2f)
            vertexData[offset++] =
                (circle.center.x + circle.radius * Math.cos(angleInRadians)).toFloat()
            vertexData[offset++] = circle.center.y;
            vertexData[offset++] =
                (circle.center.z + +circle.radius * Math.sin(angleInRadians)).toFloat()
        }
        drawList.add(object : DrawCommand {
            override fun draw() {
                glDrawArrays(GL_TRIANGLE_FAN, startVertex, numVertices)
            }
        })
    }

    fun appendOpenCylinder(cylinder: Geometry.Companion.Cylinder, numPoints: Int) {
        var startVertex = offset / FLOATS_PER_VERTEX
        val numVertices = size0fOpenCylinderInVertices(numPoints)
        val yStart = cylinder.center.y - (cylinder.height / 2f)
        val yEnd = cylinder.center.y + (cylinder.height / 2f)

        for (i in 0 until numPoints+1 step 1) {
            var angleInRadians = (i.toFloat() / numPoints.toFloat()) * (Math.PI * 2f)
            var xPosition = cylinder.center.x + cylinder.radius * Math.cos(angleInRadians)
            var zPosition = cylinder.center.z + cylinder.radius * Math.sin(angleInRadians)
            vertexData[offset++] = xPosition.toFloat()
            vertexData[offset++] = yStart
            vertexData[offset++] = zPosition.toFloat()

            vertexData[offset++] = xPosition.toFloat()
            vertexData[offset++] = yEnd
            vertexData[offset++] = zPosition.toFloat()
        }

        drawList.add(object : DrawCommand {
            override fun draw() {
                glDrawArrays(GL_TRIANGLE_STRIP, startVertex, numVertices)
            }
        })
    }

    fun build(): GeneratedData {
        return GeneratedData(vertexData, drawList)
    }
}

fun size0fCircleInVertices(numPoints: Int): Int {
    return 1 + (numPoints + 1)
}

fun size0fOpenCylinderInVertices(numPoints: Int): Int {
    return (numPoints + 1) * 2
}

fun createPuck(puck: Geometry.Companion.Cylinder, numPoints: Int): GeneratedData {
    var size = size0fCircleInVertices(numPoints) +
            size0fOpenCylinderInVertices(numPoints)
    var objectBuilder = ObjectBuilder(size)
    var puckTop = Geometry.Companion.Circle(
        puck.center.translateY(puck.height / 2f),
        puck.radius
    )
    objectBuilder.appendCircle(puckTop, numPoints)
    objectBuilder.appendOpenCylinder(puck, numPoints)
    return objectBuilder.build();
}

fun createMallet(
    center: Geometry.Companion.Point, radius: Float, height: Float,
    numPoints: Int
): GeneratedData {
    var size =
        size0fCircleInVertices(numPoints) * 2 + size0fOpenCylinderInVertices(numPoints) * 2
    var builder = ObjectBuilder(size)
    // First,generate the mallet base.
    var baseHeight = height * 0.25f
    var baseCircle = Geometry.Companion.Circle(center.translateY(-baseHeight), radius)
    var baseCylinder = Geometry.Companion.Cylinder(
        baseCircle.center.translateY(-baseHeight / 2f), radius, baseHeight
    )
    builder.appendCircle(baseCircle, numPoints)
    builder.appendOpenCylinder(
        baseCylinder,
        numPoints
    )
    var handleHeight = height * 0.75f
    var handleRadius = radius / 3f
    var handleCircle = Geometry.Companion.Circle(
        center.translateY(height * 0.5f),
        handleRadius
    )
    var handleCylinder = Geometry.Companion.Cylinder(
        handleCircle.center.translateY(-handleHeight / 2f),
        handleRadius,
        handleHeight
    )
    builder.appendCircle(handleCircle, numPoints);
    builder.appendOpenCylinder(handleCylinder, numPoints)
    return builder.build()
}

interface DrawCommand {
    fun draw()
}

class GeneratedData(vertexData: FloatArray, drawList: List<DrawCommand>) {
    var vertexData = vertexData
    var drawList = drawList
}

