package com.cellasoft.ifoundit.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.cellasoft.ifoundit.utils.LogUtils.LOGD;
import static com.cellasoft.ifoundit.utils.LogUtils.LOGE;

public class Utils {

    private static HttpURLConnection buildConnection(String url) throws IOException {
        return buildConnection(url, null);
    }

    private static HttpURLConnection buildConnection(String url, String cookies) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();

        if (cookies != null && cookies.length() > 0) {
            conn.setRequestProperty("Cookie", cookies);
        }
        conn.setReadTimeout(5000);
        conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
        conn.addRequestProperty("User-Agent", "Mozilla");
        conn.addRequestProperty("Referer", "google.com");

        return conn;
    }


    public static BufferedInputStream getStreamFromUrl(String url, int size) {
        boolean redirect = false;

        try {
            HttpURLConnection conn = buildConnection(url);

            int status = conn.getResponseCode();

            if (status != HttpURLConnection.HTTP_OK) {
                if (status == HttpURLConnection.HTTP_MOVED_TEMP
                        || status == HttpURLConnection.HTTP_MOVED_PERM
                        || status == HttpURLConnection.HTTP_SEE_OTHER) {
                    redirect = true;
                } else {
                    throw new ConnectException("Status code " + status);
                }
            }

            LOGE("Utils", "Request URL ... " + url);

            if (redirect) {
                // get redirect url from "location" header field
                url = conn.getHeaderField("Location");
                // get the cookie if need, for login
                String cookies = conn.getHeaderField("Set-Cookie");
                // open the new connnection again
                conn = buildConnection(url, cookies);

                LOGD("Connection", "Redirect to URL : " + url);
            }

            status = conn.getResponseCode();

            if (status != HttpURLConnection.HTTP_OK) {
                throw new ConnectException("Status code " + status);
            }

            return new BufferedInputStream(conn.getInputStream(), size);
        } catch (IOException e) {
            LOGE("Utils", "Connection error to [" + url + "]: " + e.getMessage());
        }

        return null;
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.length() == 0;
    }

}
