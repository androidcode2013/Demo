package com.opengllib.util

import android.content.Context
import android.graphics.Bitmap
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
import android.opengl.GLES30
import android.opengl.GLUtils
import android.opengl.GLUtils.texImage2D
import android.util.Log

private val TAG = "TextureHelper"


fun loadCubemap(context: Context, resourceIds: IntArray): Int {
    val textureObjectIds = IntArray(1)
    glGenTextures(1, textureObjectIds, 0)
    if (textureObjectIds[0] == 0) {
        glDeleteTextures(1, textureObjectIds, 0)
        return 0
    }
    GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
    glBindTexture(GLES30.GL_TEXTURE_CUBE_MAP, textureObjectIds[0])

    for (i in resourceIds.indices) {
        var bitmap = generateBitmap(context, resourceIds[i])
        GLUtils.texImage2D(GLES30.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, bitmap, 0)
        bitmap.recycle()
    }
    glTexParameteri(GLES30.GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    glTexParameteri(GLES30.GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    glTexParameteri(GLES30.GL_TEXTURE_CUBE_MAP, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE);
    glTexParameteri(GLES30.GL_TEXTURE_CUBE_MAP, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE);
    glTexParameteri(GLES30.GL_TEXTURE_CUBE_MAP, GLES30.GL_TEXTURE_WRAP_R, GLES30.GL_CLAMP_TO_EDGE);

    glBindTexture(GLES30.GL_TEXTURE_CUBE_MAP, 0);

    return textureObjectIds[0];
}

fun loadTexture(context: Context, resourceId: Int): Int {
    var bitmap = generateBitmap(context, resourceId)
    if (bitmap == null) {
        Log.d(TAG, "resourceId could not be decoded.")
        return 0
    }
    return getImgTexture(bitmap)
}

fun getImgTexture(bitmap: Bitmap): Int {
    val textureObjectIds = IntArray(1)
    glGenTextures(1, textureObjectIds, 0)
    if (textureObjectIds[0] == 0) {
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

fun generateBitmap(context: Context, resourceId: Int): Bitmap {
    return BitmapFactory.decodeResource(
        context.resources,
        resourceId,
        BitmapFactory.Options().apply {
            inScaled = false
        })
}