package com.zxy.tiny.core;

import android.os.Environment;
import android.text.TextUtils;

import com.zxy.tiny.Tiny;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by zhengxiaoyong on 2017/3/13.
 */
public final class FileKit {

    private static final String DEFAULT_FILE_COMPRESS_DIRECTORY_NAME = "tiny";

    private static final Random RANDOM = new Random();

    private static final ThreadLocal<DateFormat> FILE_SUFFIX_DATE_FORMAT_THREAD_LOCAL = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        }
    };

    public static File generateCompressOutfileFormatJPEG() {
        String suffix = getDateFormat().format(new Date(System.currentTimeMillis()));
        int seed = RANDOM.nextInt(1000);
        return new File(getDefaultFileCompressDirectory(), "tiny-" + seed + "-" + suffix + ".jpg");
    }

    public static File generateCompressOutfileFormatPNG() {
        String suffix = getDateFormat().format(new Date(System.currentTimeMillis()));
        int seed = RANDOM.nextInt(1000);
        return new File(getDefaultFileCompressDirectory(), "tiny-" + seed + "-" + suffix + ".png");
    }

    public static File getDefaultFileCompressDirectory() {
        File file = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            file = Tiny.getInstance().getApplication().getExternalFilesDir(null);
        }
        file = file == null ? Tiny.getInstance().getApplication().getFilesDir() : file;

        file = new File(file.getParentFile(), DEFAULT_FILE_COMPRESS_DIRECTORY_NAME);
        if (!file.exists())
            file.mkdirs();
        return file;
    }

    public static long getSizeInBytes(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return 0L;
        return getSizeInBytes(new File(filePath));
    }

    public static long getSizeInBytes(File file) {
        if (file == null || !file.exists() || !file.isFile())
            return 0L;
        return file.length();
    }

    public static long getSizeInBytes(InputStream is) {
        if (is == null)
            return 0L;
        try {
            return is.available();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public static DateFormat getDateFormat() {
        return FILE_SUFFIX_DATE_FORMAT_THREAD_LOCAL.get();
    }

    public static File[] wrap(String[] filePaths) {
        if (filePaths == null || filePaths.length == 0)
            return null;
        File[] files = new File[filePaths.length];
        for (int i = 0; i < filePaths.length; i++) {
            String filePath = filePaths[i];
            files[i] = TextUtils.isEmpty(filePath) ? new File("") : new File(filePath);
        }
        return files;
    }
}
