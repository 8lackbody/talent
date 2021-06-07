package com.jeesite.modules.socket;

import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.jeesite.modules.rds.entity.EPCTag;
import com.jeesite.modules.utils.ReaderUtil;
import com.jeesite.modules.rds.service.ArchivesService;
import com.jeesite.modules.rds.service.WarehouseService;
import io.socket.client.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * 消息事件处理器
 *
 * @version 1.0
 * @since 2018/1015
 */
@Component
public class MessageEventHandler {

    private final SocketIOServer server;

    @Resource
    private ArchivesService archivesService;

    @Resource
    private WarehouseService warehouseService;

    private volatile boolean flag = true;

    private static final Logger logger = LoggerFactory.getLogger(MessageEventHandler.class);

    @Autowired
    public MessageEventHandler(SocketIOServer server) {
        this.server = server;
    }

    //添加connect事件，当客户端发起连接时调用
    @OnConnect
    public void onConnect(SocketIOClient client) {
        if (client != null) {
            String warehouseId = client.getHandshakeData().getSingleUrlParam("warehouseId");
            logger.info("连接成功, warehouseId=" + warehouseId + ", sessionId=" + client.getSessionId().toString());
            client.joinRoom(warehouseId);
            Set<String> allRooms = client.getAllRooms();
            logger.info(allRooms.toString());
        } else {
            logger.error("客户端为空");
        }
    }

    //添加@OnDisconnect事件，客户端断开连接时调用，刷新客户端信息
    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        String warehouseId = client.getHandshakeData().getSingleUrlParam("warehouseId");
        logger.info("客户端断开连接, warehouseId=" + warehouseId + ", sessionId=" + client.getSessionId().toString());
        client.disconnect();
    }

    // 消息接收入口
    @OnEvent(value = Socket.EVENT_MESSAGE)
    public void onEvent(SocketIOClient client, AckRequest ackRequest, Object data) {
        logger.info("接收到客户端消息" + data);
        if (ackRequest.isAckRequested()) {
            String key = String.valueOf(data);
            boolean readerStatus = ReaderUtil.getReaderStatus(key);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("readerStatus", readerStatus);
            if (readerStatus) {
                TagReader tagReader = ReaderUtil.readers.get(key);
                TagReportListenerImplementation t = tagReader.getTagReportListener();
                // TODO 这里需要请求，可以批量请求
                for (EPCTag epcTag : t.sets) {
                    epcTag.setName(archivesService.getNameByEpc(epcTag.getEpc()));
                }
                jsonObject.put("tags", t.sets);
            } else {
                ReaderUtil.readers.remove(key);
                if (flag) {
                    restartReader(key);
                }
            }
            ackRequest.sendAckData(jsonObject);
        }
    }

    // 广播消息接收入口
    @OnEvent(value = "broadcast")
    public void onBroadcast(SocketIOClient client, AckRequest ackRequest, Object data) {
        logger.info("接收到广播消息");
        // 房间广播消息
        String room = client.getHandshakeData().getSingleUrlParam("appid");
        server.getRoomOperations(room).sendEvent("broadcast", "广播啦啦啦啦");
    }

    public void restartReader(String warehouseId) {
        flag = false;
        new Thread(() -> {
            ReaderUtil.restart(warehouseService.get(warehouseId));
            flag = true;
        }).start();
    }

}