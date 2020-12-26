package com.jeesite.modules.other.socket;

import com.alibaba.fastjson.JSONObject;
import com.impinj.octanesdk.ImpinjReader;
import com.impinj.octanesdk.Tag;
import com.impinj.octanesdk.TagReport;
import com.impinj.octanesdk.TagReportListener;
import com.jeesite.modules.entity.EPCTag;
import com.jeesite.modules.entity.Record;
import com.jeesite.modules.other.utils.ReaderUtil;
import com.jeesite.modules.other.utils.SpringContextHolder;
import com.jeesite.modules.service.ArchivesService;
import com.jeesite.modules.service.RecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 *
 */
public class TagReportListenerImplementation implements TagReportListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private HashSet<EPCTag> sets;

    private String warehouseId;

    private SocketServer socketServer;

    private boolean isNewRecord = true;

    private RecordService recordService = SpringContextHolder.getBean(RecordService.class);

    private ArchivesService archivesService = SpringContextHolder.getBean(ArchivesService.class);

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YY-MM-dd HH:mm:ss");


    Timer timer = new Timer();

    // 心跳 发送广告机对应reader状态和读取到的数据的set
    TimerTask heartBeat = new TimerTask() {
        @Override
        public void run() {
            boolean readerStatus = ReaderUtil.getReaderStatus(warehouseId);
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("readerStatus", readerStatus);
                jsonObject.put("tags", sets);
                socketServer.push(jsonObject.toJSONString(), warehouseId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    // 扫描事件计时器，六十秒后保存数据
    TimerTask saveDataTask = new TimerTask() {
        @Override
        public void run() {
            saveData();
            sets.clear();
            isNewRecord = true;
        }
    };

    public TagReportListenerImplementation(String warehouseId) {
        this.warehouseId = warehouseId;
        sets = new HashSet<>();
        socketServer = SocketServer.getInstance();
        timer.schedule(heartBeat, 10000, 1000);
    }

    @Override
    public void onTagReported(ImpinjReader reader, TagReport report) {
        List<Tag> tags = report.getTags();
        for (Tag t : tags) {
            if (isNewRecord) {
                isNewRecord = false;
                timer.schedule(saveDataTask, 60000);
            }
            //从数据库里查出来epc的名字
            String epc = t.getEpc().toString();
            //TODO alert 状态也需要查询出来
            EPCTag epcTag = new EPCTag(LocalDateTime.now().format(DateTimeFormatter.ofPattern("YY-MM-dd HH:mm:ss"))
                    , epc, archivesService.getNameByEpc(epc), "未确认", 1);
            sets.add(epcTag);
        }
    }

    /**
     * 保存set中的数据到数据库
     */
    public void saveData() {
        try {
            List<Record> list = new ArrayList<>();
            for (EPCTag epcTag : sets) {
                Record record = new Record();
                record.setEpc(epcTag.getEpc());
                record.setWarehouseId(warehouseId);
                record.setName(epcTag.getName());
                record.setConfirmStatus(1);
                record.setRecordTime(simpleDateFormat.parse(epcTag.getDate()));
                list.add(record);
            }
            recordService.saveList(list);
            logger.info(new Date() + ":本次保存记录成功，记录数：" + list.size());
        } catch (Exception e) {
            logger.info(new Date() + ":本次保存记录失败，失败异常:");
            logger.info(e.getMessage());
        }
    }
}
