package com.jeesite.modules.socket;

import com.impinj.octanesdk.ImpinjReader;
import com.impinj.octanesdk.Tag;
import com.impinj.octanesdk.TagReport;
import com.impinj.octanesdk.TagReportListener;
import com.jeesite.modules.common.SpringContextHolder;
import com.jeesite.modules.rds.entity.EPCTag;
import com.jeesite.modules.rds.entity.Record;
import com.jeesite.modules.rds.service.RecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 *
 */
public class TagReportListenerImplementation implements TagReportListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    CopyOnWriteArraySet<EPCTag> sets;

    private String warehouseId;

    private boolean isNewRecord = true;

    private RecordService recordService = SpringContextHolder.getBean(RecordService.class);

    Timer timer = new Timer();

    public TagReportListenerImplementation(String warehouseId) {
        this.warehouseId = warehouseId;
        sets = new CopyOnWriteArraySet<>();
    }

    @Override
    public void onTagReported(ImpinjReader reader, TagReport report) {
        List<Tag> tags = report.getTags();
        for (Tag t : tags) {
            if (isNewRecord) {
                isNewRecord = false;
                // 扫描事件计时器，六十秒后保存数据
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        saveData();
                        sets.clear();
                        isNewRecord = true;
                    }
                }, 60000);
            }
            sets.add(new EPCTag(t.getEpc().toString()));
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
                record.setRecordTime(epcTag.getDate());
                record.setAlarmStatus(epcTag.getAlert());
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
