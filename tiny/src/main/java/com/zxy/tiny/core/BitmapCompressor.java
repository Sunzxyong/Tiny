package com.zxy.tiny.core;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Pair;
import android.util.TypedValue;

import com.zxy.tiny.Tiny;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Created by zhengxiaoyong on 2017/3/13.
 */
public class BitmapCompressor {

    public static Bitmap compress(Bitmap bitmap, Tiny.BitmapCompressOptions options, boolean isViewMode) {
        if (bitmap == null || bitmap.isRecycled())
            return null;

        if (options == null)
            options = new Tiny.BitmapCompressOptions();

        int compressWidth = options.width;
        int compressHeight = options.height;

        Bitmap result;

        Pair<Integer, Integer> screenPair = CompressKit.getDeviceScreenSizeInPixels();

        if (isViewMode) {
            if (screenPair.second >= CompressKit.DEFAULT_MAX_COMPRESS_SIZE ||
                    screenPair.first >= CompressKit.DEFAULT_MAX_COMPRESS_SIZE) {
                result = matrixCompress(bitmap, CompressKit.DEFAULT_MAX_COMPRESS_SIZE, false);
            } else {
                result = matrixCompress(bitmap, screenPair.second, false);
            }
        } else {
            result = matrixCompress(bitmap, CompressKit.DEFAULT_MAX_COMPRESS_SIZE, false);
        }

        if (result == null)
            return null;

        if (compressWidth > 0 && compressHeight > 0) {
            result = customCompress(result, compressWidth, compressHeight, false);
        }

        return result;
    }

    public static Bitmap compress(byte[] bytes, Tiny.BitmapCompressOptions options, boolean isViewMode) {
        if (bytes == null || bytes.length == 0)
            return null;

        if (options == null)
            options = new Tiny.BitmapCompressOptions();

        Pair<Integer, Integer> pair = BitmapKit.decodeDimensions(bytes);
        if (pair == null)
            return null;

        int bitmapWidth = pair.first;
        int bitmapHeight = pair.second;

        int compressWidth = options.width;
        int compressHeight = options.height;

        Bitmap result;

        Pair<Integer, Integer> screenPair = CompressKit.getDeviceScreenSizeInPixels();

        if (isViewMode) {
            if (screenPair.second >= CompressKit.DEFAULT_MAX_COMPRESS_SIZE ||
                    screenPair.first >= CompressKit.DEFAULT_MAX_COMPRESS_SIZE) {
                int sampleSize = computeSampleSize(bitmapWidth, bitmapHeight, CompressKit.DEFAULT_MAX_COMPRESS_SIZE);
                result = matrixCompress(sampleCompress(bytes, sampleSize, options), CompressKit.DEFAULT_MAX_COMPRESS_SIZE, true);
            } else {
                int sampleSize = computeSampleSize(bitmapWidth, bitmapHeight, screenPair.second);
                result = matrixCompress(sampleCompress(bytes, sampleSize, options), screenPair.second, true);
            }
        } else {
            int sampleSize = computeSampleSize(bitmapWidth, bitmapHeight, CompressKit.DEFAULT_MAX_COMPRESS_SIZE);
            result = matrixCompress(sampleCompress(bytes, sampleSize, options), CompressKit.DEFAULT_MAX_COMPRESS_SIZE, true);
        }

        if (result == null)
            return null;

        if (compressWidth > 0 && compressHeight > 0) {
            result = customCompress(result, compressWidth, compressHeight, true);
        }

        //processing bitmap degree.
        if (ExifCompat.isJpeg(bytes)) {
            int orientation = ExifCompat.getOrientation(bytes);
            result = BitmapKit.rotateBitmap(result, orientation);
        }

        return result;
    }

