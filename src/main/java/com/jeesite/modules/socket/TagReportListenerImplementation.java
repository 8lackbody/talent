package com.jeesite.modules.socket;

import com.alibaba.fastjson.JSONObject;
import com.impinj.octanesdk.ImpinjReader;
import com.impinj.octanesdk.Tag;
import com.impinj.octanesdk.TagReport;
import com.impinj.octanesdk.TagReportListener;
import com.jeesite.modules.entity.EPCTag;
import com.jeesite.modules.entity.Record;
import com.jeesite.modules.service.ArchivesService;
import com.jeesite.modules.service.RecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class TagReportListenerImplementation implements TagReportListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private LocalDateTime time;

    private HashSet<EPCTag> sets;

    private Integer warehouseId;

    private SocketServer socketServer;

    @Autowired
    private RecordService recordService;

    @Autowired
    private ArchivesService archivesService;

    public TagReportListenerImplementation(Integer warehouseId) {
        this.warehouseId = warehouseId;
        sets = new HashSet<>();
        time = LocalDateTime.now();
        socketServer = SocketServer.getInstance();
    }

    @Override
    public void onTagReported(ImpinjReader reader, TagReport report) {
        List<Tag> tags = report.getTags();
        for (Tag t : tags) {
            //判断扫描到当前标签的时间距离上次扫描到标签有无60秒
            //如果在六十秒内，算作一次扫描
            if (!LocalDateTime.now().minusSeconds(20).isBefore(time)) {
                //上次的数据存到数据库里去
                try {
                    List<Record> list = new ArrayList<>();
                    for (EPCTag epcTag : sets) {
                        Record record = new Record();
                        record.setEpc(epcTag.getEpc());
                        record.setWarehouseId(warehouseId);
                        record.setConfirmStatus(1);
                        record.setRecordTime(Date.from(epcTag.getDate().atZone(ZoneId.systemDefault()).toInstant()));
                        list.add(record);
                    }
                    recordService.saveList(list);
                    logger.info(new Date() + ":本次保存记录成功，记录数：" + list.size());
                } catch (Exception e) {
                    logger.info(new Date() + ":本次保存记录失败，失败异常:");
                    logger.info(e.getMessage());
                }
                sets.clear();
                time = LocalDateTime.now();
            }
            //从数据库里查出来epc的名字
            String epc = t.getEpc().toString();
            EPCTag epcTag = new EPCTag(LocalDateTime.now(), epc, archivesService.getNameByEpc(epc), "未确认");
            sets.add(epcTag);
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("date", epcTag.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                jsonObject.put("epc", epcTag.getEpc());
                jsonObject.put("name", epcTag.getName());
                jsonObject.put("status", "未确认");
                socketServer.push(jsonObject.toJSONString(), "zht");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
