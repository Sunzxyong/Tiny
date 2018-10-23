package com.zxy.tiny.common;

import android.graphics.Bitmap;

import java.util.Arrays;

/**
 * Created by zhengxiaoyong on 2018/10/23.
 */
public class BitmapBatchResult extends Result {

    public Bitmap[] bitmaps;

    @Override
    public String toString() {
        return "BitmapBatchResult{" +
                "bitmaps=" + Arrays.toString(bitmaps) +
                ", success=" + success +
                ", throwable=" + throwable +
                '}';
    }
}
