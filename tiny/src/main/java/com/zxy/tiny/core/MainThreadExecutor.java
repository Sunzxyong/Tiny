package com.zxy.tiny.core;

import android.os.Handler;
import android.os.Looper;

import com.zxy.tiny.callback.CallbackDispatcher;

/**
 * Created by zhengxiaoyong on 2017/3/11.
 */
public class MainThreadExecutor {

    private static Handler sMainThreadHandler;

    public static <T> void postToMainThread(final T t, final CallbackDispatcher<T> dispatcher) {
        postToMainThread(t, dispatcher, null);
    }

    public static <T> void postToMainThread(final T t, final CallbackDispatcher<T> dispatcher, final Throwable tr) {
        if (dispatcher == null)
            return;

        enableMainThread();

        sMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                dispatcher.dispatch(t, tr);
            }
        });
    }

    private static void enableMainThread() {
        if (sMainThreadHandler == null || !isMainThreadHandler(sMainThreadHandler)) {
            sMainThreadHandler = new Handler(Looper.getMainLooper());
        }
    }

    private static boolean isMainThreadHandler(Handler handler) {
        return handler.getLooper().getThread() == Looper.getMainLooper().getThread();
    }
}
