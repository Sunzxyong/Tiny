package com.zxy.tiny.core;

import android.os.Process;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zhengxiaoyong on 2017/3/10.
 */
public class CompressThreadFactory implements ThreadFactory {

    private static final String COMPRESS_THREAD_PREFIX_NAME = "tiny-compress-thread";

    private final AtomicInteger mThreadNumber = new AtomicInteger(1);

    @Override
    public Thread newThread(final Runnable r) {
        Runnable wrapper = new Runnable() {
            @Override
            public void run() {
                try {
                    //compression of the thread priority is 0.
                    Process.setThreadPriority(Process.THREAD_PRIORITY_DEFAULT);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
                r.run();
            }
        };

        String workThreadName = COMPRESS_THREAD_PREFIX_NAME + "-" + mThreadNumber.getAndIncrement();

        Thread thread = new Thread(wrapper, workThreadName);
        if (thread.isDaemon())
            thread.setDaemon(false);

        return thread;
    }
}
