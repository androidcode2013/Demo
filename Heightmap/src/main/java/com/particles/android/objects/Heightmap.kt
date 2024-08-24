package com.particles.android.objects

import android.graphics.Bitmap
import android.graphics.Color
import android.opengl.GLES20
import android.util.Log
import com.particles.android.data.IndexBuffer
import com.particles.android.data.VertexBuffer
import com.particles.android.programs.HeightmapShaderProgram

class Heightmap(bitmap: Bitmap) {
    val TAG = "Heightmap"

    companion object {
        private const val POSITION_COMPONENT_COUNT = 3
    }

    private var width = 0
    private var height = 0
    private var numElements = 0
    private var vertexBuffer: VertexBuffer? = null
    private var indexBuffer: IndexBuffer? = null

    init {
        width = bitmap.width
        height = bitmap.height
        if (width * height > 65535) {
            Log.d(TAG, "Heightmap is too large for the index buffer.")
        }
        numElements = calculateNumElements()
        vertexBuffer = VertexBuffer(loadBitmapData(bitmap))
        indexBuffer = IndexBuffer(createIndexData())
    }

    private fun loadBitmapData(bitmap: Bitmap): FloatArray {
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        bitmap.recycle()

        var heightmapVertices = FloatArray(width * height * POSITION_COMPONENT_COUNT)
        var offset = 0
        for (row in 0 until height) {
            for (col in 0 until width) {
                val xPosition = (col.toFloat() / (width - 1).toFloat()) - 0.5f
                val yPosition = Color.red(pixels[(row * height) + col]).toFloat() / 255f
                val zPosition = row.toFloat() / (height - 1).toFloat() - 0.5f
                heightmapVertices[offset++] = xPosition
                heightmapVertices[offset++] = yPosition
                heightmapVertices[offset++] = zPosition
            }
        }
        return heightmapVertices
    }

    private fun calculateNumElements(): Int {
        return (width - 1) * (height - 1) * 2 * 3
    }

    private fun createIndexData(): ShortArray {
        var indexData = ShortArray(numElements)
        var offset = 0

        for (row in 0 until height - 1) {
            for (col in 0 until width - 1) {
                var topLeftIndexNum = (row * width + col).toShort()
                var topRightIndexNum = (row * width + col + 1).toShort()
                var bottomLeftIndexNum = ((row + 1) * width + col).toShort()
                var bottomRightIndexNum = ((row + 1) * width + col + 1).toShort()

                indexData[offset++] = topLeftIndexNum
                indexData[offset++] = bottomLeftIndexNum
                indexData[offset++] = topRightIndexNum

                indexData[offset++] = topRightIndexNum
                indexData[offset++] = bottomLeftIndexNum
                indexData[offset++] = bottomRightIndexNum
            }
        }
        return indexData
    }

    fun bindData(heightmapShaderProgram: HeightmapShaderProgram) {
        vertexBuffer?.setVertexAttribPointer(
            0, heightmapShaderProgram.getPositionAttributeLocation(),
            POSITION_COMPONENT_COUNT, 0
        )
    }

    fun draw() {
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBuffer?.getBufferId()!!)
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, numElements, GLES20.GL_UNSIGNED_SHORT, 0)
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0)
    }
}