package com.jeesite.modules.other.utils;

import com.jeesite.modules.other.socket.TagReader;

import java.util.HashMap;
import java.util.Map;

public class ReaderUtil {

    public static Map<String, TagReader> readers = new HashMap<>();

    public static void startReader(String hostname, String warehouseId){
        TagReader tagReader = new TagReader(hostname,warehouseId);
        tagReader.start();
        readers.put(warehouseId,tagReader);
    }

    public static void stopReader(String warehouseId){
        TagReader tagReader = readers.get(warehouseId);
        tagReader.stop();
    }

    public static void startAllReader(){
        //TODO 60秒过后，遍历仓库表里的所有仓库，开启对应reader
    }

    public static void stopAllReader(){
        //TODO 关闭所有的reader
    }

    public static boolean getReaderStatic(String warehouseId){
        TagReader tagReader = readers.get(warehouseId);
        return tagReader.getReaderStatic();
    }


}
