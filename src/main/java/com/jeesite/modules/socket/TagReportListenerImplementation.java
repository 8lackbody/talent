package com.jeesite.modules.socket;

import com.alibaba.fastjson.JSONObject;
import com.impinj.octanesdk.ImpinjReader;
import com.impinj.octanesdk.Tag;
import com.impinj.octanesdk.TagReport;
import com.impinj.octanesdk.TagReportListener;
import com.jeesite.modules.entity.EPCTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TagReportListenerImplementation implements TagReportListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static LocalDateTime time;

    private static HashSet<EPCTag> sets;
    int index = 1;

    private SocketServer socketServer;

    public TagReportListenerImplementation(){
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
            if(!LocalDateTime.now().minusSeconds(20).isBefore(time)){
                //TODO 在这里要吧上次的数据存到数据库里去
                logger.info(new Date()+":-----------save------------");
                sets.clear();
                time = LocalDateTime.now();
            }
            //TODO 从数据库里查出来epc的内容
            EPCTag epcTag = new EPCTag(LocalDateTime.now(),t.getEpc().toString(),"张三","未确认");
            sets.add(epcTag);
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("date",epcTag.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                jsonObject.put("epc",epcTag.getEpc());
                jsonObject.put("name",epcTag.getName());
                jsonObject.put("status","未确认");
                index++;
                socketServer.push(jsonObject.toJSONString(),"zht");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
