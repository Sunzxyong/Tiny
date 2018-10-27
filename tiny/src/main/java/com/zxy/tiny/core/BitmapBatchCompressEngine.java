package com.zxy.tiny.core;

import android.graphics.Bitmap;
import android.net.Uri;

import com.zxy.tiny.Tiny;
import com.zxy.tiny.callable.BitmapCompressCallableTasks;
import com.zxy.tiny.callback.BitmapBatchCallback;
import com.zxy.tiny.callback.Callback;
import com.zxy.tiny.callback.DefaultCallbackDispatcher;
import com.zxy.tiny.common.BitmapBatchResult;

import java.io.File;
import java.util.Arrays;

/**
 * Created by zhengxiaoyong on 2017/3/31.
 */
public class BitmapBatchCompressEngine extends CompressEngine {

    private Tiny.BitmapCompressOptions mCompressOptions;

    public BitmapBatchCompressEngine withOptions(Tiny.BitmapCompressOptions options) {
        options.config = CompressKit.filterConfig(options.config);
        mCompressOptions = options;
        return this;
    }

    public void batchCompress(BitmapBatchCallback callback) {
        impl(callback);
    }

    public BitmapBatchResult batchCompressSync() {
        return implSync();
    }

    private BitmapBatchResult implSync() {
        BitmapBatchResult result = new BitmapBatchResult();

        if (mSource == null) {
            result.success = false;
            result.throwable = new RuntimeException("the source is null!");
            return result;
        }

        if (mCompressOptions == null)
            mCompressOptions = new Tiny.BitmapCompressOptions();

        if (mSourceType == SourceType.FILE_ARRAY) {
            File[] file = (File[]) mSource;
            try {
                Bitmap[] bitmaps = new BitmapCompressCallableTasks.FileArrayAsBitmapCallable(mCompressOptions, file).call();
                if (bitmaps == null || bitmaps.length == 0) {
                    result.bitmaps = null;
                    result.success = false;
                    result.throwable = new RuntimeException("the compress result is null!");
                } else {
                    result.bitmaps = Arrays.asList(bitmaps);
                    result.success = true;
                }
            } catch (Exception e) {
                result.success = false;
                result.throwable = e;
            }
        } else if (mSourceType == SourceType.BITMAP_ARRAY) {
            try {
                Bitmap[] bitmaps = new BitmapCompressCallableTasks.BitmapArrayAsBitmapCallable(mCompressOptions, (Bitmap[]) mSource).call();
                if (bitmaps == null || bitmaps.length == 0) {
                    result.bitmaps = null;
                    result.success = false;
                    result.throwable = new RuntimeException("the compress result is null!");
                } else {
                    result.bitmaps = Arrays.asList(bitmaps);
                    result.success = true;
                }
            } catch (Exception e) {
                result.success = false;
                result.throwable = e;
            }
        } else if (mSourceType == SourceType.URI_ARRAY) {
            Uri[] uris = (Uri[]) mSource;
            try {
                Bitmap[] bitmaps = new BitmapCompressCallableTasks.UriArrayAsBitmapCallable(mCompressOptions, uris).call();
                if (bitmaps == null || bitmaps.length == 0) {
                    result.bitmaps = null;
                    result.success = false;
                    result.throwable = new RuntimeException("the compress result is null!");
                } else {
                    result.bitmaps = Arrays.asList(bitmaps);
                    result.success = true;
                }
            } catch (Exception e) {
                result.success = false;
                result.throwable = e;
            }
        } else if (mSourceType == SourceType.RES_ID_ARRAY) {
            int[] resIds = (int[]) mSource;
            try {
                Bitmap[] bitmaps = new BitmapCompressCallableTasks.ResourceArrayAsBitmapCallable(mCompressOptions, resIds).call();
                if (bitmaps == null || bitmaps.length == 0) {
                    result.bitmaps = null;
                    result.success = false;
                    result.throwable = new RuntimeException("the compress result is null!");
                } else {
                    result.bitmaps = Arrays.asList(bitmaps);
                    result.success = true;
                }
            } catch (Exception e) {
                result.success = false;
                result.throwable = e;
            }
        }

        return result;
    }

    private void impl(Callback callback) {
        if (mSource == null) {
            if (callback instanceof BitmapBatchCallback) {
                ((BitmapBatchCallback) callback).callback(false, null, new RuntimeException("the source is null!"));
            }
            return;
        }

        if (mCompressOptions == null)
            mCompressOptions = new Tiny.BitmapCompressOptions();

        if (mSourceType == SourceType.FILE_ARRAY) {
            File[] file = (File[]) mSource;
            CompressExecutor.getExecutor()
                    .execute(new CompressFutureTask<Bitmap[]>(
                            new BitmapCompressCallableTasks.FileArrayAsBitmapCallable(mCompressOptions, file)
                            , new DefaultCallbackDispatcher<Bitmap[]>(callback)
                    ));

        } else if (mSourceType == SourceType.BITMAP_ARRAY) {
            Bitmap[] bitmaps = (Bitmap[]) mSource;
            CompressExecutor.getExecutor()
                    .execute(new CompressFutureTask<Bitmap[]>(
                            new BitmapCompressCallableTasks.BitmapArrayAsBitmapCallable(mCompressOptions, bitmaps)
                            , new DefaultCallbackDispatcher<Bitmap[]>(callback)
                    ));

        } else if (mSourceType == SourceType.URI_ARRAY) {
            Uri[] uris = (Uri[]) mSource;
            CompressExecutor.getExecutor()
                    .execute(new CompressFutureTask<Bitmap[]>(
                            new BitmapCompressCallableTasks.UriArrayAsBitmapCallable(mCompressOptions, uris)
                            , new DefaultCallbackDispatcher<Bitmap[]>(callback)
                    ));
        } else if (mSourceType == SourceType.RES_ID_ARRAY) {
            int[] resIds = (int[]) mSource;
            CompressExecutor.getExecutor()
                    .execute(new CompressFutureTask<Bitmap[]>(
                            new BitmapCompressCallableTasks.ResourceArrayAsBitmapCallable(mCompressOptions, resIds)
                            , new DefaultCallbackDispatcher<Bitmap[]>(callback)
                    ));
        }
    }
}
