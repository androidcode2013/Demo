package com.example.scissordemo

import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView.Renderer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class ScissorRenderer(context: Context) : Renderer {
    private var mScreenWidth = 0
    private var mScreenHeight = 0
    private var mScissorWidth = 1000
    private var mScissorHeight = 800

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
        mScreenWidth = width
        mScreenHeight = height
        mScissorWidth = mScreenWidth / 2
        mScissorHeight = mScreenHeight / 2
    }

    override fun onDrawFrame(gl: GL10?) {
        //设置全屏默认颜色
        GLES30.glClearColor(0.0f, 0.0f, 1.0f, 0.0f)
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
        //开启裁剪测试
        GLES30.glEnable(GLES30.GL_SCISSOR_TEST)
        //设置屏幕中间红色裁剪区域
        GLES30.glScissor(
            mScreenWidth / 2 - mScissorWidth / 2,
            mScreenHeight / 2 - mScissorHeight / 2,
            mScissorWidth,
            mScissorHeight
        )
        GLES30.glClearColor(1.0f, 0.0f, 0.0f, 0.0f)//设置裁剪区颜色为红色
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)//开启清屏，执行裁剪(把屏幕颜色清除成当前设置清除颜色)
        //设置绿色裁剪区域
        GLES30.glScissor(
            mScreenWidth / 2 - mScissorWidth / 4,
            mScreenHeight / 2 - mScissorHeight / 4,
            mScissorWidth / 2,
            mScissorHeight / 2
        )
        GLES30.glClearColor(0.0f, 1.0f, 0.0f, 0.0f)// 设置清屏颜色为绿色
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
        //关闭裁剪测试
        GLES30.glDisable(GLES30.GL_SCISSOR_TEST)
    }
}