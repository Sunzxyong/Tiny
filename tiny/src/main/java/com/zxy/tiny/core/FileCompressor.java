package com.zxy.tiny.core;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.TypedValue;

import com.zxy.libjpegturbo.JpegTurboCompressor;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.common.CompressResult;
import com.zxy.tiny.common.Conditions;
import com.zxy.tiny.common.Logger;
import com.zxy.tiny.common.UriUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
        Bitmap bitmap = shouldKeepSampling(bytes, options);
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

        boolean isSuccess = false;
        try {
            isSuccess = compress(bitmap, outfile, quality);
        } catch (FileNotFoundException e) {
            result.throwable = e;
            e.printStackTrace();
        } catch (Exception e) {
            //avoid v6.0+ occur crash without permission
            result.throwable = e;
            e.printStackTrace();
        }

        if (size > 0 && isSuccess) {
            float outfileSize = (float) FileKit.getSizeInBytes(outfile) / (float) 1024;
            while (outfileSize > size) {
                if (quality <= 25)
                    break;
                quality -= 5;
                try {
                    isSuccess = compress(bitmap, outfile, quality);
                } catch (FileNotFoundException e) {
                    result.throwable = e;
                    e.printStackTrace();
                } catch (Exception e) {
                    //avoid v6.0+ occur crash without permission
                    result.throwable = e;
                    e.printStackTrace();
                }
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

    private static boolean compress(Bitmap bitmap, String outfile, int quality) throws FileNotFoundException {
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

        static boolean compress(Bitmap bitmap, String outfile, int quality, Bitmap.CompressFormat format) throws FileNotFoundException {
            boolean isSuccess = false;
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(outfile);
                isSuccess = bitmap.compress(format, quality, fos);
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return isSuccess;
        }
    }

    public static Bitmap shouldKeepSampling(byte[] bytes, Tiny.FileCompressOptions options) {
        if (bytes == null || bytes.length == 0)
            return null;
        if (options == null)
            options = new Tiny.FileCompressOptions();

        Bitmap result = null;
        if (options.isKeepSampling) {
            //TODO do you need? compress in the compression process.
            BitmapFactory.Options decodeOptions = CompressKit.getDefaultDecodeOptions();
            decodeOptions.inPreferredConfig = options.config;
            result = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, decodeOptions);
        } else {
            result = BitmapCompressor.compress(bytes, options, false);
        }
        return result;
    }

    public static Bitmap shouldKeepSampling(Bitmap bitmap, Tiny.FileCompressOptions options) {
        if (bitmap == null || bitmap.isRecycled())
            return null;
        if (options == null)
            options = new Tiny.FileCompressOptions();

        Bitmap result = null;
        if (options.isKeepSampling) {
            result = bitmap;
        } else {
            result = BitmapCompressor.compress(bitmap, options, false);
        }
        return result;
    }

    public static Bitmap shouldKeepSampling(int resId, Tiny.FileCompressOptions options) throws Exception {
        if (options == null)
            options = new Tiny.FileCompressOptions();

        Bitmap result = null;
        if (options.isKeepSampling) {
            //for drawable resource,get the original size of resources,without scaling.
            InputStream is = null;
            Resources resources = Tiny.getInstance().getApplication().getResources();
            try {
                is = resources.openRawResource(resId, new TypedValue());
                BitmapFactory.Options decodeOptions = CompressKit.getDefaultDecodeOptions();
                decodeOptions.inPreferredConfig = options.config;
                return BitmapFactory.decodeStream(is, null, decodeOptions);
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        //ignore...
                    }
                }
            }
        } else {
            result = BitmapCompressor.compress(resId, options, false);
        }
        return result;
    }

    public static Bitmap shouldKeepSampling(Uri uri, final Tiny.FileCompressOptions options) throws Exception {
        if (uri == null)
            return null;

        final Bitmap[] result = {null};
        if (UriUtil.isNetworkUri(uri)) {
            HttpUrlConnectionFetcher.fetch(uri, new HttpUrlConnectionFetcher.ResponseCallback() {
                @Override
                public void callback(InputStream is) {
                    byte[] decodeBytes = CompressKit.transformToByteArray(is);
                    if (options.isKeepSampling) {
                        BitmapFactory.Options decodeOptions = CompressKit.getDefaultDecodeOptions();
                        decodeOptions.inPreferredConfig = options.config;
                        result[0] = BitmapFactory.decodeByteArray(decodeBytes, 0, decodeBytes.length, decodeOptions);
                    } else {
                        result[0] = BitmapCompressor.compress(decodeBytes, options, true);
                    }
                }
            });

        } else if (UriUtil.isLocalContentUri(uri) || UriUtil.isLocalFileUri(uri)) {
            String filePath = UriUtil.getRealPathFromUri(uri);
            if (TextUtils.isEmpty(filePath))
                return null;
            if (Conditions.fileIsExist(filePath) && Conditions.fileCanRead(filePath)) {
                FileInputStream fis = null;
                File file = new File(filePath);
                try {
                    fis = new FileInputStream(file);
                    byte[] decodeBytes = CompressKit.transformToByteArray(fis);
                    if (options.isKeepSampling) {
                        BitmapFactory.Options decodeOptions = CompressKit.getDefaultDecodeOptions();
                        decodeOptions.inPreferredConfig = options.config;
                        result[0] = BitmapFactory.decodeByteArray(decodeBytes, 0, decodeBytes.length, decodeOptions);
                    } else {
                        result[0] = BitmapCompressor.compress(decodeBytes, options, true);
                    }
                } finally {
                    try {
                        if (fis != null)
                            fis.close();
                    } catch (IOException e) {
                        //ignore...
                    }
                }
            }
        }
        return result[0];
    }
}
