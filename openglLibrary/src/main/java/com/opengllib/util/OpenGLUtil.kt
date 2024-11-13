package com.opengllib.util

import android.app.ActivityManager
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLES30
import android.opengl.GLES31
import android.opengl.GLES32
import android.opengl.GLSurfaceView.EGLContextFactory
import android.opengl.GLUtils
import android.os.Build
import android.text.TextUtils
import android.util.Log
import androidx.annotation.RawRes
import androidx.core.util.Consumer
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.egl.EGL10
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.egl.EGLContext
import javax.microedition.khronos.egl.EGLDisplay

/**
 *
 */
object OpenGLUtil {
    private val TAG = "OpenGLUtil"
    private const val SIZEOF_FLOAT = 4
    private const val SIZEOF_INT = 4
    private const val SIZEOF_SHORT = 2

    // 初始化失败
    const val GL_NOT_INIT = -1

    // 没有Texture
    const val GL_NOT_TEXTURE = -1
    private var OpenGLVersion: Int? = null
    fun init(context: Context) {
        val am: ActivityManager? =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (am != null) {
            val ver = am.deviceConfigurationInfo.reqGlEsVersion
            if (ver >= 0x30002) {
                //  OpenGL ES 3.2
                OpenGLVersion = 5
            } else if (ver >= 0x30001) {
                //  OpenGL ES 3.1
                OpenGLVersion = 4
            } else if (ver >= 0x30000) {
                //  OpenGL ES 3.0
                OpenGLVersion = 3
            } else if (ver >= 0x20000) {
                // OpenGL ES 2.0
                OpenGLVersion = 2
            } else {
                OpenGLVersion = 0
            }
        }
    }

    @JvmOverloads
    fun createFactory(listener: Consumer<Int?>? = null): EGLContextFactory {
        return ContextFactory(listener)
    }

    /**
     * 检查OpenGL ES版本
     * @param context context
     * @param version 版本 * 2:OpenGL ES 2.0
     *  * 3:OpenGL ES 3.0
     *  * 4:OpenGL ES 3.1
     *  * 5:OpenGL ES 3.2
     * @return 是否符合
     */
    fun checkOpenGL(context: Context, version: Int): Boolean {
        if (OpenGLVersion == null) {
            init(context)
        }
        return OpenGLVersion!! >= version
    }

    fun checkOpenGLES20(activity: Context): Boolean {
        return checkOpenGL(activity, 2)
    }

    fun checkOpenGLES30(activity: Context): Boolean {
        return checkOpenGL(activity, 3)
    }

    fun checkOpenGLES31(activity: Context): Boolean {
        return checkOpenGL(activity, 4)
    }

    fun createProgram(
        vertexSource: String?,
        fragmentSource: String?,
        attributes: Array<String?>?
    ): Int {
        return createProgram(vertexSource, fragmentSource, null, attributes)
    }

