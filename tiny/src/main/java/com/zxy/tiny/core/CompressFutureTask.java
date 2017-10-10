package com.zxy.tiny.core;

import com.zxy.tiny.callback.CallbackDispatcher;
import com.zxy.tiny.common.Logger;
import com.zxy.tiny.common.TinyUtil;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Created by zhengxiaoyong on 2017/3/11.
 */
public class CompressFutureTask<T> extends FutureTask<T> {

    private CallbackDispatcher<T> mCallbackDispatcher;

    private CompressFutureTask(Callable<T> callable) {
        super(callable);
    }

    private CompressFutureTask(Runnable runnable, T result) {
        super(runnable, result);
    }

    public CompressFutureTask(Callable<T> callable, CallbackDispatcher<T> dispatcher) {
        super(callable);
        this.mCallbackDispatcher = dispatcher;
    }

    /**
     * Task is done,include task status:finish、cancel、exception.
     */
    @Override
    protected void done() {
        super.done();
        Logger.e("task is done! thread-name:" + Thread.currentThread().getName());
    }

    @Override
    protected void set(T t) {
        super.set(t);
        MainThreadExecutor.postToMainThread(t, mCallbackDispatcher);
    }

    @Override
    protected void setException(Throwable t) {
        super.setException(t);

        MainThreadExecutor.postToMainThread(null, mCallbackDispatcher, t);

        TinyUtil.printExceptionMessage(t);
    }

}
