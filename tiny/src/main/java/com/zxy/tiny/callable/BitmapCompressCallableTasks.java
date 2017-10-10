package com.zxy.tiny.callable;

import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;

import com.zxy.tiny.Tiny;
import com.zxy.tiny.common.Conditions;
import com.zxy.tiny.common.TinyException;
import com.zxy.tiny.common.UriUtil;
import com.zxy.tiny.core.BitmapCompressor;
import com.zxy.tiny.core.CompressKit;
import com.zxy.tiny.core.HttpUrlConnectionFetcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhengxiaoyong on 2017/3/12.
 */
public class BitmapCompressCallableTasks {

    private BitmapCompressCallableTasks() {
        throw new TinyException.UnsupportedOperationException("can not be a instance");
    }

    public static final class FileAsBitmapCallable extends BaseBitmapCompressCallable {
        private File mFile;

        public FileAsBitmapCallable(Tiny.BitmapCompressOptions options, File file) {
            super(options);
            mFile = file;
        }

        @Override
        public Bitmap call() throws Exception {
            Bitmap result = null;
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(mFile);
                result = BitmapCompressor.compress(CompressKit.transformToByteArray(fis), mCompressOptions, true);
            } finally {
                try {
                    if (fis != null)
                        fis.close();
                } catch (IOException e) {
                    //ignore...
                }
            }

            return result;
        }
    }

    public static final class BitmapAsBitmapCallable extends BaseBitmapCompressCallable {
        private Bitmap mBitmap;

        public BitmapAsBitmapCallable(Tiny.BitmapCompressOptions options, Bitmap bitmap) {
            super(options);
            mBitmap = bitmap;
        }

        @Override
        public Bitmap call() throws Exception {
            return BitmapCompressor.compress(mBitmap, mCompressOptions, true);
        }
    }

    public static final class InputStreamAsBitmapCallable extends BaseBitmapCompressCallable {
        private InputStream mInputStream;

        public InputStreamAsBitmapCallable(Tiny.BitmapCompressOptions options, InputStream is) {
            super(options);
            mInputStream = is;
        }

        @Override
        public Bitmap call() throws Exception {
            return BitmapCompressor.compress(CompressKit.transformToByteArray(mInputStream), mCompressOptions, true);
        }
    }

    public static final class UriAsBitmapCallable extends BaseBitmapCompressCallable {
        private Uri mUri;
        private Bitmap mResult = null;

        public UriAsBitmapCallable(Tiny.BitmapCompressOptions options, Uri uri) {
            super(options);
            mUri = uri;
        }

        @Override
        public Bitmap call() throws Exception {
            if (UriUtil.isNetworkUri(mUri)) {
                HttpUrlConnectionFetcher.fetch(mUri, new HttpUrlConnectionFetcher.ResponseCallback() {
                    @Override
                    public void callback(InputStream is) {
                        mResult = BitmapCompressor.compress(CompressKit.transformToByteArray(is), mCompressOptions, true);
                    }
                });

            } else if (UriUtil.isLocalContentUri(mUri) || UriUtil.isLocalFileUri(mUri)) {
                String filePath = UriUtil.getRealPathFromUri(mUri);
                if (TextUtils.isEmpty(filePath))
                    return null;
                if (Conditions.fileIsExist(filePath) && Conditions.fileCanRead(filePath)) {
                    FileInputStream fis = null;
                    File file = new File(filePath);
                    try {
                        fis = new FileInputStream(file);
                        mResult = BitmapCompressor.compress(CompressKit.transformToByteArray(fis), mCompressOptions, true);
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

            return mResult;
        }
    }

    public static final class ByteArrayAsBitmapCallable extends BaseBitmapCompressCallable {
        private byte[] mBytes;

        public ByteArrayAsBitmapCallable(Tiny.BitmapCompressOptions options, byte[] bytes) {
            super(options);
            mBytes = bytes;
        }

        @Override
        public Bitmap call() throws Exception {
            return BitmapCompressor.compress(mBytes, mCompressOptions, true);
        }
    }

    public static final class ResourceAsBitmapCallable extends BaseBitmapCompressCallable {
        private int mResId;

        public ResourceAsBitmapCallable(Tiny.BitmapCompressOptions options, int resId) {
            super(options);
            mResId = resId;
        }

        @Override
        public Bitmap call() throws Exception {
            return BitmapCompressor.compress(mResId, mCompressOptions, true);
        }
    }

    public static final class FileArrayAsBitmapCallable extends BaseBitmapBatchCompressCallable {
        private File[] mFiles;

        public FileArrayAsBitmapCallable(Tiny.BitmapCompressOptions options, File[] files) {
            super(options);
            mFiles = files;
        }

        @Override
        public Bitmap[] call() throws Exception {
            if (mFiles == null)
                return null;
            Bitmap[] result = new Bitmap[mFiles.length];
            for (int i = 0; i < mFiles.length; i++) {
                File file = mFiles[i];
                if (file == null) {
                    result[i] = null;
                    continue;
                }
                Bitmap bitmap = null;
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                    bitmap = BitmapCompressor.compress(CompressKit.transformToByteArray(fis), mCompressOptions, true);
                } finally {
                    try {
                        if (fis != null)
                            fis.close();
                    } catch (IOException e) {
                        //ignore...
                    }
                }
                result[i] = bitmap;
            }

            return result;
        }
    }

    public static final class BitmapArrayAsBitmapCallable extends BaseBitmapBatchCompressCallable {
        private Bitmap[] mBitmaps;

        public BitmapArrayAsBitmapCallable(Tiny.BitmapCompressOptions options, Bitmap[] bitmaps) {
            super(options);
            mBitmaps = bitmaps;
        }

        @Override
        public Bitmap[] call() throws Exception {
            if (mBitmaps == null)
                return null;
            Bitmap[] result = new Bitmap[mBitmaps.length];
            for (int i = 0; i < mBitmaps.length; i++) {
                Bitmap bitmap = mBitmaps[i];
                result[i] = BitmapCompressor.compress(bitmap, mCompressOptions, true);
            }

            return result;
        }
    }

    public static final class UriArrayAsBitmapCallable extends BaseBitmapBatchCompressCallable {
        private Uri[] mUris;

        public UriArrayAsBitmapCallable(Tiny.BitmapCompressOptions options, Uri[] uris) {
            super(options);
            mUris = uris;
        }

        @Override
        public Bitmap[] call() throws Exception {
            if (mUris == null)
                return null;
            Bitmap[] result = new Bitmap[mUris.length];
            for (int i = 0; i < mUris.length; i++) {
                Uri uri = mUris[i];
                if (uri == null) {
                    result[i] = null;
                    continue;
                }
                result[i] = new UriAsBitmapCallable(mCompressOptions, mUris[i]).call();
            }

            return result;
        }
    }

    public static final class ResourceArrayAsBitmapCallable extends BaseBitmapBatchCompressCallable {
        private int[] mResIds;

        public ResourceArrayAsBitmapCallable(Tiny.BitmapCompressOptions options, int[] resIds) {
            super(options);
            mResIds = resIds;
        }

        @Override
        public Bitmap[] call() throws Exception {
            if (mResIds == null)
                return null;
            Bitmap[] result = new Bitmap[mResIds.length];
            for (int i = 0; i < mResIds.length; i++) {
                result[i] = BitmapCompressor.compress(mResIds[i], mCompressOptions, true);
            }

            return result;
        }
    }

}
