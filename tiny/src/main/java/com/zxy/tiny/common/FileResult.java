package com.zxy.tiny.common;

/**
 * Created by zhengxiaoyong on 2018/10/23.
 */
public class FileResult extends Result {

    public String outfile;

    @Override
    public String toString() {
        return "FileResult{" +
                "outfile='" + outfile + '\'' +
                ", success=" + success +
                ", throwable=" + throwable +
                '}';
    }
}
