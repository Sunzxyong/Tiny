package com.zxy.tiny.common;

import java.util.Arrays;

/**
 * Created by zhengxiaoyong on 2017/3/31.
 */
public final class BatchCompressResult extends Result {

    public CompressResult[] results;

    @Override
    public String toString() {
        return "BatchCompressResult{" +
                "results=" + Arrays.toString(results) +
                ", success=" + success +
                ", throwable=" + throwable +
                '}';
    }
}
