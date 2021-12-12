package com.gky.io.bio1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @version 1.0.0
 * @description:
 * @author: Mr.G
 * @date: 2021-10-20 19:41
 **/
public class Server {

    final static int PROT = 8765;

    public static void main(String[] args) {

        ServerSocket server = null;
        try {
            server = new ServerSocket(PROT);
            System.out.println(" 服务器已启动，等待客户端发送数据 .. ");
            //进行阻塞
            Socket socket = server.accept();
            System.out.println("socket = " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
            //新建一个线程执行客户端的任务
            new Thread(new ServerHandler(socket)).start();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            server = null;
        }
    }
}
