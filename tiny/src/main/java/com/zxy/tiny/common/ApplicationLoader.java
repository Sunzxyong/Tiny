package com.zxy.tiny.common;

import android.app.Application;

import java.lang.reflect.Method;

/**
 * Created by zhengxiaoyong on 2017/5/23.
 */
public class ApplicationLoader {

    private static Application sApplication;

    public static Application get() {
        return sApplication != null ? sApplication : getApplication();
    }

    private static Application getApplication() {
        Application application = null;
        Method method;
        try {
            method = Class.forName("android.app.AppGlobals").getDeclaredMethod("getInitialApplication");
            method.setAccessible(true);
            application = (Application) method.invoke(null);
        } catch (Exception e) {
            try {
                method = Class.forName("android.app.ActivityThread").getDeclaredMethod("currentApplication");
                method.setAccessible(true);
                application = (Application) method.invoke(null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return application;
    }

}
