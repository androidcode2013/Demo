package com.opengllib.util

class MatrixHelper {
    companion object {
        fun perspectiveM(
            m: FloatArray, yFovInDegrees: Float, aspect: Float,
            n: Float, f: Float
        ) {
            val angleInRadians = yFovInDegrees * Math.PI / 180.0;
            val a = 1.0 / Math.tan(angleInRadians / 2.0);
            m[0] = (a / aspect).toFloat();
            m[1] = 0f;
            m[2] = 0f;
            m[3] = 0f;

            m[4] = 0f;
            m[5] = a.toFloat();
            m[6] = 0f;
            m[7] = 0f;

            m[8] = 0f;
            m[9] = 0f;
            m[10] = -((f + n) / (f - n));
            m[11] = -1f;

            m[12] = 0f;
            m[13] = 0f;
            m[14] = -((2f * f * n) / (f - n));
            m[15] = 0f;
        }
    }
}

