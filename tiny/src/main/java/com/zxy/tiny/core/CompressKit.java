package com.zxy.tiny.core;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Pair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhengxiaoyong on 2017/3/12.
 */
public final class CompressKit {

    public static final int DEFAULT_QUALITY = 76;

    public static final Bitmap.Config DEFAULT_CONFIG = Bitmap.Config.ARGB_8888;

    public static final int DEFAULT_MAX_COMPRESS_SIZE = 1280;

    private static final int DEFAULT_DECODE_BUFFER_SIZE = 16 * 1024;

    public static BitmapFactory.Options getDefaultDecodeBoundsOptions() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inTempStorage = new byte[DEFAULT_DECODE_BUFFER_SIZE];
        return options;
    }

    public static BitmapFactory.Options getDefaultDecodeOptions() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inScaled = true;
        options.inMutable = true;
        options.inTempStorage = new byte[DEFAULT_DECODE_BUFFER_SIZE];
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // after and include kitkat,ashmem heap memory included in app total memory.
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                // decode the image into a 'purgeable' bitmap that lives on the ashmem heap.
                options.inPurgeable = true;
                // enable copy of of bitmap to enable purgeable decoding by filedescriptor.
                options.inInputShareable = true;
            } else {
                options.inPurgeable = false;
                options.inInputShareable = false;
            }
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            //optimize the bitmap display quality
            options.inDither = true;
        }
        return options;
    }

    public static Pair<Integer, Integer> getDeviceScreenSizeInPixels() {
        DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
        return Pair.create(dm.widthPixels, dm.heightPixels);
    }

    public static byte[] transformToByteArray(InputStream is) {
        if (is == null)
            return new byte[0];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int len = -1;
        try {
            while ((len = is.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                //ignore...
            }
        }
        return bos.toByteArray();
    }

    public static Bitmap.Config filterConfig(Bitmap.Config config) {
        if (config == null)
            return DEFAULT_CONFIG;
        switch (config) {
            case ARGB_8888:
            case ARGB_4444:
            case ALPHA_8:
                config = Bitmap.Config.ARGB_8888;
                break;
            case RGB_565:
                config = Bitmap.Config.RGB_565;
                break;
        }
        return config;
    }

}
