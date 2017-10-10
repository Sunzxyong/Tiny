package com.zxy.tiny.callback;

/**
 * Created by zhengxiaoyong on 2017/3/12.
 */
public interface CallbackDispatcher<T> {

    void dispatch(T t, Throwable tr);

}
