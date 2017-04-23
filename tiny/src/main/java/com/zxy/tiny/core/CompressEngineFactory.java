package com.zxy.tiny.core;


import com.zxy.tiny.common.TinyException;

/**
 * Created by zhengxiaoyong on 2017/3/12.
 */
public final class CompressEngineFactory {

    private CompressEngineFactory() {
        throw new TinyException.UnsupportedOperationException("can not be a instance");
    }

    public static BitmapCompressEngine buildBitmapCompressEngine(Object source, CompressEngine.SourceType type) {
        BitmapCompressEngine engine = new BitmapCompressEngine();
        engine.mSource = source;
        engine.mSourceType = type;
        return engine;
    }

    public static FileCompressEngine buildFileCompressEngine(Object source, CompressEngine.SourceType type) {
        FileCompressEngine engine = new FileCompressEngine();
        engine.mSource = source;
        engine.mSourceType = type;
        return engine;
    }

    public static BitmapBatchCompressEngine buildBitmapBatchCompressEngine(Object source, CompressEngine.SourceType type) {
        BitmapBatchCompressEngine engine = new BitmapBatchCompressEngine();
        engine.mSource = source;
        engine.mSourceType = type;
        return engine;
    }

    public static FileBatchCompressEngine buildFileBatchCompressEngine(Object source, CompressEngine.SourceType type) {
        FileBatchCompressEngine engine = new FileBatchCompressEngine();
        engine.mSource = source;
        engine.mSourceType = type;
        return engine;
    }

}
