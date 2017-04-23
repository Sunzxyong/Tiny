package com.zxy.tiny.core;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhengxiaoyong on 2017/3/10.
 */
public class CompressExecutor {

    private CompressExecutor() {
        throw new RuntimeException("can not be a instance");
    }

    private static final ThreadPoolExecutor DEFAULT_COMPRESS_EXECUTOR;

    static {
        int nThreads = Runtime.getRuntime().availableProcessors() + 1;
        DEFAULT_COMPRESS_EXECUTOR = new CompressThreadPool(
                nThreads,
                nThreads,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new CompressThreadFactory()
        );
    }

    public static ThreadPoolExecutor getExecutor() {
        return DEFAULT_COMPRESS_EXECUTOR;
    }

}
