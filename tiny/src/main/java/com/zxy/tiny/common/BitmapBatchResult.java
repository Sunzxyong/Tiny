package com.zxy.tiny.common;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by zhengxiaoyong on 2018/10/23.
 */
public class BitmapBatchResult extends Result {

    public List<Bitmap> bitmaps;

    @Override
    public String toString() {
        return "BitmapBatchResult{" +
                "bitmaps=" + bitmaps +
                ", success=" + success +
                ", throwable=" + throwable +
                '}';
    }
}
