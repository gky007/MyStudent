package com.gky.io.netty.utils;


import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-22 19:48
 **/
public class HttpProxy {
    private static CloseableHttpClient httpClient;
    private static final String CONTENT_TYPE_JSON = "application/json";

    static {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(HttpProxyConfig.MAX_TOTAL_CONNECTIONS);
        cm.setDefaultMaxPerRoute(HttpProxyConfig.MAX_ROUTE_CONNECTIONS);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(HttpProxyConfig.CONNECT_TIMEOUT).setConnectTimeout(HttpProxyConfig.CONNECT_TIMEOUT).build();
        httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).setConnectionManager(cm).build();
    }

    public HttpProxy() {
    }

    public static HttpProxy getInstance() {
        return HttpProxy.SingletonHolder.instance;
    }

    public static HttpClient getHttpClient() {
        return httpClient;
    }

    public static byte[] get4Stream(String requestUrl) throws Exception {
        byte[] ret = null;
        HttpGet httpGet = new HttpGet(requestUrl);
        CloseableHttpResponse response = httpClient.execute(httpGet);

        byte[] var8;
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                long len = entity.getContentLength();
                ret = EntityUtils.toByteArray(entity);
                EntityUtils.consume(entity);
            }

            var8 = ret;
        } finally {
            response.close();
        }

        return var8;
    }

    public static String get4String(String requestUrl) throws Exception {
        String ret = null;
        HttpGet httpGet = new HttpGet(requestUrl);
        CloseableHttpResponse response = httpClient.execute(httpGet);

        String var8;
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                long len = entity.getContentLength();
                ret = EntityUtils.toString(entity);
                EntityUtils.consume(entity);
            }

            var8 = ret;
        } finally {
            response.close();
        }

        return var8;
    }

    public static String post(String requestUrl, String requestContent) throws IOException {
        StringEntity requestEntity = new StringEntity(requestContent, Consts.UTF_8);
        return execute(requestUrl, requestEntity);
    }

    public static String postJson(String requestUrl, String jsonContent) throws IOException {
        StringEntity requestEntity = new StringEntity(jsonContent, Consts.UTF_8);
        requestEntity.setContentEncoding("UTF-8");
        requestEntity.setContentType("application/json");
        return execute(requestUrl, requestEntity);
    }

    public static String post(String requestUrl, Map<String, String> params) throws IOException {
        List<NameValuePair> nvps = new ArrayList();
        if (params != null) {
            Iterator var4 = params.entrySet().iterator();

            while(var4.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry)var4.next();
                nvps.add(new BasicNameValuePair((String)entry.getKey(), (String)entry.getValue()));
            }
        }

        EntityBuilder builder = EntityBuilder.create();
        builder.setParameters(nvps);
        HttpEntity httpEntity = builder.build();
        return execute(requestUrl, httpEntity);
    }

    public static String upload(String requestUrl, String localFile, String username, String password) throws IOException {
        new HttpPost(requestUrl);
        FileBody fileBody = new FileBody(new File(localFile));
        StringBody usernameInp = new StringBody(username, ContentType.create("text/plain", Consts.UTF_8));
        StringBody passwordInp = new StringBody(password, ContentType.create("text/plain", Consts.UTF_8));
        HttpEntity httpEntity = MultipartEntityBuilder.create().addPart("file", fileBody).addPart("username", usernameInp).addPart("password", passwordInp).build();
        return execute(requestUrl, httpEntity);
    }

    private static String execute(String requestUrl, HttpEntity httpEntity) throws IOException {
        String result = null;
        HttpPost httpPost = new HttpPost(requestUrl);
        httpPost.setEntity(httpEntity);

        try {
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);

            try {
                HttpEntity entity = httpResponse.getEntity();
                if (httpResponse.getStatusLine().getReasonPhrase().equals("OK") && httpResponse.getStatusLine().getStatusCode() == 200) {
                    result = EntityUtils.toString(entity, "UTF-8");
                }

                EntityUtils.consume(entity);
            } finally {
                if (httpResponse != null) {
                    httpResponse.close();
                }

            }
        } finally {
            if (httpPost != null) {
                httpPost.releaseConnection();
            }

        }

        return result;
    }

    private static class SingletonHolder {
        static final HttpProxy instance = new HttpProxy();

        private SingletonHolder() {
        }
    }
}
