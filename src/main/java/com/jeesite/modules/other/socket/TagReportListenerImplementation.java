package com.jeesite.modules.other.socket;

import com.impinj.octanesdk.ImpinjReader;
import com.impinj.octanesdk.Tag;
import com.impinj.octanesdk.TagReport;
import com.impinj.octanesdk.TagReportListener;
import com.jeesite.modules.entity.EPCTag;
import com.jeesite.modules.entity.Record;
import com.jeesite.modules.other.utils.SpringContextHolder;
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

    HashSet<EPCTag> sets;

    private String warehouseId;

    private boolean isNewRecord = true;

    private RecordService recordService = SpringContextHolder.getBean(RecordService.class);

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm");

    Timer timer = new Timer();

    public TagReportListenerImplementation(String warehouseId) {
        this.warehouseId = warehouseId;
        sets = new HashSet<>();
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
            //从数据库里查出来epc的名字
            String epc = t.getEpc().toString();
            //TODO alert 状态也需要查询出来 在发送的时候查  不在这里
            EPCTag epcTag;
            if (epc.replace(" ", "").equals("666645000004")) {
                epcTag = new EPCTag(LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm"))
                        , epc, "", "未确认", 1);
            } else {
                epcTag = new EPCTag(LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm"))
                        , epc, "", "未确认", 0);
            }
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
