package com.zxy.tiny.common;

import android.graphics.Bitmap;

import java.util.Arrays;

/**
 * Created by zhengxiaoyong on 2018/10/23.
 */
public class FileWithBitmapBatchResult extends Result {

    public Bitmap[] bitmaps;

    public String[] outfiles;

    @Override
    public String toString() {
        return "FileWithBitmapBatchResult{" +
                "bitmaps=" + Arrays.toString(bitmaps) +
                ", outfiles=" + Arrays.toString(outfiles) +
                ", success=" + success +
                ", throwable=" + throwable +
                '}';
    }
}
