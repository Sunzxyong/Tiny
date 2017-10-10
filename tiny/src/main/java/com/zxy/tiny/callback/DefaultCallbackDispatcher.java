package com.zxy.tiny.callback;

import android.graphics.Bitmap;

import com.zxy.tiny.common.BatchCompressResult;
import com.zxy.tiny.common.CompressResult;

/**
 * Created by zhengxiaoyong on 2017/3/12.
 */
public class DefaultCallbackDispatcher<T> implements CallbackDispatcher<T> {

    private Callback mCallback;

    public DefaultCallbackDispatcher(Callback callback) {
        this.mCallback = callback;
    }

    @Override
    public void dispatch(T t, Throwable tr) {
        if (mCallback == null)
            return;

        if (mCallback instanceof BitmapCallback) {
            if (t != null && t instanceof Bitmap) {
                ((BitmapCallback) mCallback).callback(true, (Bitmap) t, null);
                return;
            }
            ((BitmapCallback) mCallback).callback(false, null, tr);
        } else if (mCallback instanceof BitmapBatchCallback) {
            if (t != null && t instanceof Bitmap[]) {
                ((BitmapBatchCallback) mCallback).callback(true, (Bitmap[]) t, null);
                return;
            }
            ((BitmapBatchCallback) mCallback).callback(false, null, tr);
        } else if (mCallback instanceof FileCallback) {
            if (t != null && t instanceof CompressResult) {
                ((FileCallback) mCallback).callback(((CompressResult) t).success, ((CompressResult) t).outfile, ((CompressResult) t).throwable);
                return;
            }
            ((FileCallback) mCallback).callback(false, null, tr);
        } else if (mCallback instanceof FileWithBitmapCallback) {
            if (t != null && t instanceof CompressResult) {
                ((FileWithBitmapCallback) mCallback).callback(((CompressResult) t).success, ((CompressResult) t).bitmap, ((CompressResult) t).outfile, ((CompressResult) t).throwable);
                return;
            }
            ((FileWithBitmapCallback) mCallback).callback(false, null, null, tr);
        } else if (mCallback instanceof FileBatchCallback) {
            if (t != null && t instanceof BatchCompressResult) {
                CompressResult[] result = ((BatchCompressResult) t).results;
                if (result != null) {
                    String[] outfile = new String[result.length];
                    for (int i = 0; i < result.length; i++) {
                        CompressResult cr = result[i];
                        outfile[i] = cr == null ? null : cr.outfile;
                    }
                    ((FileBatchCallback) mCallback).callback(((BatchCompressResult) t).success, outfile, ((BatchCompressResult) t).throwable);
                    return;
                }
            }
            ((FileBatchCallback) mCallback).callback(false, null, tr);
        } else if (mCallback instanceof FileWithBitmapBatchCallback) {
            if (t != null && t instanceof BatchCompressResult) {
                CompressResult[] result = ((BatchCompressResult) t).results;
                if (result != null) {
                    Bitmap[] bitmaps = new Bitmap[result.length];
                    String[] outfile = new String[result.length];
                    for (int i = 0; i < result.length; i++) {
                        CompressResult cr = result[i];
                        bitmaps[i] = cr == null ? null : cr.bitmap;
                        outfile[i] = cr == null ? null : cr.outfile;
                    }
                    ((FileWithBitmapBatchCallback) mCallback).callback(((BatchCompressResult) t).success, bitmaps, outfile, ((BatchCompressResult) t).throwable);
                    return;
                }
            }
            ((FileWithBitmapBatchCallback) mCallback).callback(false, null, null, tr);
        }
    }

}
