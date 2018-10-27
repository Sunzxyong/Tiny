package com.zxy.tiny.common;

import java.util.List;

/**
 * Created by zhengxiaoyong on 2018/10/23.
 */
public class FileWithBitmapBatchResult extends Result {

    public List<FileWithBitmapResult> results;

    @Override
    public String toString() {
        return "FileWithBitmapBatchResult{" +
                "results=" + results +
                ", success=" + success +
                ", throwable=" + throwable +
                '}';
    }
}
