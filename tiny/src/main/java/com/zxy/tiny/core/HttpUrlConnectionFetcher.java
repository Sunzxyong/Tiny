package com.zxy.tiny.core;

import android.net.Uri;
import android.text.TextUtils;

import com.zxy.tiny.common.TinyException;
import com.zxy.tiny.common.UriUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by zhengxiaoyong on 2017/4/1.
 */
public class HttpUrlConnectionFetcher {

    private static final int MAX_REDIRECTS = 5;

    private static final int HTTP_TEMPORARY_REDIRECT = 307;

    private static final int HTTP_PERMANENT_REDIRECT = 308;

    private static final int TIMEOUT = 10000;

    static {
        try {
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new TrustManager[]{new TinyTrustManager()}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
            //do not verify
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    //for google store reject,see https://support.google.com/faqs/answer/7188426.
                    return !"www.abcdefgzxy.com".equalsIgnoreCase(hostname);
                }
            });
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (Exception e) {
            //ignore...
        }
    }

    public static void fetch(String url, ResponseCallback callback) {
        if (TextUtils.isEmpty(url))
            return;
        Uri uri = Uri.parse(url);
        fetch(uri, callback);
    }

    public static void fetch(Uri uri, ResponseCallback callback) {
        if (!UriUtil.isNetworkUri(uri))
            return;

        HttpURLConnection connection = null;
        InputStream is = null;

        connection = obtainHttpURLConnection(uri, MAX_REDIRECTS);

        if (connection == null)
            return;
        try {
            is = connection.getInputStream();
            if (callback != null)
                callback.callback(is);
        } catch (IOException e) {
            e.printStackTrace();
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
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static HttpURLConnection obtainHttpURLConnection(Uri uri, int maxRedirects) {
        if (uri == null)
            return null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(uri.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(TIMEOUT);
            connection.setReadTimeout(TIMEOUT);
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode >= HttpURLConnection.HTTP_OK &&
                    responseCode < HttpURLConnection.HTTP_MULT_CHOICE)
                return connection;

            if (isHttpRedirect(responseCode)) {
                String nextUriString = connection.getHeaderField("Location");
                connection.disconnect();
                Uri nextUri = nextUriString == null ? null : Uri.parse(nextUriString);
                String originalScheme = uri.getScheme();
                if (maxRedirects > 0 && nextUri != null && !nextUri.getScheme().equals(originalScheme)) {
                    return obtainHttpURLConnection(nextUri, maxRedirects - 1);
                } else {
                    String message = maxRedirects == 0
                            ? "URL %s follows too many redirects, uri:" + uri.toString()
                            : "URL %s returned %d without a valid redirect, uri:" + uri.toString() + ", responseCode:" + responseCode;
                    throw new TinyException.NetworkIOException(message);
                }
            } else {
                connection.disconnect();
                connection = null;
            }

        } catch (MalformedURLException e) {
            connection = null;
            e.printStackTrace();
        } catch (IOException e) {
            connection = null;
            e.printStackTrace();
        } catch (Exception e) {
            connection = null;
            e.printStackTrace();
        }
        return connection;
    }

    private static boolean isHttpRedirect(int responseCode) {
        switch (responseCode) {
            case HttpURLConnection.HTTP_MULT_CHOICE:
            case HttpURLConnection.HTTP_MOVED_PERM:
            case HttpURLConnection.HTTP_MOVED_TEMP:
            case HttpURLConnection.HTTP_SEE_OTHER:
            case HTTP_TEMPORARY_REDIRECT:
            case HTTP_PERMANENT_REDIRECT:
                return true;
            default:
                return false;
        }
    }

    /**
     * Trust all certificates.
     */
    private static final class TinyTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    public interface ResponseCallback {

        void callback(InputStream is);

    }

}
