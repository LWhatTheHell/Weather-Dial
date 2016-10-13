package com.leommxj.zd;

/**
 * Created by wolf on 2016/9/30.
 *
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class HttpRequestUtil {

    /**
     * 从url请求中获得返回的字符串
     * @param requestUrl String
     * @return JSON字符串
     */
    static String HttpRequest(String requestUrl) {
        StringBuilder sb = new StringBuilder();
        InputStream ips = getInputStream(requestUrl);
        InputStreamReader isreader = null;
        try {
            isreader = new InputStreamReader(ips, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        BufferedReader bufferedReader = null;
        if (isreader != null) {
            bufferedReader = new BufferedReader(isreader);
        }
        String temp;
        try {
            if (bufferedReader != null) {
                while ((temp = bufferedReader.readLine()) != null) {
                    sb.append(temp);
                }
                bufferedReader.close();
            }
            if (isreader != null) {
                isreader.close();
            }
            ips.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 从请求的URL中获取返回的流数据
     * @param requestUrl String
     * @return InputStream
     */
    private static InputStream getInputStream(String requestUrl) {
        URL url = null;
        HttpURLConnection conn = null;
        InputStream in = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            if (url != null) {
                conn = (HttpURLConnection) url.openConnection();
            }
            if (conn != null) {
                conn.setDoInput(true);
                conn.setRequestMethod("GET");
                conn.connect();
                in = conn.getInputStream();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }
}
