package com.zxy.tiny;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;

import com.zxy.tiny.common.ApplicationLoader;
import com.zxy.tiny.common.TinyException;
import com.zxy.tiny.core.CompressEngine;
import com.zxy.tiny.core.CompressKit;
import com.zxy.tiny.core.FileKit;

import java.io.File;
import java.io.InputStream;

/**
 * Created by zhengxiaoyong on 2017/3/4.
 */
public final class Tiny {

    private volatile static Tiny sInstance;

    private Application mApplication;

    private boolean isDebug = false;

    private Tiny() {
    }

    public static Tiny getInstance() {
        if (sInstance == null) {
            synchronized (Tiny.class) {
                if (sInstance == null) {
                    sInstance = new Tiny();
                }
            }
        }
        return sInstance;
    }

    public Tiny debug(boolean isDebug) {
        this.isDebug = isDebug;
        return this;
    }

    /**
     * Initialization is not must.
     *
     * @param application
     */
    @Deprecated
    public void init(Application application) {
        if (application == null)
            throw new TinyException.IllegalArgumentException("application can not be null!");
        mApplication = application;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public Application getApplication() {
        if (mApplication == null)
            mApplication = ApplicationLoader.get();
        return mApplication;
    }

    public synchronized CompressEngine source(String filePath) {
        return new CompressEngine().source(TextUtils.isEmpty(filePath) ? new File("") : new File(filePath));
    }

    public synchronized CompressEngine source(File file) {
        return new CompressEngine().source(file);
    }

    public synchronized CompressEngine source(String[] filePaths) {
        return new CompressEngine().source(FileKit.wrap(filePaths));
    }

    public synchronized CompressEngine source(File[] files) {
        return new CompressEngine().source(files);
    }

    public synchronized CompressEngine source(Bitmap bitmap) {
        return new CompressEngine().source(bitmap);
    }

    public synchronized CompressEngine source(Bitmap[] bitmaps) {
        return new CompressEngine().source(bitmaps);
    }

    public synchronized CompressEngine source(Uri uri) {
        return new CompressEngine().source(uri);
    }

    public synchronized CompressEngine source(Uri[] uris) {
        return new CompressEngine().source(uris);
    }

    public synchronized CompressEngine source(byte[] bytes) {
        return new CompressEngine().source(bytes);
    }

    public synchronized CompressEngine source(InputStream is) {
        return new CompressEngine().source(is);
    }

    public synchronized CompressEngine source(int resId) {
        return new CompressEngine().source(resId);
    }

    public synchronized CompressEngine source(int[] resIds) {
        return new CompressEngine().source(resIds);
    }

    public synchronized boolean clearCompressDirectory() {
        File dir = FileKit.getDefaultFileCompressDirectory();
        try {
            return FileKit.clearDirectory(dir);
        } catch (Exception e) {
            //for android 6.0+,permission request
        }
        return false;
    }

    public static class BitmapCompressOptions {

        /**
         * By default,using ARGB_8888.
         * <p>
         * You can also consider using RGB_565,it can save half of memory size.
         */
        public Bitmap.Config config = CompressKit.DEFAULT_CONFIG;

        /**
         * The width of the bitmap.
         * <p>
         * If the value is zero,the default compression maximum width is the screen width or {@link CompressKit#DEFAULT_MAX_COMPRESS_SIZE}.
         */
        public int width;

        /**
         * The height of the bitmap.
         * <p>
         * If the value is zero,the default compression maximum height is the screen height or {@link CompressKit#DEFAULT_MAX_COMPRESS_SIZE}.
         */
        public int height;
    }

    public static class FileCompressOptions extends BitmapCompressOptions {

        /**
         * The compression quality,value range:0~100.
         * <p>
         * The smaller the value of the higher compression ratio.
         */
        public int quality = CompressKit.DEFAULT_QUALITY;

        /**
         * Whether to keep the bitmap width and height of the sample size.
         * <p>
         * Suggestion to false, can save the bitmap memory after decode.
         * <p>
         * Otherwise the bitmap width and height will remain the same,also compress in the compression process.
         */
        public boolean isKeepSampling = false;

        /**
         * The max memory size on the hard disk,unit of KB.
         * <p>
         * If the value less than or equal to zero,{@link Tiny} will be automatically set.
         */
        public float size;

        /**
         * The output path of the compressed file.
         * <p>
         * By default,we will according to time to generate a outfile.
         * <p>
         * for batch see {@link BatchFileCompressOptions#outfiles}.
         */
        public String outfile;

        /**
         * Whether need to cover the source file,only to the file(file、content://、file://).
         */
        public boolean overrideSource = false;
    }

    public static class BatchFileCompressOptions extends FileCompressOptions {

        /**
         * The output paths of the compressed file.
         * <p>
         * By default,we will according to time to generate a outfile.
         */
        public String[] outfiles;
    }

}
