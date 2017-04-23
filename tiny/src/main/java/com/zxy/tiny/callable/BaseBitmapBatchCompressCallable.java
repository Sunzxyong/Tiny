package com.zxy.tiny.callable;

import android.graphics.Bitmap;

import com.zxy.tiny.Tiny;

import java.util.concurrent.Callable;

/**
 * Created by zhengxiaoyong on 2017/3/31.
 */
abstract class BaseBitmapBatchCompressCallable implements Callable<Bitmap[]> {

    Tiny.BitmapCompressOptions mCompressOptions;

    BaseBitmapBatchCompressCallable(Tiny.BitmapCompressOptions options) {
        mCompressOptions = options;
    }

}
