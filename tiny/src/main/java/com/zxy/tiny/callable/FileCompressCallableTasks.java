package com.zxy.tiny.callable;

import android.graphics.Bitmap;
import android.net.Uri;

import com.zxy.tiny.Tiny;
import com.zxy.tiny.common.BatchCompressResult;
import com.zxy.tiny.common.CompressResult;
import com.zxy.tiny.common.TinyException;
import com.zxy.tiny.common.UriUtil;
import com.zxy.tiny.core.CompressKit;
import com.zxy.tiny.core.FileCompressor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhengxiaoyong on 2017/3/13.
 */
public class FileCompressCallableTasks {

    private FileCompressCallableTasks() {
        throw new TinyException.UnsupportedOperationException("can not be a instance");
    }

    public static final class InputStreamAsFileCallable extends BaseFileCompressCallable {
        private InputStream mInputStream;

        public InputStreamAsFileCallable(Tiny.FileCompressOptions options, boolean withBitmap, InputStream is) {
            super(options, withBitmap);
            mInputStream = is;
        }

        @Override
        public CompressResult call() throws Exception {
            return FileCompressor.compress(CompressKit.transformToByteArray(mInputStream), mCompressOptions, shouldReturnBitmap, true);
        }
    }

    public static final class BitmapAsFileCallable extends BaseFileCompressCallable {
        private Bitmap mBitmap;

        public BitmapAsFileCallable(Tiny.FileCompressOptions options, boolean withBitmap, Bitmap bitmap) {
            super(options, withBitmap);
            mBitmap = bitmap;
        }

        @Override
        public CompressResult call() throws Exception {
            Bitmap bitmap = FileCompressor.shouldKeepSampling(mBitmap, mCompressOptions);
            return FileCompressor.compress(bitmap, mCompressOptions, shouldReturnBitmap, false);
        }
    }

    public static final class FileAsFileCallable extends BaseFileCompressCallable {
        private File mFile;

        public FileAsFileCallable(Tiny.FileCompressOptions options, boolean withBitmap, File file) {
            super(options, withBitmap);
            mFile = file;
        }

