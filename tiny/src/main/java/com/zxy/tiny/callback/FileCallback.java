package com.zxy.tiny.callback;

/**
 * Created by zhengxiaoyong on 2017/3/12.
 */
public interface FileCallback extends Callback {

    void callback(boolean isSuccess, String outfile, Throwable t);

}
