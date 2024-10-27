package com.opengllib.data

import android.opengl.GLES30

class FBOManager {
    private val TAG = "FBOManager"

    /**
     * 绑定FBO
     */
    fun bindFBO(fboIds: IntArray) {
        GLES30.glGenFramebuffers(fboIds.size, fboIds, 0)
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, fboIds[0]);
    }

    /**
     * 绑定RBO
     */
    fun bindRBO(rboIds: IntArray) {
        GLES30.glGenRenderbuffers(rboIds.size, rboIds, 0);
        GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER, rboIds[0]);
    }

    /**
     * 绑定纹理
     */
    fun bindTexture(textureIds: IntArray) {
        GLES30.glGenTextures(textureIds.size, textureIds, 0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureIds[0])
    }

    /**
     * 分配RBO缓冲对象
     */
    fun attachRBOToFBO(
        internalformat: Int,
        width: Int,
        height: Int,
        attachment: Int,
        rboIds: IntArray
    ) {
        //为当前的Renderbuffer分配空间，格式为GL_DEPTH_COMPONENT，顾名思义
        //这个Renderbuffer肯定是与depth test相关的。
        GLES30.glRenderbufferStorage(
            GLES30.GL_RENDERBUFFER,
            internalformat,
            width,
            height
        )
        //将当前的Renderbuffer与Framebuffer Object的GL_DEPTH_ATTACHMENT的连接点
        //连接起来。再顾名思义，肯定是与Depth test相关的。
        GLES30.glFramebufferRenderbuffer(
            GLES30.GL_FRAMEBUFFER,
            attachment,
            GLES30.GL_RENDERBUFFER,
            rboIds[0]
        )
    }

    /**
     * 创建纹理并附加到FBO上
     */
    fun attachTextureToFBO(textureIds: IntArray, width: Int, height: Int) {
        /**
         * 注意：我们在 glTexImage2D 的数据data 那里设置了 null，
         * 因为我们只需要内存地址，暂时不需要去填充它，然后环绕方式，我们也不用太去关注，
         * 大小也是。
         */
        GLES30.glTexImage2D(
            GLES30.GL_TEXTURE_2D, 0, GLES30.GL_RGB,
            width, height,
            0, GLES30.GL_RGB,
            GLES30.GL_UNSIGNED_BYTE, null
        )
        //附加到FBO
        GLES30.glFramebufferTexture2D(
            GLES30.GL_FRAMEBUFFER,
            GLES30.GL_COLOR_ATTACHMENT0,
            GLES30.GL_TEXTURE_2D,
            textureIds[0], 0
        )
    }

    fun setVertexAttributePointer(attributeLocation: Int, size: Int, stride: Int) {
        //将顶点位置数据送入渲染管线. 用于将当前的顶点属性与顶点缓冲对象（VBO）关联起来
        GLES30.glVertexAttribPointer(
            attributeLocation,
            size,
            GLES30.GL_FLOAT,
            false,
            stride,
            0
        )
        //启用顶点位置属性
        GLES30.glEnableVertexAttribArray(attributeLocation)
    }

    /**
     * 检查FBO的完整性
     */
    fun isCheckFBO(): Boolean {
        return (GLES30.glCheckFramebufferStatus(GLES30.GL_FRAMEBUFFER)
                == GLES30.GL_FRAMEBUFFER_COMPLETE)
    }

    /**
     * 解绑纹理
     */
    fun unbindTexture() {
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0)
    }

    /**
     * 解绑FBO
     */
    fun unbindFBO() {
        //修改帧缓冲为默认的，即屏幕
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0)
    }

}