    /**
     * 创建program
     * @param vertexSource   vertexSource
     * @param fragmentSource fragmentSource
     * @return program
     */
    @JvmOverloads
    fun createProgram(
        vertexSource: String?, fragmentSource: String?, geometrySource: String? = null,
        attributes: Array<String?>? = null
    ): Int {
        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource)
        if (vertexShader == 0) {
            return 0
        }
        val pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource)
        if (pixelShader == 0) {
            return 0
        }
        var geometryShader = 0
        val loadGeometry =
            !TextUtils.isEmpty(geometrySource) && OpenGLVersion!! >= 5 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
        if (loadGeometry) {
            geometryShader = loadShader(GLES32.GL_GEOMETRY_SHADER, geometrySource)
            if (geometryShader == 0) {
                return 0
            }
        }
        var program = GLES20.glCreateProgram()
        checkGlError("glCreateProgram")
        if (program == 0) {
            Log.e(TAG, "Could not create program")
        }
        GLES20.glAttachShader(program, vertexShader)
        checkGlError("glAttachShader")
        GLES20.glAttachShader(program, pixelShader)
        checkGlError("glAttachShader")
        if (loadGeometry) {
            GLES20.glAttachShader(program, geometryShader)
            checkGlError("glAttachShader")
        }
        if (attributes != null) {
            val size = attributes.size
            for (i in 0 until size) {
                GLES20.glBindAttribLocation(program, i, attributes[i])
            }
        }
        GLES20.glLinkProgram(program)
        val linkStatus = IntArray(1)
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0)
        if (linkStatus[0] != GLES20.GL_TRUE) {
            Log.e(TAG, "Could not link program: ")
            Log.e(TAG, GLES20.glGetProgramInfoLog(program))
            GLES20.glDeleteProgram(program)
            program = 0
        }
        return program
    }

    /**
     * 加载Shader
     * @param shaderType shaderType
     * @param source     source
     * @return shader
     */
    fun loadShader(shaderType: Int, source: String?): Int {
        var shader = GLES20.glCreateShader(shaderType)
        checkGlError("glCreateShader type=$shaderType")
        GLES20.glShaderSource(shader, source)
        GLES20.glCompileShader(shader)
        val compiled = IntArray(1)
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0)
        if (compiled[0] == 0) {
            Log.e(
                TAG,
                "Could not compile shader " + shaderType + ":" + GLES20.glGetShaderInfoLog(
                    shader
                )
            )
            GLES20.glDeleteShader(shader)
            shader = 0
        }
        return shader
    }

    /**
     * 检查是否出错
     * @param op op
     */
    fun checkGlError(op: String) {
        val error = GLES20.glGetError()
        if (error != GLES20.GL_NO_ERROR) {
            val msg = op + ": glError 0x" + Integer.toHexString(error)
            Log.e(TAG, msg)
            throw RuntimeException(msg)
        }
    }

    /**
     * 创建IntBuffer
     * @param arr arr
     * @return IntBuffer
     */
    fun createIntBuffer(arr: IntArray): IntBuffer {
        // 初始化ByteBuffer，长度为arr数组的长度*4，因为一个int占4个字节
        val qbb = ByteBuffer.allocateDirect(arr.size * SIZEOF_INT)
        // 数组排列用nativeOrder
        qbb.order(ByteOrder.nativeOrder())
        val ib = qbb.asIntBuffer()
        ib.put(arr)
        ib.position(0)
        return ib
    }

    /**
     * 创建FloatBuffer
     * @param floatArray FloatArray
     * @return FloatBuffer
     */
    fun createFloatBuffer(floatArray: FloatArray): FloatBuffer {
        val bb = ByteBuffer.allocateDirect(floatArray.size * SIZEOF_FLOAT)
        bb.order(ByteOrder.nativeOrder())
        val fb = bb.asFloatBuffer()
        fb.put(floatArray)
        fb.position(0)
        return fb
    }

    /**
     * 创建FloatBuffer
     * @param data data
     * @return FloatBuffer
     */
    fun createFloatBuffer(data: ArrayList<Float>): FloatBuffer {
        val aar = FloatArray(data.size)
        for (i in aar.indices) {
            aar[i] = data[i]
        }
        return createFloatBuffer(aar)
    }

    /**
     * 创建ShortBuffer
     * @param arr arr
     * @return ShortBuffer
     */
    fun createShortBuffer(arr: ShortArray): ShortBuffer {
        val bb = ByteBuffer.allocateDirect(arr.size * SIZEOF_SHORT)
        bb.order(ByteOrder.nativeOrder())
        val sb = bb.asShortBuffer()
        sb.put(arr)
        sb.position(0)
        return sb
    }

    /**
     * 创建ShortBuffer
     * @param data data
     * @return ShortBuffer
     */
    fun createShortBuffer(data: ArrayList<Short>): ShortBuffer {
        val aar = ShortArray(data.size)
        for (i in aar.indices) {
            aar[i] = data[i]
        }
        return createShortBuffer(aar)
    }

    /**
     * 加载bitmap纹理
     * @param bitmap bitmap图片
     * @return int
     */
    @JvmOverloads
    fun createTextureNormal(bitmap: Bitmap?, withAlpha: Boolean = false): Int {
        val texture = IntArray(1)
        if (bitmap != null && !bitmap.isRecycled) {
            //生成纹理
            GLES20.glGenTextures(1, texture, 0)
            checkGlError("glGenTexture")
            //生成纹理
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0])
            GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR
            )
            GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR
            )
            GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
                if (withAlpha) GLES20.GL_CLAMP_TO_EDGE else GLES20.GL_REPEAT
            )
            GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                if (withAlpha) GLES20.GL_CLAMP_TO_EDGE else GLES20.GL_REPEAT
            )
            //根据以上指定的参数，生成一个2D纹理
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
            return texture[0]
        }
        return 0
    }

    /**
     * 绑定纹理
     * @param location    句柄
     * @param texture     纹理id
     * @param index       绑定的位置
     * @param textureType 纹理类型
     */
    @JvmOverloads
    fun bindTexture(
        location: Int,
        texture: Int,
        index: Int,
        textureType: Int = GLES20.GL_TEXTURE_2D
    ) {
        // 最多支持绑定32个纹理
        require(!(index > 31)) { "index must be no more than 31!" }
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + index)
        GLES20.glBindTexture(textureType, texture)
        GLES20.glUniform1i(location, index)
    }

    /**
     * 从资源文件中读取shader字符串
     * @param rawResId rawResId
     * @return string
     */
    fun getShaderFromResources(context: Context, @RawRes rawResId: Int): String? {
        var inputStream: InputStream? = null
        try {
            inputStream = context.resources.openRawResource(rawResId)
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        }
        return getShaderStringFromStream(inputStream)
    }

    /**
     * 从文件路径中读取shader字符串
     * @param filePath filePath
     * @return string
     */
    fun getShaderFromFile(filePath: String?): String? {
        if (TextUtils.isEmpty(filePath)) {
            return null
        }
        val file = File(filePath)
        if (file.isDirectory()) {
            return null
        }
        var inputStream: InputStream? = null
        try {
            inputStream = FileInputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return getShaderStringFromStream(inputStream)
    }

    /**
     * 从Assets文件夹中读取shader字符串
     * @param context context
     * @param path    shader相对路径
     * @return string
     */
    fun getShaderFromAssets(context: Context, path: String?): String? {
        var inputStream: InputStream? = null
        try {
            inputStream = context.resources.assets.open(path!!)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return getShaderStringFromStream(inputStream)
    }

    /**
     * 从输入流中读取shader字符创
     * @param stream input stream
     * @return string
     */
    private fun getShaderStringFromStream(stream: InputStream?): String? {
        if (stream == null) {
            return null
        }
        try {
            val reader = BufferedReader(InputStreamReader(stream))
            val builder = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                builder.append(line).append("\n")
            }
            reader.close()
            return builder.toString()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 创建帧缓冲
     * @param width  width
     * @param height height
     * @return texture,
     */
    fun createFrameBuffer(width: Int, height: Int): IntArray {
        val values = IntArray(1)
        // 纹理缓冲
        GLES20.glGenTextures(1, values, 0)
        val mOffscreenTexture = values[0] // expected > 0
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mOffscreenTexture)

        // 创建纹理存储。
        GLES20.glTexImage2D(
            GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0,
            GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null
        )

        // 设置参数。
        GLES20.glTexParameterf(
            GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
            GLES20.GL_LINEAR.toFloat()
        )
        GLES20.glTexParameterf(
            GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
            GLES20.GL_LINEAR.toFloat()
        )
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
            GLES20.GL_CLAMP_TO_EDGE
        )
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
            GLES20.GL_CLAMP_TO_EDGE
        )

        // 自定义帧缓冲
        GLES20.glGenFramebuffers(1, values, 0)
        val mFramebuffer = values[0] // expected > 0
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFramebuffer)

        // 深度缓冲
        GLES20.glGenRenderbuffers(1, values, 0)
        val mDepthBuffer = values[0] // expected > 0
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, mDepthBuffer)

        // 为深度缓冲区分配存储空间。
        GLES20.glRenderbufferStorage(
            GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16,
            width, height
        )

        // 将深度缓冲区和纹理（颜色缓冲区）附加到帧缓冲区对象。
        GLES20.glFramebufferRenderbuffer(
            GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT,
            GLES20.GL_RENDERBUFFER, mDepthBuffer
        )
        GLES20.glFramebufferTexture2D(
            GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
            GLES20.GL_TEXTURE_2D, mOffscreenTexture, 0
        )

        // 判断是否创建成功
        val status = GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER)
        if (status != GLES20.GL_FRAMEBUFFER_COMPLETE) {
            // 未创建成功
            throw RuntimeException("Framebuffer not complete, status=$status")
        }
        // 切换到默认缓冲
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
        return intArrayOf(mOffscreenTexture, mFramebuffer, mDepthBuffer)
    }

    /**
     * 创建帧缓冲
     * @param width  width
     * @param height height
     * @return texture,
     */
    fun createMSAAFrameBuffer(width: Int, height: Int): IntArray {
        val values = IntArray(1)
        var status: Int
        GLES20.glGenFramebuffers(1, values, 0)
        val framebuffer = values[0] // expected > 0
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, framebuffer)

        // 纹理缓冲
        GLES20.glGenTextures(1, values, 0)
        val textureColorBufferMultiSampled = values[0] // expected > 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            GLES20.glBindTexture(GLES31.GL_TEXTURE_2D_MULTISAMPLE, textureColorBufferMultiSampled)
            GLES31.glTexStorage2DMultisample(
                GLES31.GL_TEXTURE_2D_MULTISAMPLE, 4, GLES31.GL_RGBA8,
                width, height, true
            )
            //GLES20.glBindTexture(GLES31.GL_TEXTURE_2D_MULTISAMPLE, 0);
            GLES20.glFramebufferTexture2D(
                GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
                GLES31.GL_TEXTURE_2D_MULTISAMPLE, textureColorBufferMultiSampled, 0
            )
            //GLES31.glDrawBuffers(1, new int[]{ GLES31.GL_COLOR_ATTACHMENT0 }, 0);
        }
        status = GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER)
        if (status != GLES20.GL_FRAMEBUFFER_COMPLETE) {
            // 未创建成功
            throw RuntimeException("Framebuffer not complete, status=$status")
        }

        // rbo
        GLES20.glGenRenderbuffers(1, values, 0)
        val rbo = values[0] // expected > 0
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, rbo)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val param = IntArray(1)
            GLES30.glGetIntegerv(GLES30.GL_MAX_SAMPLES, param, 0)
            GLES31.glRenderbufferStorageMultisample(
                GLES20.GL_RENDERBUFFER, param[0], GLES32.GL_DEPTH24_STENCIL8,
                width, height
            )
        }
        //GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            GLES20.glFramebufferRenderbuffer(
                GLES20.GL_FRAMEBUFFER, GLES30.GL_DEPTH_STENCIL_ATTACHMENT,
                GLES20.GL_RENDERBUFFER, rbo
            )
        }
        status = GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER)
        if (status != GLES20.GL_FRAMEBUFFER_COMPLETE) {
            // 未创建成功
            throw RuntimeException("Framebuffer not complete, status=$status")
        }
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)

        // fbo
        GLES20.glGenFramebuffers(1, values, 0)
        val intermediateFBO = values[0] // expected > 0
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, intermediateFBO)
        GLES20.glGenTextures(1, values, 0)
        val screenTexture = values[0]
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, screenTexture)

        // 设置参数。
        GLES20.glTexParameterf(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_MIN_FILTER,
            GLES20.GL_LINEAR.toFloat()
        )
        GLES20.glTexParameterf(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_MAG_FILTER,
            GLES20.GL_LINEAR.toFloat()
        )
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_WRAP_S,
            GLES20.GL_CLAMP_TO_EDGE
        )
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_WRAP_T,
            GLES20.GL_CLAMP_TO_EDGE
        )
        GLES20.glTexImage2D(
            GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0,
            GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null
        )
        GLES20.glFramebufferTexture2D(
            GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
            GLES20.GL_TEXTURE_2D, screenTexture, 0
        )

        // 判断是否创建成功
        status = GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER)
        if (GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER) != GLES20.GL_FRAMEBUFFER_COMPLETE) {
            // 未创建成功
            throw RuntimeException("Framebuffer not complete, status=$status")
        }
        // 切换到默认缓冲
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
        return intArrayOf(
            framebuffer,
            textureColorBufferMultiSampled,
            intermediateFBO,
            rbo,
            screenTexture
        )
    }

    /**
     * 立方体贴图
     * @param context context
     * @param resIds  贴图集合，顺序是：
     *  * 右[GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X]
     *  * 左[GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_X]
     *  * 上[GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Y]
     *  * 下[GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y]
     *  * 后[GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Z]
     *  * 前[GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z]
     * @return int
     */
    fun createTextureCube(context: Context, resIds: IntArray?): Int {
        if (resIds != null && resIds.size >= 6) {
            val texture = IntArray(1)
            //生成纹理
            GLES20.glGenTextures(1, texture, 0)
            checkGlError("glGenTexture")
            //生成纹理
            GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, texture[0])
            GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_CUBE_MAP,
                GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR
            )
            GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_CUBE_MAP,
                GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR
            )
            GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_CUBE_MAP,
                GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_CLAMP_TO_EDGE
            )
            GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_CUBE_MAP,
                GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_CLAMP_TO_EDGE
            )
            if (OpenGLVersion!! > 2 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                GLES20.glTexParameteri(
                    GLES20.GL_TEXTURE_CUBE_MAP,
                    GLES30.GL_TEXTURE_WRAP_R,
                    GLES20.GL_CLAMP_TO_EDGE
                )
            }
            var bitmap: Bitmap?
            val options = BitmapFactory.Options()
            options.inScaled = false
            for (i in resIds.indices) {
                bitmap = BitmapFactory.decodeResource(
                    context.resources,
                    resIds[i], options
                )
                if (bitmap == null) {
                    Log.w(TAG, "Resource ID " + resIds[i] + " could not be decoded.")
                    GLES20.glDeleteTextures(1, texture, 0)
                    return 0
                }
                GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, bitmap, 0)
                bitmap.recycle()
            }
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
            return texture[0]
        }
        return 0
    }

    private class ContextFactory(private val listener: Consumer<Int?>?) :
        EGLContextFactory {
        override fun createContext(
            egl: EGL10,
            display: EGLDisplay,
            eglConfig: EGLConfig
        ): EGLContext {
            var context: EGLContext? = null
            var version: Int
            while (true) {
                version = if (OpenGLVersion == null) {
                    3
                } else {
                    when (OpenGLVersion) {
                        5, 4, 3 -> 3
                        else -> 2
                    }
                }
                try {
                    context = egl.eglCreateContext(
                        display, eglConfig, EGL10.EGL_NO_CONTEXT, intArrayOf(
                            EGL_CONTEXT_CLIENT_VERSION, version, EGL10.EGL_NONE
                        )
                    )
                } catch (ex: Exception) {
                    Log.e(TAG, ex.toString())
                }
                if (context == null || context === EGL10.EGL_NO_CONTEXT) {
                    version--
                    if (version < 2) {
                        break
                    }
                } else {
                    break
                }
            }
            listener?.accept(version)
            return context!!
        }

        override fun destroyContext(egl: EGL10, display: EGLDisplay, context: EGLContext) {
            if (!egl.eglDestroyContext(display, context)) {
                Log.d(TAG, "destroyContext false")
            }
        }

        companion object {
            private const val EGL_CONTEXT_CLIENT_VERSION = 0x3098
        }
    }
}

