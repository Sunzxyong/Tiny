package com.zxy.tiny.core;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;
import java.io.InputStream;

/**
 * Created by zhengxiaoyong on 2017/3/12.
 */
public class CompressEngine {

    SourceType mSourceType;

    Object mSource;

    enum SourceType {
        FILE(1),
        BITMAP(2),
        URI(3),
        BYTE_ARRAY(4),
        INPUT_STREAM(5),
        RES_ID(6),
        FILE_ARRAY(7),
        BITMAP_ARRAY(8),
        URI_ARRAY(9),
        RES_ID_ARRAY(10);

        int value;

        SourceType(int value) {
        }

        public int getValue() {
            return value;
        }

    }

    public CompressEngine source(File file) {
        mSourceType = SourceType.FILE;
        mSource = file;
        return this;
    }

    public CompressEngine source(Bitmap bitmap) {
        mSourceType = SourceType.BITMAP;
        mSource = bitmap;
        return this;
    }

    public CompressEngine source(Uri uri) {
        mSourceType = SourceType.URI;
        mSource = uri;
        return this;
    }

    public CompressEngine source(byte[] bytes) {
        mSourceType = SourceType.BYTE_ARRAY;
        mSource = bytes;
        return this;
    }

    public CompressEngine source(InputStream is) {
        mSourceType = SourceType.INPUT_STREAM;
        mSource = is;
        return this;
    }

    public CompressEngine source(int resId) {
        mSourceType = SourceType.RES_ID;
        mSource = resId;
        return this;
    }

    public CompressEngine source(File[] files) {
        mSourceType = SourceType.FILE_ARRAY;
        mSource = files;
        return this;
    }

    public CompressEngine source(Bitmap[] bitmaps) {
        mSourceType = SourceType.BITMAP_ARRAY;
        mSource = bitmaps;
        return this;
    }

    public CompressEngine source(Uri[] uris) {
        mSourceType = SourceType.URI_ARRAY;
        mSource = uris;
        return this;
    }

    public CompressEngine source(int[] resIds) {
        mSourceType = SourceType.RES_ID_ARRAY;
        mSource = resIds;
        return this;
    }

    public BitmapCompressEngine asBitmap() {
        return CompressEngineFactory.buildBitmapCompressEngine(mSource, mSourceType);
    }

    public FileCompressEngine asFile() {
        return CompressEngineFactory.buildFileCompressEngine(mSource, mSourceType);
    }

    public BitmapBatchCompressEngine batchAsBitmap() {
        return CompressEngineFactory.buildBitmapBatchCompressEngine(mSource, mSourceType);
    }

    public FileBatchCompressEngine batchAsFile() {
        return CompressEngineFactory.buildFileBatchCompressEngine(mSource, mSourceType);
    }

}
