package com.zxy.tiny.core;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Pair;
import android.util.TypedValue;

import com.zxy.tiny.Tiny;

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

        boolean hasCustomSize = false;

        if (compressWidth > 0 && compressHeight > 0)
            hasCustomSize = true;

        int baseline;
        if (isViewMode) {
            if (screenPair.second >= CompressKit.getBaseline(options.baseline) ||
                    screenPair.first >= CompressKit.getBaseline(options.baseline)) {
                baseline = hasCustomSize ? Math.min(CompressKit.getBaseline(options.baseline), Math.max(compressWidth, compressHeight)) : CompressKit.getBaseline(options.baseline);
                result = matrixCompress(bitmap, baseline, false);
            } else {
                baseline = hasCustomSize ? Math.min(CompressKit.getBaseline(options.baseline), Math.max(compressWidth, compressHeight)) : screenPair.second;
                result = matrixCompress(bitmap, baseline, false);
            }
        } else {
            baseline = hasCustomSize ? Math.min(CompressKit.getBaseline(options.baseline), Math.max(compressWidth, compressHeight)) : CompressKit.getBaseline(options.baseline);
            result = matrixCompress(bitmap, baseline, false);
        }

        if (result == null)
            return null;

        if (hasCustomSize) {
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

        boolean hasCustomSize = false;

        if (compressWidth > 0 && compressHeight > 0)
            hasCustomSize = true;

        int baseline;
        if (isViewMode) {
            if (screenPair.second >= CompressKit.getBaseline(options.baseline) ||
                    screenPair.first >= CompressKit.getBaseline(options.baseline)) {
                baseline = hasCustomSize ? Math.min(CompressKit.getBaseline(options.baseline), Math.max(compressWidth, compressHeight)) : CompressKit.getBaseline(options.baseline);
                int sampleSize = computeSampleSize(bitmapWidth, bitmapHeight, baseline);
                result = matrixCompress(sampleCompress(bytes, sampleSize, options), baseline, true);
            } else {
                baseline = hasCustomSize ? Math.min(CompressKit.getBaseline(options.baseline), Math.max(compressWidth, compressHeight)) : screenPair.second;
                int sampleSize = computeSampleSize(bitmapWidth, bitmapHeight, baseline);
                result = matrixCompress(sampleCompress(bytes, sampleSize, options), baseline, true);
            }
        } else {
            baseline = hasCustomSize ? Math.min(CompressKit.getBaseline(options.baseline), Math.max(compressWidth, compressHeight)) : CompressKit.getBaseline(options.baseline);
            int sampleSize = computeSampleSize(bitmapWidth, bitmapHeight, baseline);
            result = matrixCompress(sampleCompress(bytes, sampleSize, options), baseline, true);
        }

        if (result == null)
            return null;

        if (hasCustomSize) {
            result = customCompress(result, compressWidth, compressHeight, true);
        }

        return result;
    }

    public static Bitmap compress(int resId, Tiny.BitmapCompressOptions options, boolean isViewMode) throws Exception {
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

        boolean hasCustomSize = false;

        if (compressWidth > 0 && compressHeight > 0)
            hasCustomSize = true;

        int baseline;
        if (isViewMode) {
            if (screenPair.second >= CompressKit.getBaseline(options.baseline) ||
                    screenPair.first >= CompressKit.getBaseline(options.baseline)) {
                baseline = hasCustomSize ? Math.min(CompressKit.getBaseline(options.baseline), Math.max(compressWidth, compressHeight)) : CompressKit.getBaseline(options.baseline);
                int sampleSize = computeSampleSize(bitmapWidth, bitmapHeight, baseline);
                result = matrixCompress(sampleCompress(resId, sampleSize, options), baseline, true);
            } else {
                baseline = hasCustomSize ? Math.min(CompressKit.getBaseline(options.baseline), Math.max(compressWidth, compressHeight)) : screenPair.second;
                int sampleSize = computeSampleSize(bitmapWidth, bitmapHeight, baseline);
                result = matrixCompress(sampleCompress(resId, sampleSize, options), baseline, true);
            }
        } else {
            baseline = hasCustomSize ? Math.min(CompressKit.getBaseline(options.baseline), Math.max(compressWidth, compressHeight)) : CompressKit.getBaseline(options.baseline);
            int sampleSize = computeSampleSize(bitmapWidth, bitmapHeight, baseline);
            result = matrixCompress(sampleCompress(resId, sampleSize, options), baseline, true);
        }

        if (result == null)
            return null;

        if (hasCustomSize) {
            result = customCompress(result, compressWidth, compressHeight, true);
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
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, decodeOptions);
        bitmap = Degrees.handle(bitmap, bytes);
        return bitmap;
    }

    public static Bitmap sampleCompress(int resId, int sampleSize, Tiny.BitmapCompressOptions options) throws Exception {
        InputStream is = null;
        Resources resources = Tiny.getInstance().getApplication().getResources();
        try {
            is = resources.openRawResource(resId, new TypedValue());
            BitmapFactory.Options decodeOptions = CompressKit.getDefaultDecodeOptions();
            decodeOptions.inPreferredConfig = options.config;
            decodeOptions.inSampleSize = sampleSize;
            Bitmap bitmap = BitmapFactory.decodeStream(is, null, decodeOptions);
            bitmap = Degrees.handle(bitmap, resId);
            return bitmap;
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
