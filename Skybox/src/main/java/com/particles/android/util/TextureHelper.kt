package com.particles.android.util

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20.GL_LINEAR
import android.opengl.GLES20.GL_LINEAR_MIPMAP_LINEAR
import android.opengl.GLES20.GL_TEXTURE_2D
import android.opengl.GLES20.GL_TEXTURE_MAG_FILTER
import android.opengl.GLES20.GL_TEXTURE_MIN_FILTER
import android.opengl.GLES20.glBindTexture
import android.opengl.GLES20.glDeleteTextures
import android.opengl.GLES20.glGenTextures
import android.opengl.GLES20.glGenerateMipmap
import android.opengl.GLES20.glTexParameteri
import android.opengl.GLUtils.texImage2D
import android.util.Log

private val TAG = "TextureHelper"

class TextureHelper {

}

fun loadTexture(context: Context, resourceId: Int): Int {
    val textureObjectIds = IntArray(1)
    glGenTextures(1, textureObjectIds, 0)
    if (textureObjectIds[0] == 0) return 0
    var options = BitmapFactory.Options();
    options.inScaled = false
    var bitmap = BitmapFactory.decodeResource(context.resources, resourceId, options)
    if (bitmap == null) {
        Log.d(TAG, "resourceId could not be decoded.")
        glDeleteTextures(1, textureObjectIds, 0)
        return 0
    }
    glBindTexture(GL_TEXTURE_2D, textureObjectIds[0])
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
    texImage2D(GL_TEXTURE_2D, 0, bitmap, 0)
    glGenerateMipmap(GL_TEXTURE_2D)
    bitmap.recycle()
    glBindTexture(GL_TEXTURE_2D, 0)
    return textureObjectIds[0]
}