package com.zxy.tiny.callable;

import com.zxy.tiny.Tiny;
import com.zxy.tiny.common.CompressResult;

import java.util.concurrent.Callable;

/**
 * Created by zhengxiaoyong on 2017/3/1.
 */
abstract class BaseFileCompressCallable implements Callable<CompressResult> {

    Tiny.FileCompressOptions mCompressOptions;

    boolean shouldReturnBitmap;

    BaseFileCompressCallable(Tiny.FileCompressOptions options, boolean withBitmap) {
        mCompressOptions = options;
        shouldReturnBitmap = withBitmap;
    }

}
