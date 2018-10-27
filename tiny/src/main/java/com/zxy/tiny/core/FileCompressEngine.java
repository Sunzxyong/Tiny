package com.zxy.tiny.core;

import android.graphics.Bitmap;
import android.net.Uri;

import com.zxy.tiny.Tiny;
import com.zxy.tiny.callable.FileCompressCallableTasks;
import com.zxy.tiny.callback.Callback;
import com.zxy.tiny.callback.DefaultCallbackDispatcher;
import com.zxy.tiny.callback.FileCallback;
import com.zxy.tiny.callback.FileWithBitmapCallback;
import com.zxy.tiny.common.CompressResult;
import com.zxy.tiny.common.FileResult;
import com.zxy.tiny.common.FileWithBitmapResult;

import java.io.File;
import java.io.InputStream;

/**
 * Created by zhengxiaoyong on 2017/3/12.
 */
public class FileCompressEngine extends CompressEngine {

    private Tiny.FileCompressOptions mCompressOptions;

    public FileCompressEngine withOptions(Tiny.FileCompressOptions options) {
        options.config = CompressKit.filterConfig(options.config);
        mCompressOptions = options;
        return this;
    }

    public void compress(FileCallback callback) {
        impl(callback);
    }

    public void compress(FileWithBitmapCallback callback) {
        impl(callback);
    }

    public FileResult compressSync() {
        return implSync();
    }

    public FileWithBitmapResult compressWithReturnBitmapSync() {
        return implWithReturnBitmapSync();
    }

    private FileResult implSync() {
        FileResult result = new FileResult();

        if (mSource == null) {
            result.success = false;
            result.throwable = new RuntimeException("the source is null!");
            return result;
        }

        boolean shouldReturnBitmap = false;

        if (mCompressOptions == null)
            mCompressOptions = new Tiny.FileCompressOptions();

        if (mSourceType == SourceType.FILE) {
            File file = (File) mSource;
            try {
                CompressResult compressResult = new FileCompressCallableTasks.FileAsFileCallable(mCompressOptions, shouldReturnBitmap, file).call();
                result.outfile = compressResult.outfile;
                result.success = compressResult.success;
            } catch (Exception e) {
                result.success = false;
                result.throwable = e;
            }
        } else if (mSourceType == SourceType.BITMAP) {
            Bitmap bitmap = (Bitmap) mSource;
            try {
                CompressResult compressResult = new FileCompressCallableTasks.BitmapAsFileCallable(mCompressOptions, shouldReturnBitmap, bitmap).call();
                result.outfile = compressResult.outfile;
                result.success = compressResult.success;
            } catch (Exception e) {
                result.success = false;
                result.throwable = e;
            }
        } else if (mSourceType == SourceType.URI) {
            Uri uri = (Uri) mSource;
            try {
                CompressResult compressResult = new FileCompressCallableTasks.UriAsFileCallable(mCompressOptions, shouldReturnBitmap, uri).call();
                result.outfile = compressResult.outfile;
                result.success = compressResult.success;
            } catch (Exception e) {
                result.success = false;
                result.throwable = e;
            }
        } else if (mSourceType == SourceType.BYTE_ARRAY) {
            byte[] bytes = (byte[]) mSource;
            try {
                CompressResult compressResult = new FileCompressCallableTasks.ByteArrayAsFileCallable(mCompressOptions, shouldReturnBitmap, bytes).call();
                result.outfile = compressResult.outfile;
                result.success = compressResult.success;
            } catch (Exception e) {
                result.success = false;
                result.throwable = e;
            }
        } else if (mSourceType == SourceType.INPUT_STREAM) {
            InputStream is = (InputStream) mSource;
            try {
                CompressResult compressResult = new FileCompressCallableTasks.InputStreamAsFileCallable(mCompressOptions, shouldReturnBitmap, is).call();
                result.outfile = compressResult.outfile;
                result.success = compressResult.success;
            } catch (Exception e) {
                result.success = false;
                result.throwable = e;
            }
        } else if (mSourceType == SourceType.RES_ID) {
            int resId = (int) mSource;
            try {
                CompressResult compressResult = new FileCompressCallableTasks.ResourceAsFileCallable(mCompressOptions, shouldReturnBitmap, resId).call();
                result.outfile = compressResult.outfile;
                result.success = compressResult.success;
            } catch (Exception e) {
                result.success = false;
                result.throwable = e;
            }
        }

        return result;
    }

