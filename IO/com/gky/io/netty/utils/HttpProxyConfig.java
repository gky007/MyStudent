package com.gky.io.netty.utils;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-22 19:48
 **/
public class HttpProxyConfig {
    public static int MAX_TOTAL_CONNECTIONS = 800;
    public static int MAX_ROUTE_CONNECTIONS = 400;
    public static int CONNECT_TIMEOUT = 10000;
    public static int READ_TIMEOUT = 10000;

    public HttpProxyConfig() {
    }
}
