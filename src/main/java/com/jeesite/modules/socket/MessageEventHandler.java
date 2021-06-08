package com.jeesite.modules.socket;

import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.jeesite.modules.common.config.UrlConfig;
import com.jeesite.modules.common.payload.FilePersonNameOrDetailPayload;
import com.jeesite.modules.rds.entity.EPCTag;
import com.jeesite.modules.rds.service.WarehouseService;
import com.jeesite.modules.utils.HttpUtil;
import com.jeesite.modules.utils.ReaderUtil;
import io.socket.client.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 消息事件处理器
 *
 * @version 1.0
 * @since 2018/1015
 */
@Component
public class MessageEventHandler {

    private final static Logger logger = LoggerFactory.getLogger(MessageEventHandler.class);

    private final SocketIOServer server;

    @Resource
    private WarehouseService warehouseService;

    private volatile boolean flag = true;

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
                // 请求接口获取 名字和状态
                Set<EPCTag> result = getNameAndActivity(t.sets);
                jsonObject.put("tags", result);
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

    /**
     * 获取当前标签的名字和状态
     *
     * @param set 当前的标签集合
     */
    public Set<EPCTag> getNameAndActivity(CopyOnWriteArraySet<EPCTag> set) {
        Set<EPCTag> epcTags = new HashSet<>(set);
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<EPCTag> it = epcTags.iterator();
        while (it.hasNext()) {
            stringBuilder.append(it.next());
            if (it.hasNext()) {
                stringBuilder.append(",");
            }
        }
        Map<String, String> map = new HashMap<>();
        map.put("filePerson.IcCard", stringBuilder.toString());

        FilePersonNameOrDetailPayload filePersonNameOrDetailPayload;
        String response = null;
        try {
            response = HttpUtil.doPost(UrlConfig.URL_NAME, map);
            filePersonNameOrDetailPayload = JSONObject.parseObject(response, FilePersonNameOrDetailPayload.class);
        } catch (Exception e) {
            logger.error("接口返回消息序列化错误；" + response);
            return epcTags;
        }
        // 检查返回对象序列化后是否为空
        if (filePersonNameOrDetailPayload == null) {
            logger.error("接口返回消息为空；" + response);
            return epcTags;
        }
        // 检查接口的返回码是否为0
        if (filePersonNameOrDetailPayload.getErrCode() == null || filePersonNameOrDetailPayload.getErrCode() != 0) {
            logger.error("接口返回错误码不为0；" + filePersonNameOrDetailPayload.getErrMsg());
            return epcTags;
        }
        // 检查返回数据长度是否和已有的长度一样
        List<Map<String, String>> data = filePersonNameOrDetailPayload.getData();
        if (data == null || data.size() != epcTags.size()) {
            logger.error("回数据长度是否和已有的长度不一致；" + filePersonNameOrDetailPayload.getData());
            return epcTags;
        }
        Iterator<EPCTag> EPCTagIterator = epcTags.iterator();
        Iterator<Map<String, String>> mapIterator = data.iterator();
        while (EPCTagIterator.hasNext()){
            EPCTag epcTag = EPCTagIterator.next();
            Map<String, String> payload = mapIterator.next();
            epcTag.setName(String.valueOf(payload.get("NAME")));
            epcTag.setAlert(Integer.valueOf(payload.get("ACTIVITY")));
        }
        return epcTags;
    }


}