        @Override
        public CompressResult call() throws Exception {
            CompressResult result = null;
            FileInputStream fis = null;
            try {
                if (mCompressOptions != null && mCompressOptions.overrideSource)
                    mCompressOptions.outfile = mFile.getAbsolutePath();
                fis = new FileInputStream(mFile);
                result = FileCompressor.compress(CompressKit.transformToByteArray(fis), mCompressOptions, shouldReturnBitmap, true);
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

    public static final class UriAsFileCallable extends BaseFileCompressCallable {
        private Uri mUri;

        public UriAsFileCallable(Tiny.FileCompressOptions options, boolean withBitmap, Uri uri) {
            super(options, withBitmap);
            mUri = uri;
        }

        @Override
        public CompressResult call() throws Exception {
            Bitmap bitmap = FileCompressor.shouldKeepSampling(mUri, mCompressOptions);
            if (mCompressOptions != null && mCompressOptions.overrideSource &&
                    (UriUtil.isLocalContentUri(mUri) || UriUtil.isLocalFileUri(mUri))) {
                mCompressOptions.outfile = UriUtil.getRealPathFromUri(mUri);
            }
            return FileCompressor.compress(bitmap, mCompressOptions, shouldReturnBitmap, true);
        }
    }

    public static final class ByteArrayAsFileCallable extends BaseFileCompressCallable {
        private byte[] mBytes;

        public ByteArrayAsFileCallable(Tiny.FileCompressOptions options, boolean withBitmap, byte[] bytes) {
            super(options, withBitmap);
            mBytes = bytes;
        }

        @Override
        public CompressResult call() throws Exception {
            return FileCompressor.compress(mBytes, mCompressOptions, shouldReturnBitmap, true);
        }
    }

    public static final class ResourceAsFileCallable extends BaseFileCompressCallable {
        private int mResId;

        public ResourceAsFileCallable(Tiny.FileCompressOptions options, boolean withBitmap, int resId) {
            super(options, withBitmap);
            mResId = resId;
        }

        @Override
        public CompressResult call() throws Exception {
            Bitmap bitmap = FileCompressor.shouldKeepSampling(mResId, mCompressOptions);
            return FileCompressor.compress(bitmap, mCompressOptions, shouldReturnBitmap, true);
        }
    }

    public static final class FileArrayAsFileCallable extends BaseFileBatchCompressCallable {
        private File[] mFiles;

        public FileArrayAsFileCallable(Tiny.FileCompressOptions options, boolean withBitmap, File[] files) {
            super(options, withBitmap);
            mFiles = files;
        }

        @Override
        public BatchCompressResult call() throws Exception {
            if (mFiles == null)
                return null;
            BatchCompressResult result = new BatchCompressResult();
            result.results = new CompressResult[mFiles.length];

            String[] outfilePaths = getBatchOutfilePaths(mCompressOptions, mFiles.length);

            for (int i = 0; i < mFiles.length; i++) {
                File file = mFiles[i];
                if (file == null) {
                    result.results[i] = null;
                    continue;
                }
                CompressResult compressResult = null;
                FileInputStream fis = null;
                try {
                    if (mCompressOptions != null) {
                        if (outfilePaths != null && outfilePaths.length == mFiles.length)
                            mCompressOptions.outfile = outfilePaths[i];

                        if (mCompressOptions.overrideSource)
                            mCompressOptions.outfile = file.getAbsolutePath();
                    }
                    fis = new FileInputStream(file);
                    compressResult = FileCompressor.compress(CompressKit.transformToByteArray(fis), mCompressOptions, shouldReturnBitmap, true);
                } finally {
                    try {
                        if (fis != null)
                            fis.close();
                    } catch (IOException e) {
                        //ignore...
                    }
                }
                if (compressResult != null)
                    result.success = true;
                result.results[i] = compressResult;
            }

            return result;
        }
    }

    public static final class BitmapArrayAsFileCallable extends BaseFileBatchCompressCallable {
        private Bitmap[] mBitmaps;

        public BitmapArrayAsFileCallable(Tiny.FileCompressOptions options, boolean withBitmap, Bitmap[] bitmaps) {
            super(options, withBitmap);
            mBitmaps = bitmaps;
        }

        @Override
        public BatchCompressResult call() throws Exception {
            if (mBitmaps == null)
                return null;
            BatchCompressResult result = new BatchCompressResult();
            result.results = new CompressResult[mBitmaps.length];

            String[] outfilePaths = getBatchOutfilePaths(mCompressOptions, mBitmaps.length);

            for (int i = 0; i < mBitmaps.length; i++) {
                Bitmap bitmap = FileCompressor.shouldKeepSampling(mBitmaps[i], mCompressOptions);
                if (mCompressOptions != null && outfilePaths != null && outfilePaths.length == mBitmaps.length)
                    mCompressOptions.outfile = outfilePaths[i];

                CompressResult compressResult = FileCompressor.compress(bitmap, mCompressOptions, shouldReturnBitmap, false);
                if (compressResult != null)
                    result.success = true;
                result.results[i] = compressResult;
            }
            return result;
        }
    }

    public static final class UriArrayAsFileCallable extends BaseFileBatchCompressCallable {
        private Uri[] mUris;

        public UriArrayAsFileCallable(Tiny.FileCompressOptions options, boolean withBitmap, Uri[] uris) {
            super(options, withBitmap);
            mUris = uris;
        }

        @Override
        public BatchCompressResult call() throws Exception {
            if (mUris == null)
                return null;
            BatchCompressResult result = new BatchCompressResult();
            result.results = new CompressResult[mUris.length];

            String[] outfilePaths = getBatchOutfilePaths(mCompressOptions, mUris.length);

            for (int i = 0; i < mUris.length; i++) {
                Uri uri = mUris[i];
                if (uri == null) {
                    result.results[i] = null;
                    continue;
                }
                if (mCompressOptions != null && outfilePaths != null && outfilePaths.length == mUris.length)
                    mCompressOptions.outfile = outfilePaths[i];

                CompressResult compressResult = new UriAsFileCallable(mCompressOptions, shouldReturnBitmap, uri).call();
                if (compressResult != null)
                    result.success = true;
                result.results[i] = compressResult;
            }

            return result;
        }
    }

    public static final class ResourceArrayAsFileCallable extends BaseFileBatchCompressCallable {
        private int[] mResIds;

        public ResourceArrayAsFileCallable(Tiny.FileCompressOptions options, boolean withBitmap, int[] resIds) {
            super(options, withBitmap);
            mResIds = resIds;
        }

        @Override
        public BatchCompressResult call() throws Exception {
            if (mResIds == null)
                return null;
            BatchCompressResult result = new BatchCompressResult();
            result.results = new CompressResult[mResIds.length];

            String[] outfilePaths = getBatchOutfilePaths(mCompressOptions, mResIds.length);

            for (int i = 0; i < mResIds.length; i++) {
                Bitmap bitmap = FileCompressor.shouldKeepSampling(mResIds[i], mCompressOptions);
                if (mCompressOptions != null && outfilePaths != null && outfilePaths.length == mResIds.length)
                    mCompressOptions.outfile = outfilePaths[i];

                CompressResult compressResult = FileCompressor.compress(bitmap, mCompressOptions, shouldReturnBitmap, true);
                if (compressResult != null)
                    result.success = true;
                result.results[i] = compressResult;
            }
            return result;
        }
    }

    private static String[] getBatchOutfilePaths(Tiny.FileCompressOptions options, int length) {
        if (options == null || length <= 0)
            return null;
        String[] outfilePaths = null;
        if (options instanceof Tiny.BatchFileCompressOptions) {
            Tiny.BatchFileCompressOptions compressOptions = (Tiny.BatchFileCompressOptions) options;
            String[] outfiles = compressOptions.outfiles;
            if (outfiles != null && outfiles.length > 0) {
                outfilePaths = new String[length];
                if (outfiles.length >= length) {
                    System.arraycopy(outfiles, 0, outfilePaths, 0, length);
                } else {
                    for (int i = 0; i < length; i++) {
                        try {
                            outfilePaths[i] = outfiles[i];
                        } catch (Exception e) {
                            outfilePaths[i] = null;
                        }
                    }
                }
            }
        } else {
            options.outfile = null;
        }
        return outfilePaths;
    }

}
