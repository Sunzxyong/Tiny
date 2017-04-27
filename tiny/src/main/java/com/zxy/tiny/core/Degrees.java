package com.zxy.tiny.core;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.TypedValue;

import com.zxy.tiny.Tiny;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhengxiaoyong on 2017/4/27.
 */
public class Degrees {

    public static Bitmap handle(Bitmap bitmap, byte[] data) {
        if (ExifCompat.isJpeg(data)) {
            int orientation = ExifCompat.getOrientation(data);
            bitmap = BitmapKit.rotateBitmap(bitmap, orientation);
        }
        return bitmap;
    }

    public static Bitmap handle(Bitmap bitmap, int resId) {
        InputStream is = null;
        Resources resources = Tiny.getInstance().getApplication().getResources();
        try {
            is = resources.openRawResource(resId, new TypedValue());
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int len = -1;
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            os.close();

            if (ExifCompat.isJpeg(os.toByteArray())) {
                int orientation = ExifCompat.getOrientation(os.toByteArray());
                bitmap = BitmapKit.rotateBitmap(bitmap, orientation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    //ignore...
                }
            }
        }
        return bitmap;
    }

}
