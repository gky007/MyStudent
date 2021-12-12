package com.gky.io.netty.utils;


import java.io.IOException;
import java.util.Map;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-22 19:42
 **/
public class HttpCallerUtils {
    private HttpCallerUtils() {
    }

    public static String request(String url, Map<String, String> params) throws IOException {
        HttpCaller c = new HttpCaller(url, "POST", params);
        return (String)c.request(String.class);
    }

    public static String get(String url, Map<String, String> params) throws IOException {
        HttpCaller c = new HttpCaller(url, "GET", params);
        return (String)c.request(String.class);
    }

    public static byte[] getStream(String url, Map<String, String> params) throws IOException {
        HttpCaller c = new HttpCaller(url, "GET", true, params);
        return (byte[])c.request(Byte.class);
    }

    public static String post(String url, Map<String, String> params) throws IOException {
        HttpCaller c = new HttpCaller(url, "POST", params);
        return (String)c.request(String.class);
    }

    public static byte[] postStream(String url, Map<String, String> params) throws IOException {
        HttpCaller c = new HttpCaller(url, "POST", true, params);
        return (byte[])c.request(Byte.class);
    }

    public static String put(String url, Map<String, String> params) throws IOException {
        HttpCaller c = new HttpCaller(url, "PUT", params);
        return (String)c.request(String.class);
    }

    public static String delete(String url, Map<String, String> params) throws IOException {
        HttpCaller c = new HttpCaller(url, "DELETE", params);
        return (String)c.request(String.class);
    }
}
