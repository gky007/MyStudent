package com.gky.io.netty.heartbeat;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-21 23:53
 **/
public class ClienHeartBeattHandler extends ChannelHandlerAdapter {
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> heartBeat;
    private InetAddress addr;
    private static final String SUCCESS_KEY = "auth_success_key";

    public ClienHeartBeattHandler() {
    }

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.addr = InetAddress.getLocalHost();
        String ip = this.addr.getHostAddress();
        String key = "1234";
        String auth = ip + "," + key;
        ctx.writeAndFlush(auth);
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg instanceof String) {
                String ret = (String)msg;
                if ("auth_success_key".equals(ret)) {
                    this.heartBeat = this.scheduler.scheduleWithFixedDelay(new ClienHeartBeattHandler.HeartBeatTask(ctx), 0L, 2L, TimeUnit.SECONDS);
                    System.out.println(msg);
                } else {
                    System.out.println(msg);
                }
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }

    }

    private class HeartBeatTask implements Runnable {
        private final ChannelHandlerContext ctx;

        public HeartBeatTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        public void run() {
            try {
                RequestInfo info = new RequestInfo();
                info.setIp(ClienHeartBeattHandler.this.addr.getHostAddress());
                Sigar sigar = new Sigar();
                CpuPerc cpuPerc = sigar.getCpuPerc();
                HashMap<String, Object> cpuPercMap = new HashMap();
                cpuPercMap.put("combined", cpuPerc.getCombined());
                cpuPercMap.put("user", cpuPerc.getUser());
                cpuPercMap.put("sys", cpuPerc.getSys());
                cpuPercMap.put("wait", cpuPerc.getWait());
                cpuPercMap.put("idle", cpuPerc.getIdle());
                Mem mem = sigar.getMem();
                HashMap<String, Object> memoryMap = new HashMap();
                memoryMap.put("total", mem.getTotal() / 1024L);
                memoryMap.put("used", mem.getUsed() / 1024L);
                memoryMap.put("free", mem.getFree() / 1024L);
                info.setCpuPercMap(cpuPercMap);
                info.setMemoryMap(memoryMap);
                this.ctx.writeAndFlush(info);
            } catch (Exception var7) {
                var7.printStackTrace();
            }

        }

        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            if (ClienHeartBeattHandler.this.heartBeat != null) {
                ClienHeartBeattHandler.this.heartBeat.cancel(true);
                ClienHeartBeattHandler.this.heartBeat = null;
            }

            ctx.fireExceptionCaught(cause);
        }
    }
}
