package com.zxy.tiny.common;

import java.util.Arrays;

/**
 * Created by zhengxiaoyong on 2018/10/23.
 */
public class FileBatchResult extends Result {

    public String[] outfiles;

    @Override
    public String toString() {
        return "FileBatchResult{" +
                "outfiles=" + Arrays.toString(outfiles) +
                ", success=" + success +
                ", throwable=" + throwable +
                '}';
    }
}
