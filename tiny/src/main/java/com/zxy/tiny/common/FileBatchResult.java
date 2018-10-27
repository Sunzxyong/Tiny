package com.zxy.tiny.common;

import java.util.List;

/**
 * Created by zhengxiaoyong on 2018/10/23.
 */
public class FileBatchResult extends Result {

    public List<String> outfiles;

    @Override
    public String toString() {
        return "FileBatchResult{" +
                "outfiles=" + outfiles +
                ", success=" + success +
                ", throwable=" + throwable +
                '}';
    }
}
