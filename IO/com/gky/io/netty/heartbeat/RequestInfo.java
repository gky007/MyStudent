package com.gky.io.netty.heartbeat;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-21 23:51
 **/
public class RequestInfo implements Serializable {
    private String ip;
    private HashMap<String, Object> cpuPercMap;
    private HashMap<String, Object> memoryMap;

    public RequestInfo() {
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public HashMap<String, Object> getCpuPercMap() {
        return this.cpuPercMap;
    }

    public void setCpuPercMap(HashMap<String, Object> cpuPercMap) {
        this.cpuPercMap = cpuPercMap;
    }

    public HashMap<String, Object> getMemoryMap() {
        return this.memoryMap;
    }

    public void setMemoryMap(HashMap<String, Object> memoryMap) {
        this.memoryMap = memoryMap;
    }
}
