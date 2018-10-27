package com.zxy.tiny.common;

import android.graphics.Bitmap;

/**
 * Created by zhengxiaoyong on 2018/10/23.
 */
public class FileWithBitmapResult extends Result {

    public Bitmap bitmap;

    public String outfile;

    @Override
    public String toString() {
        return "FileWithBitmapResult{" +
                "bitmap=" + bitmap +
                ", outfile='" + outfile + '\'' +
                ", success=" + success +
                ", throwable=" + throwable +
                '}';
    }
}
