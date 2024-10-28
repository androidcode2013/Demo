package com.opengllib.data

import android.opengl.GLES30

/**
 * 基础管理类
 */

open class BaseManager {
    /**
     * 生成纹理
     */
    fun genTexture(textureIds: IntArray) {
        GLES30.glGenTextures(textureIds.size, textureIds, 0)
    }
    /**
     * 绑定纹理
     */
    fun bindTexture(textureIds: IntArray) {
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureIds[0])
    }
    /**
     * 解绑纹理
     */
    fun unbindTexture() {
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0)
    }
}