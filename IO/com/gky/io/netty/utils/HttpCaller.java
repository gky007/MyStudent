package com.gky.io.netty.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-22 19:44
 **/
public class HttpCaller {
    public static final String REQUEST_METHOD_GET = "GET";
    public static final String REQUEST_METHOD_POST = "POST";
    public static final String REQUEST_METHOD_PUT = "PUT";
    public static final String REQUEST_METHOD_DELETE = "DELETE";
    private HttpCallerConfig config;

    public HttpCaller(String url, String method) {
        this.config = new HttpCallerConfig();
        this.config.setUrl(url);
        this.config.setMethod(method);
    }

    public HttpCaller(String url, Map<String, String> params) {
        this.config = new HttpCallerConfig();
        this.config.setUrl(url);
        this.config.setMethod("POST");
        this.config.setParams(params);
    }

    public HttpCaller(String url, String method, Map<String, String> params) {
        this.config = new HttpCallerConfig();
        this.config.setUrl(url);
        this.config.setMethod(method);
        this.config.setParams(params);
    }

    public HttpCaller(String url, String method, boolean isStream, Map<String, String> params) {
        this.config = new HttpCallerConfig();
        this.config.setUrl(url);
        this.config.setMethod(method);
        this.config.setStream(isStream);
        this.config.setParams(params);
    }

    public HttpCaller(HttpCallerConfig config) {
        this.config = config;
    }

    public <T> T request(Object T) throws IOException {
        URL targetUrl = new URL(this.config.getUrl());
        HttpURLConnection conn = (HttpURLConnection)targetUrl.openConnection();
        conn.setRequestMethod(this.config.getMethod());
        conn.setConnectTimeout(this.config.getConnTimeout());
        conn.setReadTimeout(this.config.getReadTimeout());
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.connect();
        this.write(conn);
        return this.config.isStream() ? (T) this.readStream(conn) : (T) this.read(conn);
    }

    private byte[] readStream(HttpURLConnection conn) throws IOException {
        InputStream is = null;
        ByteArrayOutputStream bos = null;

        try {
            is = conn.getInputStream();
            byte[] buf = new byte[this.config.getMaxBufferSize()];
            bos = new ByteArrayOutputStream();

            int num;
            while((num = is.read(buf, 0, buf.length)) != -1) {
                bos.write(buf, 0, num);
            }
        } finally {
            if (bos != null) {
                bos.close();
            }

            if (is != null) {
                is.close();
            }

        }

        return bos.toByteArray();
    }

    private String read(HttpURLConnection conn) throws IOException {
        StringBuffer str = new StringBuffer();
        InputStream is = null;
        InputStreamReader reader = null;

        try {
            is = conn.getInputStream();
            reader = new InputStreamReader(is, this.config.getCharset());
            char[] buffer = new char[this.config.getMaxBufferSize()];
            boolean var6 = false;

            int c;
            while((c = reader.read(buffer)) >= 0) {
                str.append(buffer, 0, c);
            }
        } finally {
            if (reader != null) {
                reader.close();
            }

            if (is != null) {
                is.close();
            }

        }

        return str.length() < 1 ? null : str.toString();
    }

    private void write(HttpURLConnection conn) throws IOException {
        Map<String, String> params = this.config.getParams();
        if (params != null && !params.isEmpty()) {
            OutputStream out = null;
            OutputStreamWriter writer = null;

            try {
                out = conn.getOutputStream();
                writer = new OutputStreamWriter(out, this.config.getCharset());
                Iterator var6 = params.entrySet().iterator();

                while(var6.hasNext()) {
                    Map.Entry<String, String> param = (Map.Entry)var6.next();
                    writer.write("&");
                    writer.write((String)param.getKey());
                    writer.write("=");
                    if (param.getValue() != null) {
                        writer.write((String)param.getValue());
                    }
                }
            } finally {
                if (writer != null) {
                    writer.close();
                }

                if (out != null) {
                    out.close();
                }

            }

        }
    }
}