    private FileWithBitmapResult implWithReturnBitmapSync() {
        FileWithBitmapResult result = new FileWithBitmapResult();

        if (mSource == null) {
            result.success = false;
            result.throwable = new RuntimeException("the source is null!");
            return result;
        }

        boolean shouldReturnBitmap = true;

        if (mCompressOptions == null)
            mCompressOptions = new Tiny.FileCompressOptions();

        if (mSourceType == SourceType.FILE) {
            File file = (File) mSource;
            try {
                CompressResult compressResult = new FileCompressCallableTasks.FileAsFileCallable(mCompressOptions, shouldReturnBitmap, file).call();
                result.bitmap = compressResult.bitmap;
                result.outfile = compressResult.outfile;
                result.success = compressResult.success;
            } catch (Exception e) {
                result.success = false;
                result.throwable = e;
            }
        } else if (mSourceType == SourceType.BITMAP) {
            Bitmap bitmap = (Bitmap) mSource;
            try {
                CompressResult compressResult = new FileCompressCallableTasks.BitmapAsFileCallable(mCompressOptions, shouldReturnBitmap, bitmap).call();
                result.bitmap = compressResult.bitmap;
                result.outfile = compressResult.outfile;
                result.success = compressResult.success;
            } catch (Exception e) {
                result.success = false;
                result.throwable = e;
            }
        } else if (mSourceType == SourceType.URI) {
            Uri uri = (Uri) mSource;
            try {
                CompressResult compressResult = new FileCompressCallableTasks.UriAsFileCallable(mCompressOptions, shouldReturnBitmap, uri).call();
                result.bitmap = compressResult.bitmap;
                result.outfile = compressResult.outfile;
                result.success = compressResult.success;
            } catch (Exception e) {
                result.success = false;
                result.throwable = e;
            }
        } else if (mSourceType == SourceType.BYTE_ARRAY) {
            byte[] bytes = (byte[]) mSource;
            try {
                CompressResult compressResult = new FileCompressCallableTasks.ByteArrayAsFileCallable(mCompressOptions, shouldReturnBitmap, bytes).call();
                result.bitmap = compressResult.bitmap;
                result.outfile = compressResult.outfile;
                result.success = compressResult.success;
            } catch (Exception e) {
                result.success = false;
                result.throwable = e;
            }
        } else if (mSourceType == SourceType.INPUT_STREAM) {
            InputStream is = (InputStream) mSource;
            try {
                CompressResult compressResult = new FileCompressCallableTasks.InputStreamAsFileCallable(mCompressOptions, shouldReturnBitmap, is).call();
                result.bitmap = compressResult.bitmap;
                result.outfile = compressResult.outfile;
                result.success = compressResult.success;
            } catch (Exception e) {
                result.success = false;
                result.throwable = e;
            }
        } else if (mSourceType == SourceType.RES_ID) {
            int resId = (int) mSource;
            try {
                CompressResult compressResult = new FileCompressCallableTasks.ResourceAsFileCallable(mCompressOptions, shouldReturnBitmap, resId).call();
                result.bitmap = compressResult.bitmap;
                result.outfile = compressResult.outfile;
                result.success = compressResult.success;
            } catch (Exception e) {
                result.success = false;
                result.throwable = e;
            }
        }

        return result;
    }

    private void impl(Callback callback) {
        if (mSource == null) {
            if (callback instanceof FileCallback) {
                ((FileCallback) callback).callback(false, null, new RuntimeException("the source is null!"));
            } else if (callback instanceof FileWithBitmapCallback) {
                ((FileWithBitmapCallback) callback).callback(false, null, null, new RuntimeException("the source is null!"));
            }
            return;
        }

        boolean shouldReturnBitmap = false;

        if (callback != null && callback instanceof FileWithBitmapCallback)
            shouldReturnBitmap = true;

        if (mCompressOptions == null)
            mCompressOptions = new Tiny.FileCompressOptions();

        if (mSourceType == SourceType.FILE) {
            File file = (File) mSource;
            CompressExecutor.getExecutor()
                    .execute(new CompressFutureTask<CompressResult>(
                            new FileCompressCallableTasks.FileAsFileCallable(mCompressOptions, shouldReturnBitmap, file)
                            , new DefaultCallbackDispatcher<CompressResult>(callback)
                    ));

        } else if (mSourceType == SourceType.BITMAP) {
            Bitmap bitmap = (Bitmap) mSource;
            CompressExecutor.getExecutor()
                    .execute(new CompressFutureTask<CompressResult>(
                            new FileCompressCallableTasks.BitmapAsFileCallable(mCompressOptions, shouldReturnBitmap, bitmap)
                            , new DefaultCallbackDispatcher<CompressResult>(callback)
                    ));

        } else if (mSourceType == SourceType.URI) {
            Uri uri = (Uri) mSource;
            CompressExecutor.getExecutor()
                    .execute(new CompressFutureTask<CompressResult>(
                            new FileCompressCallableTasks.UriAsFileCallable(mCompressOptions, shouldReturnBitmap, uri)
                            , new DefaultCallbackDispatcher<CompressResult>(callback)
                    ));

        } else if (mSourceType == SourceType.BYTE_ARRAY) {
            byte[] bytes = (byte[]) mSource;
            CompressExecutor.getExecutor()
                    .execute(new CompressFutureTask<CompressResult>(
                            new FileCompressCallableTasks.ByteArrayAsFileCallable(mCompressOptions, shouldReturnBitmap, bytes)
                            , new DefaultCallbackDispatcher<CompressResult>(callback)
                    ));

        } else if (mSourceType == SourceType.INPUT_STREAM) {
            InputStream is = (InputStream) mSource;
            CompressExecutor.getExecutor()
                    .execute(new CompressFutureTask<CompressResult>(
                            new FileCompressCallableTasks.InputStreamAsFileCallable(mCompressOptions, shouldReturnBitmap, is)
                            , new DefaultCallbackDispatcher<CompressResult>(callback)
                    ));

        } else if (mSourceType == SourceType.RES_ID) {
            int resId = (int) mSource;
            CompressExecutor.getExecutor()
                    .execute(new CompressFutureTask<CompressResult>(
                            new FileCompressCallableTasks.ResourceAsFileCallable(mCompressOptions, shouldReturnBitmap, resId)
                            , new DefaultCallbackDispatcher<CompressResult>(callback)
                    ));
        }
    }

}
