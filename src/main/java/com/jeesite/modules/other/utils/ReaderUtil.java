package com.jeesite.modules.other.utils;

import com.jeesite.modules.entity.Warehouse;
import com.jeesite.modules.other.socket.TagReader;
import com.jeesite.modules.service.WarehouseService;

import java.util.*;

public class ReaderUtil {

    public static Map<String, TagReader> readers = new HashMap<>();

    public static void startReader(String warehouseId) {
        TagReader tagReader = readers.get(warehouseId);
        if (tagReader != null) {
            tagReader.start();
        }
    }

    public static void stopReader(String warehouseId) {
        TagReader tagReader = readers.get(warehouseId);
        if (tagReader != null) {
            tagReader.stop();
        }
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
                    TagReader tagReader = new TagReader(warehouse);
                    readers.put(warehouse.getWarehouseId(), tagReader);

                    tagReader.start();
                }
            }
        }, 10000);
    }

    public static void stopAllReader() {
        readers.forEach((s, tagReader) -> tagReader.stop());
    }

    public static boolean getReaderStatus(String warehouseId) {
        TagReader tagReader = readers.get(warehouseId);
        if (tagReader == null) {
            return false;
        }
        return tagReader.getReaderStatus();
    }


}
