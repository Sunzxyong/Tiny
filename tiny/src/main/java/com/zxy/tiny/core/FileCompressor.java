package com.zxy.tiny.core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import com.zxy.libjpegturbo.JpegTurboCompressor;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.common.CompressResult;
import com.zxy.tiny.common.Conditions;
import com.zxy.tiny.common.Logger;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by zhengxiaoyong on 2017/3/13.
 */
public class FileCompressor {

    public static CompressResult compress(byte[] bytes, Tiny.FileCompressOptions options, boolean withBitmap, boolean recycle) {
        if (bytes == null || bytes.length == 0)
            return null;

        if (options == null)
            options = new Tiny.FileCompressOptions();

        CompressResult result = null;
        Bitmap bitmap = null;

        if (options.isKeepSampling) {
            //TODO do you need? compress in the compression process.
            BitmapFactory.Options decodeOptions = CompressKit.getDefaultDecodeOptions();
            decodeOptions.inPreferredConfig = options.config;
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, decodeOptions);
        } else {
            bitmap = BitmapCompressor.compress(bytes, options, false);
        }
        result = compress(bitmap, options, withBitmap, recycle);
        return result;
    }

    public static CompressResult compress(Bitmap bitmap, Tiny.FileCompressOptions options, boolean withBitmap, boolean recycle) {
        if (bitmap == null || bitmap.isRecycled())
            return null;

        CompressResult result = new CompressResult();

        if (options == null)
            options = new Tiny.FileCompressOptions();

        int quality = options.quality;
        String outfile = options.outfile;
        float size = options.size;

        if (quality < 0 || quality > 100)
            quality = CompressKit.DEFAULT_QUALITY;

        if (Conditions.isDirectory(outfile))
            outfile = FileKit.generateCompressOutfileFormatJPEG().getAbsolutePath();

        if (!Conditions.isJpegFormat(outfile))
            outfile = FileKit.generateCompressOutfileFormatJPEG().getAbsolutePath();

        if (bitmap.hasAlpha())
            outfile = FileKit.generateCompressOutfileFormatPNG().getAbsolutePath();

        boolean isSuccess = compress(bitmap, outfile, quality);

        if (size > 0 && isSuccess) {
            float outfileSize = (float) FileKit.getSizeInBytes(outfile) / (float) 1024;
            while (outfileSize > size) {
                if (quality <= 25)
                    break;
                quality -= 5;
                isSuccess = compress(bitmap, outfile, quality);
                if (!isSuccess)
                    break;
                outfileSize = (float) FileKit.getSizeInBytes(outfile) / (float) 1024;
            }
        }

        Logger.e("compress quality: " + quality);

        result.outfile = outfile;
        result.success = isSuccess;

        if (withBitmap) {
            result.bitmap = bitmap;
        } else {
            if (recycle) {
                result.bitmap = null;
                bitmap.recycle();
                bitmap = null;
            }
        }
        return result;
    }

    private static boolean compress(Bitmap bitmap, String outfile, int quality) {
        if (bitmap == null || bitmap.isRecycled())
            return false;

        if (bitmap.hasAlpha()) {
            return CompatCompressor.compress(bitmap, outfile, quality, Bitmap.CompressFormat.PNG);
        } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                return JpegTurboCompressor.compress(bitmap, outfile, quality);
            } else {
                return CompatCompressor.compress(bitmap, outfile, quality, Bitmap.CompressFormat.JPEG);
            }
        }
    }

    private static final class CompatCompressor {

        static boolean compress(Bitmap bitmap, String outfile, int quality, Bitmap.CompressFormat format) {
            boolean isSuccess = false;
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(outfile);
                isSuccess = bitmap.compress(format, quality, fos);
            } catch (FileNotFoundException e) {
                isSuccess = false;
                e.printStackTrace();
            } catch (Exception e) {
                //avoid v6.0+ occur crash without permission
                e.printStackTrace();
            } finally {
                if (isSuccess) {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return isSuccess;
        }
    }
}