    public static Bitmap compress(int resId, Tiny.BitmapCompressOptions options, boolean isViewMode) {
        if (options == null)
            options = new Tiny.BitmapCompressOptions();

        Pair<Integer, Integer> pair = BitmapKit.decodeDimensions(resId);
        if (pair == null)
            return null;

        int bitmapWidth = pair.first;
        int bitmapHeight = pair.second;

        int compressWidth = options.width;
        int compressHeight = options.height;

        Bitmap result;

        Pair<Integer, Integer> screenPair = CompressKit.getDeviceScreenSizeInPixels();

        if (isViewMode) {
            if (screenPair.second >= CompressKit.DEFAULT_MAX_COMPRESS_SIZE ||
                    screenPair.first >= CompressKit.DEFAULT_MAX_COMPRESS_SIZE) {
                int sampleSize = computeSampleSize(bitmapWidth, bitmapHeight, CompressKit.DEFAULT_MAX_COMPRESS_SIZE);
                result = matrixCompress(sampleCompress(resId, sampleSize, options), CompressKit.DEFAULT_MAX_COMPRESS_SIZE, true);
            } else {
                int sampleSize = computeSampleSize(bitmapWidth, bitmapHeight, screenPair.second);
                result = matrixCompress(sampleCompress(resId, sampleSize, options), screenPair.second, true);
            }
        } else {
            int sampleSize = computeSampleSize(bitmapWidth, bitmapHeight, CompressKit.DEFAULT_MAX_COMPRESS_SIZE);
            result = matrixCompress(sampleCompress(resId, sampleSize, options), CompressKit.DEFAULT_MAX_COMPRESS_SIZE, true);
        }

        if (result == null)
            return null;

        if (compressWidth > 0 && compressHeight > 0) {
            result = customCompress(result, compressWidth, compressHeight, true);
        }

        //processing bitmap degree.
        InputStream is = null;
        Resources resources = Tiny.getInstance().getApplication().getResources();
        try {
            is = resources.openRawResource(resId, new TypedValue());
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int len = -1;
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            os.close();

            if (ExifCompat.isJpeg(os.toByteArray())) {
                int orientation = ExifCompat.getOrientation(os.toByteArray());
                result = BitmapKit.rotateBitmap(result, orientation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    //ignore...
                }
            }
        }

        return result;
    }

    private static Bitmap customCompress(Bitmap source, int targetWidth, int targetHeight, boolean recycle) {
        Matrix matrix = new Matrix();

        float bitmapWidth = source.getWidth();
        float bitmapHeight = source.getHeight();

        float bitmapAspect = bitmapWidth / bitmapHeight;
        float viewAspect = (float) targetWidth / (float) targetHeight;

        if (bitmapAspect > viewAspect) {
            float scale = targetHeight / bitmapHeight;
            if (scale < .9F || scale > 1F) {
                matrix.setScale(scale, scale);
            } else {
                matrix = null;
            }
        } else {
            float scale = targetWidth / bitmapWidth;
            if (scale < .9F || scale > 1F) {
                matrix.setScale(scale, scale);
            } else {
                matrix = null;
            }
        }

        Bitmap temp;
        if (matrix != null) {
            temp = Bitmap.createBitmap(source, 0, 0,
                    source.getWidth(), source.getHeight(), matrix, true);
        } else {
            temp = source;
        }

        if (recycle && temp != source) {
            source.recycle();
        }

        int dx = Math.max(0, temp.getWidth() - targetWidth);
        int dy = Math.max(0, temp.getHeight() - targetHeight);

        Bitmap result = Bitmap.createBitmap(
                temp,
                dx / 2,
                dy / 2,
                targetWidth,
                targetHeight);

        if (result != temp && (recycle || temp != source)) {
            temp.recycle();
        }

        return result;
    }

    private static Bitmap sampleCompress(byte[] bytes, int sampleSize, Tiny.BitmapCompressOptions options) {
        BitmapFactory.Options decodeOptions = CompressKit.getDefaultDecodeOptions();
        decodeOptions.inPreferredConfig = options.config;
        decodeOptions.inSampleSize = sampleSize;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, decodeOptions);
    }

    public static Bitmap sampleCompress(int resId, int sampleSize, Tiny.BitmapCompressOptions options) {
        InputStream is = null;
        Resources resources = Tiny.getInstance().getApplication().getResources();
        try {
            is = resources.openRawResource(resId, new TypedValue());
            BitmapFactory.Options decodeOptions = CompressKit.getDefaultDecodeOptions();
            decodeOptions.inPreferredConfig = options.config;
            decodeOptions.inSampleSize = sampleSize;
            return BitmapFactory.decodeStream(is, null, decodeOptions);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    //ignore...
                }
            }
        }
        return null;
    }

    private static Bitmap matrixCompress(Bitmap bitmap, int baseline, boolean recycle) {
        if (bitmap == null)
            return null;

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        int maxValue = Math.max(bitmapWidth, bitmapHeight);

        if (maxValue > baseline) {
            Matrix matrix = new Matrix();
            float rate = (float) baseline / (float) maxValue;
            matrix.postScale(rate, rate);
            Bitmap result = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true);
            if (recycle) {
                bitmap.recycle();
                bitmap = null;
            }
            return result;
        } else {
            return bitmap;
        }
    }

    private static int computeSampleSize(int width, int height, int baseline) {
        return computeAccurateSampleSize(Math.max(width, height), baseline);
    }

    private static int computeAccurateSampleSize(int value, int baseline) {
        int sampleSize = 1;
        int exponent = 1;
        for (; ; ) {
            int result = (int) Math.pow(2, exponent);
            if (value >= baseline * result) {
                sampleSize = result;
                exponent++;
            } else {
                break;
            }
        }
        return sampleSize;
    }
}
