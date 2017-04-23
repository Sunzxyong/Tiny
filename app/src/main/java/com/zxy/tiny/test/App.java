package com.zxy.tiny.test;

import android.app.Application;
import android.util.Log;

import com.zxy.recovery.callback.RecoveryCallback;
import com.zxy.recovery.core.Recovery;
import com.zxy.tiny.Tiny;

/**
 * Created by zhengxiaoyong on 2017/3/14.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Tiny.getInstance().debug(true).init(this);

        Recovery.getInstance().debug(true).callback(new RecoveryCallback() {
            @Override
            public void stackTrace(String stackTrace) {
                Log.e("zxy", "stackTrace:" + stackTrace);
            }

            @Override
            public void cause(String cause) {
                Log.e("zxy", "cause:" + cause);
            }

            @Override
            public void exception(String throwExceptionType, String throwClassName, String throwMethodName, int throwLineNumber) {

            }

            @Override
            public void throwable(Throwable throwable) {

            }
        }).init(this);
    }
}
