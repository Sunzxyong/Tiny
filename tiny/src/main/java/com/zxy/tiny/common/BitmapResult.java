package com.zxy.tiny.common;

import android.graphics.Bitmap;

/**
 * Created by zhengxiaoyong on 2018/10/23.
 */
public class BitmapResult extends Result {

    public Bitmap bitmap;

    @Override
    public String toString() {
        return "BitmapResult{" +
                "bitmap=" + bitmap +
                ", success=" + success +
                ", throwable=" + throwable +
                '}';
    }
}
