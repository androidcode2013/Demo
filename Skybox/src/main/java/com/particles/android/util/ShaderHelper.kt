package com.particles.android.util

import android.opengl.GLES20.GL_COMPILE_STATUS
import android.opengl.GLES20.GL_FRAGMENT_SHADER
import android.opengl.GLES20.GL_LINK_STATUS
import android.opengl.GLES20.GL_VALIDATE_STATUS
import android.opengl.GLES20.GL_VERTEX_SHADER
import android.opengl.GLES20.glAttachShader
import android.opengl.GLES20.glCompileShader
import android.opengl.GLES20.glCreateProgram
import android.opengl.GLES20.glCreateShader
import android.opengl.GLES20.glDeleteProgram
import android.opengl.GLES20.glDeleteShader
import android.opengl.GLES20.glGetProgramInfoLog
import android.opengl.GLES20.glGetProgramiv
import android.opengl.GLES20.glGetShaderInfoLog
import android.opengl.GLES20.glGetShaderiv
import android.opengl.GLES20.glLinkProgram
import android.opengl.GLES20.glShaderSource
import android.opengl.GLES20.glValidateProgram
import android.util.Log
import java.security.CodeSource

private val TAG = "ShaderHelper"

class ShaderHelper {

    companion object {
        @JvmStatic
        fun bindProgram(vertexShaderSource: String, fragmentShaderSource: String): Int {
            var vertexShaderId = compileVertexShader(vertexShaderSource)
            var fragmentShaderId = compileFragementShader(fragmentShaderSource)
            var programObjectId = linkProgram(vertexShaderId, fragmentShaderId)
            return programObjectId
        }
    }
}

fun compileShader(type: Int, shaderCode: String): Int {
    var shaderObjectId = glCreateShader(type)//获取着色器对象
    if (shaderObjectId == 0) {
        Log.e(TAG, "can not create new shader.")
        return 0
    }
    glShaderSource(shaderObjectId, shaderCode)//上传源代码
    glCompileShader(shaderObjectId)
    val compileStatus = IntArray(1)
    glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0)
    Log.v(TAG, "shader info:" + glGetShaderInfoLog(shaderObjectId))
    if (compileStatus[0] == 0) {
        glDeleteShader(shaderObjectId)
        Log.e(TAG, "complie shader failed.")
        return 0
    }
    return shaderObjectId
}

fun compileVertexShader(shaderCode: String): Int {
    return compileShader(GL_VERTEX_SHADER, shaderCode)
}

fun compileFragementShader(shaderCode: String): Int {
    return compileShader(GL_FRAGMENT_SHADER, shaderCode)
}

fun linkProgram(vertexShaderId: Int, fragmentShaderId: Int): Int {
    var programObjectId = glCreateProgram()
    if (programObjectId == 0) {
        Log.e(TAG, "can not create new program.")
        return 0
    }
    glAttachShader(programObjectId, vertexShaderId)
    glAttachShader(programObjectId, fragmentShaderId)
    glLinkProgram(programObjectId)
    val linkStatus = IntArray(1)
    glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0)
    Log.d(TAG, "program info:" + glGetProgramInfoLog(programObjectId))
    if (linkStatus[0] == 0) {
        glDeleteProgram(programObjectId)
        Log.e(TAG, "link program failed.")
    }
    return programObjectId
}

fun validateProgram(programObjectId: Int): Boolean {
    glValidateProgram(programObjectId)
    val validateStatus = IntArray(1)
    glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0)
    Log.d(TAG, "validate program:" + glGetProgramInfoLog(programObjectId))
    return validateStatus[0] != 0
}

