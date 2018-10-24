package com.zxy.tiny.core;

import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;

import com.zxy.tiny.Tiny;
import com.zxy.tiny.callable.FileCompressCallableTasks;
import com.zxy.tiny.callback.Callback;
import com.zxy.tiny.callback.DefaultCallbackDispatcher;
import com.zxy.tiny.callback.FileBatchCallback;
import com.zxy.tiny.callback.FileWithBitmapBatchCallback;
import com.zxy.tiny.common.BatchCompressResult;
import com.zxy.tiny.common.CompressResult;
import com.zxy.tiny.common.FileBatchResult;
import com.zxy.tiny.common.FileWithBitmapBatchResult;
import com.zxy.tiny.common.FileWithBitmapResult;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengxiaoyong on 2017/3/31.
 */
public class FileBatchCompressEngine extends CompressEngine {

    private Tiny.FileCompressOptions mCompressOptions;

    public FileBatchCompressEngine withOptions(Tiny.FileCompressOptions options) {
        options.config = CompressKit.filterConfig(options.config);
        mCompressOptions = options;
        return this;
    }

    public void batchCompress(FileBatchCallback callback) {
        impl(callback);
    }

    public void batchCompress(FileWithBitmapBatchCallback callback) {
        impl(callback);
    }

    public FileBatchResult batchCompressSync() {
        return implSync();
    }

    public FileWithBitmapBatchResult batchCompressWithReturnBitmapResult() {
        return implWithReturnBitmapSync();
    }

    private FileBatchResult implSync() {
        FileBatchResult result = new FileBatchResult();

        if (mSource == null) {
            result.success = false;
            result.throwable = new RuntimeException("the source is null!");
            return result;
        }

        boolean shouldReturnBitmap = false;

        if (mCompressOptions == null)
            mCompressOptions = new Tiny.FileCompressOptions();

        if (mSourceType == SourceType.FILE_ARRAY) {
            File[] files = (File[]) mSource;
            try {
                BatchCompressResult compressResult = new FileCompressCallableTasks.FileArrayAsFileCallable(mCompressOptions, shouldReturnBitmap, files).call();
                List<String> fileResults = new ArrayList<>();
                if (compressResult != null && compressResult.results != null) {
                    for (int i = 0; i < compressResult.results.length; i++) {
                        CompressResult tmp = compressResult.results[i];
                        if (tmp == null || TextUtils.isEmpty(tmp.outfile))
                            continue;
                        fileResults.add(tmp.outfile);
                    }
                    result.success = compressResult.success;
                } else {
                    result.success = false;
                    result.throwable = new RuntimeException("the compress result is null!");
                }
                result.outfiles = fileResults;
            } catch (Exception e) {
                result.success = false;
                result.throwable = e;
            }
        } else if (mSourceType == SourceType.BITMAP_ARRAY) {
            Bitmap[] bitmaps = (Bitmap[]) mSource;
            try {
                BatchCompressResult compressResult = new FileCompressCallableTasks.BitmapArrayAsFileCallable(mCompressOptions, shouldReturnBitmap, bitmaps).call();
                List<String> fileResults = new ArrayList<>();
                if (compressResult != null && compressResult.results != null) {
                    for (int i = 0; i < compressResult.results.length; i++) {
                        CompressResult tmp = compressResult.results[i];
                        if (tmp == null || TextUtils.isEmpty(tmp.outfile))
                            continue;
                        fileResults.add(tmp.outfile);
                    }
                    result.success = compressResult.success;
                } else {
                    result.success = false;
                    result.throwable = new RuntimeException("the compress result is null!");
                }
                result.outfiles = fileResults;
            } catch (Exception e) {
                result.success = false;
                result.throwable = e;
            }
        } else if (mSourceType == SourceType.URI_ARRAY) {
            Uri[] uris = (Uri[]) mSource;
            try {
                BatchCompressResult compressResult = new FileCompressCallableTasks.UriArrayAsFileCallable(mCompressOptions, shouldReturnBitmap, uris).call();
                List<String> fileResults = new ArrayList<>();
                if (compressResult != null && compressResult.results != null) {
                    for (int i = 0; i < compressResult.results.length; i++) {
                        CompressResult tmp = compressResult.results[i];
                        if (tmp == null || TextUtils.isEmpty(tmp.outfile))
                            continue;
                        fileResults.add(tmp.outfile);
                    }
                    result.success = compressResult.success;
                } else {
                    result.success = false;
                    result.throwable = new RuntimeException("the compress result is null!");
                }
                result.outfiles = fileResults;
            } catch (Exception e) {
                result.success = false;
                result.throwable = e;
            }
        } else if (mSourceType == SourceType.RES_ID_ARRAY) {
            int[] resIds = (int[]) mSource;
            try {
                BatchCompressResult compressResult = new FileCompressCallableTasks.ResourceArrayAsFileCallable(mCompressOptions, shouldReturnBitmap, resIds).call();
                List<String> fileResults = new ArrayList<>();
                if (compressResult != null && compressResult.results != null) {
                    for (int i = 0; i < compressResult.results.length; i++) {
                        CompressResult tmp = compressResult.results[i];
                        if (tmp == null || TextUtils.isEmpty(tmp.outfile))
                            continue;
                        fileResults.add(tmp.outfile);
                    }
                    result.success = compressResult.success;
                } else {
                    result.success = false;
                    result.throwable = new RuntimeException("the compress result is null!");
                }
                result.outfiles = fileResults;
            } catch (Exception e) {
                result.success = false;
                result.throwable = e;
            }
        }

        return result;
    }

