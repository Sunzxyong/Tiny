package com.zxy.tiny.common;

import android.text.TextUtils;

import java.io.File;

/**
 * Created by zhengxiaoyong on 2017/3/12.
 */
public final class Conditions {

    private static final String[] JPEG_FORMAT_SUFFIX = new String[]{
            ".jpg", ".jpeg", ".JPG", ".JPEG"
    };

    public static boolean fileIsExist(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return false;
        File file = new File(filePath);
        return file.exists() && file.isFile();
    }

    public static boolean fileCanRead(String filePath) {
        if (filePath == null)
            return false;
        final File file = new File(filePath);
        return file.exists() && file.canRead();
    }

    public static boolean fileCanRead(File file) {
        return file != null && file.exists() && file.canRead();
    }

    public static boolean isDirectory(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return true;
        File file = new File(filePath);
        return file.exists() && file.isDirectory();
    }

    public static boolean isJpegFormat(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return false;
        for (int i = 0; i < JPEG_FORMAT_SUFFIX.length; i++) {
            if (filePath.endsWith(JPEG_FORMAT_SUFFIX[i]))
                return true;
        }
        return false;
    }

}
