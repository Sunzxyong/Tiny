package com.zxy.libjpegturbo;

import android.graphics.Bitmap;

/**
 * Created by zhengxiaoyong on 2017/3/4.
 */
public class JpegTurboCompressor {

    public static boolean compress(Bitmap bitmap, String outfile, int quality) {
        return nativeCompress(bitmap, outfile, quality, true);
    }

    public static String getVersion() {
        return "libjpeg-turbo api version : " + getLibjpegTurboVersion() + ", libjpeg api version : " + getLibjpegVersion();
    }

    static {
        System.loadLibrary("tiny");
    }

    private static native boolean nativeCompress(Bitmap bitmap, String outfile, int quality, boolean optimize);

    private static native int getLibjpegTurboVersion();

    private static native int getLibjpegVersion();

}