    private FileWithBitmapBatchResult implWithReturnBitmapSync() {
        FileWithBitmapBatchResult result = new FileWithBitmapBatchResult();

        if (mSource == null) {
            result.success = false;
            result.throwable = new RuntimeException("the source is null!");
            return result;
        }

        boolean shouldReturnBitmap = true;

        if (mCompressOptions == null)
            mCompressOptions = new Tiny.FileCompressOptions();

        if (mSourceType == SourceType.FILE_ARRAY) {
            File[] files = (File[]) mSource;
            try {
                BatchCompressResult compressResult = new FileCompressCallableTasks.FileArrayAsFileCallable(mCompressOptions, shouldReturnBitmap, files).call();
                List<FileWithBitmapResult> fileWithBitmapResults = new ArrayList<>();
                if (compressResult != null && compressResult.results != null) {
                    for (int i = 0; i < compressResult.results.length; i++) {
                        CompressResult tmp = compressResult.results[i];
                        if (tmp == null)
                            continue;
                        FileWithBitmapResult value = new FileWithBitmapResult();
                        value.outfile = tmp.outfile;
                        value.bitmap = tmp.bitmap;
                        fileWithBitmapResults.add(value);
                    }
                    result.success = compressResult.success;
                } else {
                    result.success = false;
                    result.throwable = new RuntimeException("the compress result is null!");
                }
                result.results = fileWithBitmapResults;
            } catch (Exception e) {
                result.success = false;
                result.throwable = e;
            }
        } else if (mSourceType == SourceType.BITMAP_ARRAY) {
            Bitmap[] bitmaps = (Bitmap[]) mSource;
            try {
                BatchCompressResult compressResult = new FileCompressCallableTasks.BitmapArrayAsFileCallable(mCompressOptions, shouldReturnBitmap, bitmaps).call();
                List<FileWithBitmapResult> fileWithBitmapResults = new ArrayList<>();
                if (compressResult != null && compressResult.results != null) {
                    for (int i = 0; i < compressResult.results.length; i++) {
                        CompressResult tmp = compressResult.results[i];
                        if (tmp == null)
                            continue;
                        FileWithBitmapResult value = new FileWithBitmapResult();
                        value.outfile = tmp.outfile;
                        value.bitmap = tmp.bitmap;
                        fileWithBitmapResults.add(value);
                    }
                    result.success = compressResult.success;
                } else {
                    result.success = false;
                    result.throwable = new RuntimeException("the compress result is null!");
                }
                result.results = fileWithBitmapResults;
            } catch (Exception e) {
                result.success = false;
                result.throwable = e;
            }
        } else if (mSourceType == SourceType.URI_ARRAY) {
            Uri[] uris = (Uri[]) mSource;
            try {
                BatchCompressResult compressResult = new FileCompressCallableTasks.UriArrayAsFileCallable(mCompressOptions, shouldReturnBitmap, uris).call();
                List<FileWithBitmapResult> fileWithBitmapResults = new ArrayList<>();
                if (compressResult != null && compressResult.results != null) {
                    for (int i = 0; i < compressResult.results.length; i++) {
                        CompressResult tmp = compressResult.results[i];
                        if (tmp == null)
                            continue;
                        FileWithBitmapResult value = new FileWithBitmapResult();
                        value.outfile = tmp.outfile;
                        value.bitmap = tmp.bitmap;
                        fileWithBitmapResults.add(value);
                    }
                    result.success = compressResult.success;
                } else {
                    result.success = false;
                    result.throwable = new RuntimeException("the compress result is null!");
                }
                result.results = fileWithBitmapResults;
            } catch (Exception e) {
                result.success = false;
                result.throwable = e;
            }
        } else if (mSourceType == SourceType.RES_ID_ARRAY) {
            int[] resIds = (int[]) mSource;
            try {
                BatchCompressResult compressResult = new FileCompressCallableTasks.ResourceArrayAsFileCallable(mCompressOptions, shouldReturnBitmap, resIds).call();
                List<FileWithBitmapResult> fileWithBitmapResults = new ArrayList<>();
                if (compressResult != null && compressResult.results != null) {
                    for (int i = 0; i < compressResult.results.length; i++) {
                        CompressResult tmp = compressResult.results[i];
                        if (tmp == null)
                            continue;
                        FileWithBitmapResult value = new FileWithBitmapResult();
                        value.outfile = tmp.outfile;
                        value.bitmap = tmp.bitmap;
                        fileWithBitmapResults.add(value);
                    }
                    result.success = compressResult.success;
                } else {
                    result.success = false;
                    result.throwable = new RuntimeException("the compress result is null!");
                }
                result.results = fileWithBitmapResults;
            } catch (Exception e) {
                result.success = false;
                result.throwable = e;
            }
        }

        return result;
    }

