package com.jeesite.modules.other.socket;

import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.AckCallback;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.corundumstudio.socketio.protocol.Packet;
import com.corundumstudio.socketio.protocol.PacketType;
import com.jeesite.modules.entity.EPCTag;
import com.jeesite.modules.other.utils.ReaderUtil;
import com.jeesite.modules.service.ArchivesService;
import io.socket.client.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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

    private static final Logger logger = LoggerFactory.getLogger(MessageEventHandler.class);

    @Autowired
    public MessageEventHandler(SocketIOServer server) {
        this.server = server;
    }

    //添加connect事件，当客户端发起连接时调用
    @OnConnect
    public void onConnect(SocketIOClient client) {
        if (client != null) {
            String applicationId = client.getHandshakeData().getSingleUrlParam("appid");
            logger.info("连接成功, applicationId=" + applicationId + ", sessionId=" + client.getSessionId().toString());
            client.joinRoom(applicationId);

        } else {
            logger.error("客户端为空");
        }
    }

    //添加@OnDisconnect事件，客户端断开连接时调用，刷新客户端信息
    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        String applicationId = client.getHandshakeData().getSingleUrlParam("appid");
        logger.info("客户端断开连接, applicationId=" + applicationId + ", sessionId=" + client.getSessionId().toString());
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
                //TODO 待优化 查找名字
                for (EPCTag epcTag : t.sets) {
                    epcTag.setName(archivesService.getNameByEpc(epcTag.getEpc()));
                }
                jsonObject.put("tags", t.sets);
            } else {
                ReaderUtil.readers.remove(key);
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

    /**
     * 报告地址接口
     *
     * @param client     客户端
     * @param ackRequest 回执消息
     * @param param      报告地址参数
     */
    @OnEvent(value = "doReport")
    public void onDoReport(SocketIOClient client, AckRequest ackRequest, ReportParam param) {

    }

}