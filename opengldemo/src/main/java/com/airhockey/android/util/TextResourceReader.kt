package com.airhockey.android.util

import android.content.Context
import android.content.res.Resources
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

private val TAG = "TextResourceReader"

class TextResourceReader {

    companion object {
        @JvmStatic
        fun readTextFileFromResource(context: Context, resourceId: Int): String? {
            var body = StringBuilder()
            try {
                var inputStream = context.resources.openRawResource(resourceId)
                var inputStreamReader = InputStreamReader(inputStream)
                var bufferedReader = BufferedReader(inputStreamReader)
                var nextLine: String? = null;
                while ((nextLine.let {
                        bufferedReader.readLine()
                    }) != null) {
                    body.append(nextLine)
                    body.append('\n')
                }
            } catch (e: IOException) {
                Log.e(TAG,"whx#could not open resource:" + resourceId)
            } catch (e: Resources.NotFoundException) {
                Log.e(TAG,"whx#resource not found:" + resourceId)
            }
            return null
        }
    }
}