package com.gky.io.netty.utils;

import java.io.Serializable;
import java.util.Map;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-22 19:46
 **/
public class HttpCallerConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String DEFAULT_CONFIG_CHARSET = "UTF-8";
    public static final int DEFAULT_CONFIG_CONN_TIMEOUT = 30000;
    public static final int DEFAULT_CONFIG_READ_TIMEOUT = 30000;
    public static final int DEFAULT_CONFIG_MAX_BUFFER_SIZE = 500;
    private String url;
    private String method;
    private String charset = "UTF-8";
    private int connTimeout = 30000;
    private int readTimeout = 30000;
    private int maxBufferSize = 500;
    private Map<String, String> params;
    private boolean isStream;

    public HttpCallerConfig() {
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCharset() {
        return this.charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public int getConnTimeout() {
        return this.connTimeout;
    }

    public void setConnTimeout(int connTimeout) {
        this.connTimeout = connTimeout;
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getMaxBufferSize() {
        return this.maxBufferSize;
    }

    public void setMaxBufferSize(int maxBufferSize) {
        this.maxBufferSize = maxBufferSize;
    }

    public Map<String, String> getParams() {
        return this.params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public boolean isStream() {
        return this.isStream;
    }

    public void setStream(boolean isStream) {
        this.isStream = isStream;
    }

    public String toString() {
        return "HttpCallerConfig [url=" + this.url + ", method=" + this.method + ", charset=" + this.charset + ", connTimeout=" + this.connTimeout + ", readTimeout=" + this.readTimeout + ", maxBufferSize=" + this.maxBufferSize + ", params=" + (this.params == null ? null : this.params.size()) + "]";
    }
}