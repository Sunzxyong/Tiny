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

    private void impl(Callback callback) {
        if (mSource == null)
            return;

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
