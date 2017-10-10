package com.zxy.tiny.common;

import android.graphics.Bitmap;

/**
 * Created by zhengxiaoyong on 2017/3/12.
 */
public final class CompressResult extends Result {

    public String outfile;

    public Bitmap bitmap;

    @Override
    public String toString() {
        return "CompressResult{" +
                "bitmap=" + bitmap +
                ", success=" + success +
                ", outfile='" + outfile + '\'' +
                ", throwable=" + throwable +
                '}';
    }
}
