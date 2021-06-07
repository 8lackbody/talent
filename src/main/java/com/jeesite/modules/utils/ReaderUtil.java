package com.jeesite.modules.utils;

import com.impinj.octanesdk.OctaneSdkException;
import com.jeesite.modules.common.SpringContextHolder;
import com.jeesite.modules.rds.entity.Warehouse;
import com.jeesite.modules.socket.TagReader;
import com.jeesite.modules.rds.service.WarehouseService;

import java.util.*;

public class ReaderUtil {

    public static Map<String, TagReader> readers = new HashMap<>();

    public static boolean restart(Warehouse warehouse) {
        TagReader tagReader = readers.get(warehouse.getWarehouseId());
        if (tagReader != null) {  //启动的reader
            tagReader.stop();
            tagReader.disconnect();
        }
        if (warehouse.getAntenna1Enable() == 1 || warehouse.getAntenna2Enable() == 1) {
            try {
                tagReader = new TagReader(warehouse);
            } catch (OctaneSdkException e) {
                e.printStackTrace();
                return false;
            }
            readers.put(warehouse.getWarehouseId(), tagReader);
        }
        return true;
    }

    public static void startAllReader() {
        // 60秒过后，遍历仓库表里的所有仓库，开启对应reader
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                WarehouseService warehouseService = SpringContextHolder.getBean(WarehouseService.class);
                List<Warehouse> list = warehouseService.findAll();
                for (Warehouse warehouse : list) {
                    restart(warehouse);
                }
            }
        }, 30000);
    }

    public static boolean getReaderStatus(String warehouseId) {
        TagReader tagReader = readers.get(warehouseId);
        if (tagReader == null) {
            return false;
        }
        return tagReader.getReaderStatus();
    }

}
