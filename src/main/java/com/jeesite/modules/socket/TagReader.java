package com.jeesite.modules.socket;

import com.impinj.octanesdk.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TagReader {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String hostname;
    private String warehouseId;
    private ImpinjReader reader;

    public TagReader(String hostname, String warehouseId) {
        this.hostname = hostname;
        this.warehouseId = warehouseId;
        this.reader = new ImpinjReader();
        connect();
    }

    public void connect(){
        try {
            //TODO 需要从数据库中读取设置，然后启动
            reader.connect(hostname);
            logger.info("Connecting reader success");
            Settings settings = reader.queryDefaultSettings();
            ReportConfig report = settings.getReport();
            report.setIncludeAntennaPortNumber(true);
            report.setMode(ReportMode.Individual);
            settings.setReaderMode(ReaderMode.AutoSetDenseReader);
            AntennaConfigGroup antennas = settings.getAntennas();
            antennas.disableAll();
            antennas.enableById(new short[]{1});
            antennas.getAntenna((short) 1).setIsMaxRxSensitivity(false);
            antennas.getAntenna((short) 1).setIsMaxTxPower(false);
            antennas.getAntenna((short) 1).setTxPowerinDbm(20.0);
            antennas.getAntenna((short) 1).setRxSensitivityinDbm(-70);
            reader.setTagReportListener(new TagReportListenerImplementation(warehouseId));
            reader.applySettings(settings);
        }catch (OctaneSdkException ex){
            logger.debug(ex.getMessage());
        }catch (Exception e){
            logger.debug(e.getMessage());
        }
    }


    public void stop(){
        try {
            reader.stop();
        }catch (OctaneSdkException ex){
            logger.debug(ex.getMessage());
        }
    }

    public void start(){
        try {
            reader.start();
        }catch (OctaneSdkException ex){
            logger.debug(ex.getMessage());
        }
    }

    public boolean getReaderStatic(){
        reader.onKeepaliveTimeout();
        return reader.isConnected();
    }


}
