package com.opengllib.data

import android.opengl.GLES30

class FBOManager : BaseManager() {
    private val TAG = "FBOManager"
    /**
     * 生成FBO
     */
    fun genFBO(fboIds: IntArray) {
        GLES30.glGenFramebuffers(fboIds.size, fboIds, 0)
    }
    /**
     * 绑定FBO
     */
    fun bindFBO(fboIds: IntArray) {
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, fboIds[0]);
    }

    /**
     * 生成RBO
     */
    fun genRBO(rboIds: IntArray) {
        GLES30.glGenRenderbuffers(rboIds.size, rboIds, 0);
    }

    /**
     * 绑定RBO
     */
    fun bindRBO(rboIds: IntArray) {
        GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER, rboIds[0]);
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
        //设置采样，拉伸方式
        GLES30.glTexParameterf(
            GLES30.GL_TEXTURE_2D,
            GLES30.GL_TEXTURE_MIN_FILTER,
            GLES30.GL_NEAREST.toFloat()
        )
        GLES30.glTexParameterf(
            GLES30.GL_TEXTURE_2D,
            GLES30.GL_TEXTURE_MAG_FILTER,
            GLES30.GL_LINEAR.toFloat()
        )
        GLES30.glTexParameterf(
            GLES30.GL_TEXTURE_2D,
            GLES30.GL_TEXTURE_WRAP_S,
            GLES30.GL_MIRRORED_REPEAT.toFloat()
        )
        GLES30.glTexParameterf(
            GLES30.GL_TEXTURE_2D,
            GLES30.GL_TEXTURE_WRAP_T,
            GLES30.GL_MIRRORED_REPEAT.toFloat()
        )
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

    /**
     * 检查FBO的完整性
     */
    fun isCheckFBO(): Boolean {
        return (GLES30.glCheckFramebufferStatus(GLES30.GL_FRAMEBUFFER)
                == GLES30.GL_FRAMEBUFFER_COMPLETE)
    }

    /**
     * 解绑FBO
     */
    fun unbindFBO() {
        //修改帧缓冲为默认的，即屏幕
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0)
    }

    /**
     * 解绑RBO
     */
    fun unbindRBO() {
        GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER, 0)
    }


    fun deleteFBO(fboIds: IntArray) {
        GLES30.glDeleteFramebuffers(fboIds.size, fboIds, 0)
    }
}