    private void impl(Callback callback) {
        if (mSource == null) {
            if (callback instanceof FileBatchCallback) {
                ((FileBatchCallback) callback).callback(false, null, new RuntimeException("the source is null!"));
            } else if (callback instanceof FileWithBitmapBatchCallback) {
                ((FileWithBitmapBatchCallback) callback).callback(false, null, null, new RuntimeException("the source is null!"));
            }
            return;
        }

        boolean shouldReturnBitmap = false;

        if (callback != null && callback instanceof FileWithBitmapBatchCallback)
            shouldReturnBitmap = true;

        if (mCompressOptions == null)
            mCompressOptions = new Tiny.FileCompressOptions();

        if (mSourceType == SourceType.FILE_ARRAY) {
            File[] files = (File[]) mSource;
            CompressExecutor.getExecutor()
                    .execute(new CompressFutureTask<BatchCompressResult>(
                            new FileCompressCallableTasks.FileArrayAsFileCallable(mCompressOptions, shouldReturnBitmap, files)
                            , new DefaultCallbackDispatcher<BatchCompressResult>(callback)
                    ));
        } else if (mSourceType == SourceType.BITMAP_ARRAY) {
            Bitmap[] bitmaps = (Bitmap[]) mSource;
            CompressExecutor.getExecutor()
                    .execute(new CompressFutureTask<BatchCompressResult>(
                            new FileCompressCallableTasks.BitmapArrayAsFileCallable(mCompressOptions, shouldReturnBitmap, bitmaps)
                            , new DefaultCallbackDispatcher<BatchCompressResult>(callback)
                    ));

        } else if (mSourceType == SourceType.URI_ARRAY) {
            Uri[] uris = (Uri[]) mSource;
            CompressExecutor.getExecutor()
                    .execute(new CompressFutureTask<BatchCompressResult>(
                            new FileCompressCallableTasks.UriArrayAsFileCallable(mCompressOptions, shouldReturnBitmap, uris)
                            , new DefaultCallbackDispatcher<BatchCompressResult>(callback)
                    ));
        } else if (mSourceType == SourceType.RES_ID_ARRAY) {
            int[] resIds = (int[]) mSource;
            CompressExecutor.getExecutor()
                    .execute(new CompressFutureTask<BatchCompressResult>(
                            new FileCompressCallableTasks.ResourceArrayAsFileCallable(mCompressOptions, shouldReturnBitmap, resIds)
                            , new DefaultCallbackDispatcher<BatchCompressResult>(callback)
                    ));
        }
    }
}
