package com.zxy.tiny.common;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.zxy.tiny.Tiny;

/**
 * Created by zhengxiaoyong on 2017/4/2.
 */
public final class UriUtil {

    /**
     * http scheme for URIs.
     */
    public static final String HTTP_SCHEME = "http";

    /**
     * https scheme for URIs.
     */
    public static final String HTTPS_SCHEME = "https";

    /**
     * File scheme for URIs.
     */
    public static final String LOCAL_FILE_SCHEME = "file";

    /**
     * Content URI scheme for URIs.
     */
    public static final String LOCAL_CONTENT_SCHEME = "content";

    /**
     * Asset scheme for URIs.
     */
    public static final String LOCAL_ASSET_SCHEME = "asset";

    /**
     * Resource scheme for URIs.
     */
    public static final String LOCAL_RESOURCE_SCHEME = "res";

    /**
     * Data scheme for URIs.
     */
    public static final String DATA_SCHEME = "data";

    /**
     * /**
     * Check if uri represents network resource.
     *
     * @param uri uri to check.
     * @return true if uri's scheme is equal to "http" or "https".
     */
    public static boolean isNetworkUri(Uri uri) {
        final String scheme = getSchemeOrNull(uri);
        return HTTPS_SCHEME.equals(scheme) || HTTP_SCHEME.equals(scheme);
    }

    /**
     * Check if uri represents local file.
     *
     * @param uri uri to check.
     * @return true if uri's scheme is equal to "file".
     */
    public static boolean isLocalFileUri(Uri uri) {
        final String scheme = getSchemeOrNull(uri);
        return LOCAL_FILE_SCHEME.equals(scheme);
    }

    /**
     * Check if uri represents local content.
     *
     * @param uri uri to check.
     * @return true if uri's scheme is equal to "content".
     */
    public static boolean isLocalContentUri(Uri uri) {
        final String scheme = getSchemeOrNull(uri);
        return LOCAL_CONTENT_SCHEME.equals(scheme);
    }

    /**
     * Check if uri represents local asset.
     *
     * @param uri uri to check.
     * @return true if uri's scheme is equal to "asset".
     */
    public static boolean isLocalAssetUri(Uri uri) {
        final String scheme = getSchemeOrNull(uri);
        return LOCAL_ASSET_SCHEME.equals(scheme);
    }

    /**
     * Check if uri represents local resource.
     *
     * @param uri uri to check.
     * @return true if uri's scheme is equal to {@link #LOCAL_RESOURCE_SCHEME}.
     */
    public static boolean isLocalResourceUri(Uri uri) {
        final String scheme = getSchemeOrNull(uri);
        return LOCAL_RESOURCE_SCHEME.equals(scheme);
    }

    /**
     * Check if the uri is a data uri.
     */
    public static boolean isDataUri(Uri uri) {
        return DATA_SCHEME.equals(getSchemeOrNull(uri));
    }

    /**
     * @param uri uri to extract scheme from, possibly null.
     * @return null if uri is null, result of uri.getScheme() otherwise.
     */
    public static String getSchemeOrNull(Uri uri) {
        return uri == null ? null : uri.getScheme();
    }

    /**
     * A wrapper around {@link Uri#parse} that returns null if the input is null.
     *
     * @param uriAsString the uri as a string.
     * @return the parsed Uri or null if the input was null.
     */
    public static Uri parseUriOrNull(String uriAsString) {
        return uriAsString != null ? Uri.parse(uriAsString) : null;
    }

    /**
     * Get the path of a file from the Uri.
     *
     * @param srcUri The source uri.
     * @return The Path for the file or null if doesn't exists.
     */
    public static String getRealPathFromUri(final Uri srcUri) {
        String result = null;
        if (UriUtil.isLocalContentUri(srcUri)) {
            Cursor cursor = Tiny.getInstance().getApplication().getContentResolver().query(srcUri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(idx);
                cursor.close();
            }
        } else if (UriUtil.isLocalFileUri(srcUri)) {
            result = srcUri.getPath();
        }
        return result;
    }
}
