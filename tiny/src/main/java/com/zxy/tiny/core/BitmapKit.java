package com.zxy.tiny.core;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.util.Pair;
import android.util.TypedValue;

import com.zxy.tiny.Tiny;
import com.zxy.tiny.common.Conditions;
import com.zxy.tiny.common.TinyException;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Created by zhengxiaoyong on 2017/3/13.
 */
public final class BitmapKit {

    private static final int ALPHA_8_BYTES_PER_PIXEL = 1;

    private static final int ARGB_4444_BYTES_PER_PIXEL = 2;

    private static final int ARGB_8888_BYTES_PER_PIXEL = 4;

    private static final int RGB_565_BYTES_PER_PIXEL = 2;

    public static Pair<Integer, Integer> decodeDimensions(String filePath) throws Exception {
        boolean isExist = Conditions.fileIsExist(filePath);
        if (!isExist)
            return null;
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            return decodeDimensions(CompressKit.transformToByteArray(is));
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    //ignore...
                }
            }
        }
    }

    public static Pair<Integer, Integer> decodeDimensions(byte[] bytes) {
        if (bytes == null || bytes.length == 0)
            return null;
        BitmapFactory.Options options = CompressKit.getDefaultDecodeBoundsOptions();
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        return (options.outWidth == -1 || options.outHeight == -1) ?
                null : Pair.create(options.outWidth, options.outHeight);
    }

    public static Pair<Integer, Integer> decodeDimensions(int resId) throws Exception {
        //for drawable resource,get the original size of resources,without scaling.
        InputStream is = null;
        Resources resources = Tiny.getInstance().getApplication().getResources();
        try {
            is = resources.openRawResource(resId, new TypedValue());
            BitmapFactory.Options options = CompressKit.getDefaultDecodeBoundsOptions();
            BitmapFactory.decodeStream(is, null, options);
            return (options.outWidth == -1 || options.outHeight == -1) ?
                    null : Pair.create(options.outWidth, options.outHeight);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    //ignore...
                }
            }
        }
    }

    public static Pair<Integer, Integer> decodeDimensions(FileDescriptor fd) {
        if (fd == null)
            return null;
        BitmapFactory.Options options = CompressKit.getDefaultDecodeBoundsOptions();
        BitmapFactory.decodeFileDescriptor(fd, null, options);
        return (options.outWidth == -1 || options.outHeight == -1) ?
                null : Pair.create(options.outWidth, options.outHeight);
    }

    public static long getSizeInBytes(Bitmap bitmap) {
        if (bitmap == null)
            return 0L;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            try {
                return bitmap.getAllocationByteCount();
            } catch (Exception e) {
                //ignore...
            }
        }
        return bitmap.getByteCount();
    }

    public static long getBitmapSizeInBytes(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled())
            return 0L;
        Bitmap.Config config = bitmap.getConfig();
        switch (config) {
            case ARGB_8888:
                return ARGB_8888_BYTES_PER_PIXEL * getSizeInBytes(bitmap);
            case ALPHA_8:
                return ALPHA_8_BYTES_PER_PIXEL * getSizeInBytes(bitmap);
            case ARGB_4444:
                return ARGB_4444_BYTES_PER_PIXEL * getSizeInBytes(bitmap);
            case RGB_565:
                return RGB_565_BYTES_PER_PIXEL * getSizeInBytes(bitmap);
        }
        throw new TinyException.UnsupportedParamException("this bitmap config is not supported!");
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degree) {
        if (bitmap == null || bitmap.isRecycled())
            return bitmap;
        if (degree != 90 && degree != 180 && degree != 270)
            return bitmap;
        float pointX = bitmap.getWidth() / 2;
        float pointY = bitmap.getHeight() / 2;
        Matrix matrix = new Matrix();
        matrix.setRotate(degree, pointX, pointY);
        Bitmap result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.recycle();
        return result;
    }

}
