package com.example.renderyuv

import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView.Renderer
import com.example.renderyuv.program.YUVShaderProgram
import com.opengllib.data.VertexArray
import com.opengllib.util.BYTES_PER_FLOAT
import java.nio.ByteBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class YUVRenderer(context: Context) : Renderer {
    private var mContext = context
    private var mShaderProgram: YUVShaderProgram? = null
    private val textures = IntArray(3)
    private var yBuffer: ByteBuffer? = null
    private var uBuffer: ByteBuffer? = null
    private var vBuffer: ByteBuffer? = null
    private var yuvWidth = 0
    private var yuvHeight = 0
    private val POINT_RECT_DATA = floatArrayOf(
        // 前三个数字为顶点坐标(x, y, z)，后两个数字为纹理坐标(s, t)
        // 第一个三角形
        0.8f, 0.5f, 0f, 1f, 0f,
        0.8f, -0.5f, 0f, 1f, 1f,
        -0.8f, -0.5f, 0f, 0f, 1f,
        // 第二个三角形
        0.8f, 0.5f, 0f, 1f, 0f,
        -0.8f, -0.5f, 0f, 0f, 1f,
        -0.8f, 0.5f, 0f, 0f, 0f
    )
    private val POSITION_COMPONENT_COUNT = 3
    private val TEXTURE_COORDINATES_COMPONENT_COUNT = 2
    private val STRIDE =
        (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT
    private var mVertexArray: VertexArray? = null


    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES30.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
        mShaderProgram = YUVShaderProgram(
            mContext,
            R.raw.yuvrender_vertex_shader,
            R.raw.yuvrender_fragment_shader
        )
        mShaderProgram?.useProgram()

        mVertexArray = VertexArray(POINT_RECT_DATA)
        mVertexArray?.setVertexAttribPointer(
            0,
            mShaderProgram!!.getPositionAttributeLocation(),
            POSITION_COMPONENT_COUNT,
            STRIDE
        )
        mVertexArray?.setVertexAttribPointer(
            POSITION_COMPONENT_COUNT,
            mShaderProgram!!.getTextureCoordinatesAttribLocation(),
            TEXTURE_COORDINATES_COMPONENT_COUNT,
            STRIDE
        )
        createTextures()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        if (yBuffer == null || uBuffer == null || vBuffer == null) {
            return;
        }
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
        //1.加载纹理y
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0); //激活纹理0
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textures[0]); //绑定纹理
        GLES30.glTexImage2D(
            GLES30.GL_TEXTURE_2D, 0, GLES30.GL_LUMINANCE, yuvWidth,
            yuvHeight, 0, GLES30.GL_LUMINANCE, GLES30.GL_UNSIGNED_BYTE, yBuffer
        )// 赋值
        GLES30.glUniform1i(
            mShaderProgram!!.getTextureYLocation(),
            0
        ); // sampler_y的location=0, 把纹理0赋值给sampler_y
        //2.加载纹理u
        GLES30.glActiveTexture(GLES30.GL_TEXTURE1);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textures[1]);
        GLES30.glTexImage2D(
            GLES30.GL_TEXTURE_2D, 0, GLES30.GL_LUMINANCE, yuvWidth / 2,
            yuvHeight / 2, 0, GLES30.GL_LUMINANCE, GLES30.GL_UNSIGNED_BYTE, uBuffer
        );
        GLES30.glUniform1i(
            mShaderProgram!!.getTextureULocation(),
            1
        ); // sampler_u的location=1, 把纹理1赋值给sampler_u

        // 3.加载纹理v
        GLES30.glActiveTexture(GLES30.GL_TEXTURE2);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textures[2]);
        GLES30.glTexImage2D(
            GLES30.GL_TEXTURE_2D, 0, GLES30.GL_LUMINANCE, yuvWidth / 2,
            yuvHeight / 2, 0, GLES30.GL_LUMINANCE, GLES30.GL_UNSIGNED_BYTE, vBuffer
        );
        GLES30.glUniform1i(
            mShaderProgram!!.getTextureVLocation(),
            2
        ); // sampler_v的location=2, 把纹理1赋值给sampler_v
        // 4.绘制
        GLES30.glDrawArrays(
            GLES30.GL_TRIANGLES,
            0,
            POINT_RECT_DATA.size / (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT)
        );
    }


    fun createTextures() {
        //创建纹理
        GLES30.glGenTextures(textures.size, textures, 0)
        for (i in 0..2) {
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textures[i])
            //纹理环绕
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_REPEAT)
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_REPEAT)
            //纹理过滤
            GLES30.glTexParameteri(
                GLES30.GL_TEXTURE_2D,
                GLES30.GL_TEXTURE_MIN_FILTER,
                GLES30.GL_NEAREST
            )
            GLES30.glTexParameteri(
                GLES30.GL_TEXTURE_2D,
                GLES30.GL_TEXTURE_MAG_FILTER,
                GLES30.GL_LINEAR
            )
            //解绑纹理对象
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0)
        }
    }

    fun setYuvData(i420: ByteArray, width: Int, height: Int) {
        yBuffer?.clear();
        uBuffer?.clear();
        vBuffer?.clear();
        // 该函数多次被调用的时，不要每次都new，可以设置为全局变量缓存起来
        var y = ByteArray(width * height)
        var u = ByteArray(width * height / 4)
        var v = ByteArray(width * height / 4)
        System.arraycopy(i420, 0, y, 0, y.size);
        System.arraycopy(i420, y.size, u, 0, u.size);
        System.arraycopy(i420, y.size + u.size, v, 0, v.size);
        yBuffer = ByteBuffer.wrap(y);
        uBuffer = ByteBuffer.wrap(u);
        vBuffer = ByteBuffer.wrap(v);
        yuvWidth = width;
        yuvHeight = height;
    }

}