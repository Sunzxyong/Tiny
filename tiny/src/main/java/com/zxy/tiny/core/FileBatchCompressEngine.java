package com.zxy.tiny.core;

import android.graphics.Bitmap;
import android.net.Uri;

import com.zxy.tiny.Tiny;
import com.zxy.tiny.callable.FileCompressCallableTasks;
import com.zxy.tiny.callback.Callback;
import com.zxy.tiny.callback.DefaultCallbackDispatcher;
import com.zxy.tiny.callback.FileBatchCallback;
import com.zxy.tiny.callback.FileWithBitmapBatchCallback;
import com.zxy.tiny.common.BatchCompressResult;

import java.io.File;

/**
 * Created by zhengxiaoyong on 2017/3/31.
 */
public class FileBatchCompressEngine extends CompressEngine {

    private Tiny.FileCompressOptions mCompressOptions;

    public FileBatchCompressEngine withOptions(Tiny.FileCompressOptions options) {
        options.config = CompressKit.filterConfig(options.config);
        mCompressOptions = options;
        return this;
    }

    public void batchCompress(FileBatchCallback callback) {
        impl(callback);
    }

    public void batchCompress(FileWithBitmapBatchCallback callback) {
        impl(callback);
    }

    private void impl(Callback callback) {
        if (mSource == null)
            return;

        boolean shouldReturnBitmap = false;

        if (callback != null && callback instanceof FileWithBitmapBatchCallback)
            shouldReturnBitmap = true;

        if (mCompressOptions == null)
            mCompressOptions = new Tiny.FileCompressOptions();

        if (mSourceType == SourceType.FILE_ARRAY) {
            File[] files = (File[]) mSource;
            CompressExecutor.getExecutor()
                    .execute(new CompressFutureTask<BatchCompressResult>(
                            new FileCompressCallableTasks.FileArrayAsFileCallable(mCompressOptions, shouldReturnBitmap, files)
                            , new DefaultCallbackDispatcher<BatchCompressResult>(callback)
                    ));
        } else if (mSourceType == SourceType.BITMAP_ARRAY) {
            Bitmap[] bitmaps = (Bitmap[]) mSource;
            CompressExecutor.getExecutor()
                    .execute(new CompressFutureTask<BatchCompressResult>(
                            new FileCompressCallableTasks.BitmapArrayAsFileCallable(mCompressOptions, shouldReturnBitmap, bitmaps)
                            , new DefaultCallbackDispatcher<BatchCompressResult>(callback)
                    ));

        } else if (mSourceType == SourceType.URI_ARRAY) {
            Uri[] uris = (Uri[]) mSource;
            CompressExecutor.getExecutor()
                    .execute(new CompressFutureTask<BatchCompressResult>(
                            new FileCompressCallableTasks.UriArrayAsFileCallable(mCompressOptions, shouldReturnBitmap, uris)
                            , new DefaultCallbackDispatcher<BatchCompressResult>(callback)
                    ));
        } else if (mSourceType == SourceType.RES_ID_ARRAY) {
            int[] resIds = (int[]) mSource;
            CompressExecutor.getExecutor()
                    .execute(new CompressFutureTask<BatchCompressResult>(
                            new FileCompressCallableTasks.ResourceArrayAsFileCallable(mCompressOptions, shouldReturnBitmap, resIds)
                            , new DefaultCallbackDispatcher<BatchCompressResult>(callback)
                    ));
        }
    }
}
