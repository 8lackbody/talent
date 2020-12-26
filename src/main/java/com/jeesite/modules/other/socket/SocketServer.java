package com.jeesite.modules.other.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;


@Component
public class SocketServer implements Runnable {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static SocketServer instance;

    // 实例化一个list,用于保存所有的socket客户端
    public static Map<String, Socket> map = new HashMap<>();

    ServerSocket serverSocket;

    private SocketServer() {

    }

    public static SocketServer getInstance() {
        if (instance == null) {
            instance = new SocketServer();
        }
        return instance;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(9999);
            // 创建绑定到特定端口的服务器套接字
            logger.info("socket服务端启动成功");
            // 循环监听客户端连接
            while (true) {
                Socket socket = serverSocket.accept();
                InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String userName = bufferedReader.readLine();
                // 每接受一个线程，就随机生成一个一个新用户
                logger.info(userName + " 登录成功···");
                map.put(userName, socket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void push(String text, String name) throws IOException {
        if (map.get(name) != null) {
            PrintWriter pw = new PrintWriter(map.get(name).getOutputStream());
            pw.println(text);
            pw.flush();
        }
    }

}
