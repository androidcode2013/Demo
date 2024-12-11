package com.example.renderyuv.util

import android.content.Context
import java.io.ByteArrayOutputStream

class FileUtil {
}

fun getAssertData(context: Context, fileName: String): ByteArray? {
    var inputStream = context.getAssets().open(fileName)
    val bos = ByteArrayOutputStream()
    val data = ByteArray(1024)
    var len = -1
    while (inputStream.read(data).also { len = it } != -1) {
        bos.write(data, 0, len)
    }
    return bos.toByteArray()
}