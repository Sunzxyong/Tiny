package com.zxy.tiny.callable;

import android.graphics.Bitmap;

import com.zxy.tiny.Tiny;

import java.util.concurrent.Callable;

/**
 * Created by zhengxiaoyong on 2017/3/11.
 */
abstract class BaseBitmapCompressCallable implements Callable<Bitmap> {

    Tiny.BitmapCompressOptions mCompressOptions;

    BaseBitmapCompressCallable(Tiny.BitmapCompressOptions options) {
        mCompressOptions = options;
    }

}
