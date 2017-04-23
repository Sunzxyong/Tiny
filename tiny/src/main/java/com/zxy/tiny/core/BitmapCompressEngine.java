package com.zxy.tiny.core;

import android.graphics.Bitmap;
import android.net.Uri;

import com.zxy.tiny.Tiny;
import com.zxy.tiny.callable.BitmapCompressCallableTasks;
import com.zxy.tiny.callback.BitmapCallback;
import com.zxy.tiny.callback.Callback;
import com.zxy.tiny.callback.DefaultCallbackDispatcher;

import java.io.File;
import java.io.InputStream;

/**
 * Created by zhengxiaoyong on 2017/3/12.
 */
public class BitmapCompressEngine extends CompressEngine {

    private Tiny.BitmapCompressOptions mCompressOptions;

    public BitmapCompressEngine withOptions(Tiny.BitmapCompressOptions options) {
        options.config = CompressKit.filterConfig(options.config);
        mCompressOptions = options;
        return this;
    }

    public void compress(BitmapCallback callback) {
        impl(callback);
    }

    private void impl(Callback callback) {
        if (mSource == null)
            return;

        if (mCompressOptions == null)
            mCompressOptions = new Tiny.BitmapCompressOptions();

        if (mSourceType == SourceType.FILE) {
            File file = (File) mSource;
            CompressExecutor.getExecutor()
                    .execute(new CompressFutureTask<Bitmap>(
                            new BitmapCompressCallableTasks.FileAsBitmapCallable(mCompressOptions, file)
                            , new DefaultCallbackDispatcher<Bitmap>(callback))
                    );

        } else if (mSourceType == SourceType.BITMAP) {
            Bitmap bitmap = (Bitmap) mSource;
            CompressExecutor.getExecutor()
                    .execute(new CompressFutureTask<Bitmap>(
                            new BitmapCompressCallableTasks.BitmapAsBitmapCallable(mCompressOptions, bitmap)
                            , new DefaultCallbackDispatcher<Bitmap>(callback)
                    ));

        } else if (mSourceType == SourceType.URI) {
            Uri uri = (Uri) mSource;
            CompressExecutor.getExecutor()
                    .execute(new CompressFutureTask<Bitmap>(
                            new BitmapCompressCallableTasks.UriAsBitmapCallable(mCompressOptions, uri)
                            , new DefaultCallbackDispatcher<Bitmap>(callback)
                    ));

        } else if (mSourceType == SourceType.BYTE_ARRAY) {
            byte[] bytes = (byte[]) mSource;
            CompressExecutor.getExecutor()
                    .execute(new CompressFutureTask<Bitmap>(
                            new BitmapCompressCallableTasks.ByteArrayAsBitmapCallable(mCompressOptions, bytes)
                            , new DefaultCallbackDispatcher<Bitmap>(callback)
                    ));

        } else if (mSourceType == SourceType.INPUT_STREAM) {
            InputStream is = (InputStream) mSource;
            CompressExecutor.getExecutor()
                    .execute(new CompressFutureTask<Bitmap>(
                            new BitmapCompressCallableTasks.InputStreamAsBitmapCallable(mCompressOptions, is)
                            , new DefaultCallbackDispatcher<Bitmap>(callback)
                    ));

        } else if (mSourceType == SourceType.RES_ID) {
            int resId = (int) mSource;
            CompressExecutor.getExecutor()
                    .execute(new CompressFutureTask<Bitmap>(
                            new BitmapCompressCallableTasks.ResourceAsBitmapCallable(mCompressOptions, resId)
                            , new DefaultCallbackDispatcher<Bitmap>(callback)
                    ));
        }
    }

}
