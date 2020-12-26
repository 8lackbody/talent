package com.jeesite.modules.other.socket;

import com.impinj.octanesdk.*;
import com.jeesite.modules.entity.Warehouse;
import com.jeesite.modules.other.utils.SpringContextHolder;
import com.jeesite.modules.service.WarehouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TagReader {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ImpinjReader reader;
    private Warehouse warehouse;

    public TagReader(Warehouse warehouse) {
        this.reader = new ImpinjReader();
        this.warehouse = warehouse;
        connect();
    }

    public void connect() {
        try {
            reader.connect(warehouse.getReaderIp());
            logger.info(warehouse.getWarehouseName() + " Connecting reader success");

            Settings settings = reader.queryDefaultSettings();
            ReportConfig report = settings.getReport();
            report.setIncludeAntennaPortNumber(true);
            report.setMode(ReportMode.Individual);
            settings.setReaderMode(ReaderMode.AutoSetDenseReader);
            AntennaConfigGroup antennas = settings.getAntennas();
            antennas.disableAll();

            antennas.enableById(new short[]{1, 2});
            antennas.getAntenna((short) 1).setIsMaxRxSensitivity(false);
            antennas.getAntenna((short) 1).setIsMaxTxPower(false);
            antennas.getAntenna((short) 1).setTxPowerinDbm(warehouse.getAntenna1Power());
            antennas.getAntenna((short) 1).setRxSensitivityinDbm(warehouse.getAntenna1Sensitivity());

            antennas.getAntenna((short) 1).setIsMaxRxSensitivity(false);
            antennas.getAntenna((short) 1).setIsMaxTxPower(false);
            antennas.getAntenna((short) 1).setTxPowerinDbm(warehouse.getAntenna2Power());
            antennas.getAntenna((short) 1).setRxSensitivityinDbm(warehouse.getAntenna2Sensitivity());

            if (warehouse.getAntenna1Enable() == 0) {
                antennas.disableById(new short[]{1});
            }

            if (warehouse.getAntenna2Enable() == 0) {
                antennas.disableById(new short[]{2});
            }

            reader.setTagReportListener(new TagReportListenerImplementation(warehouse.getWarehouseId()));
            reader.applySettings(settings);
        } catch (OctaneSdkException ex) {
            logger.error(ex.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }


    // TODO 各个指令在执行前需要做判断
    public void stop() {
        try {
            reader.stop();
            logger.info(warehouse.getWarehouseName() + " stop reader success");
        } catch (OctaneSdkException ex) {
            logger.debug(ex.getMessage());
        }
    }

    public void start() {
        try {
            reader.start();
            logger.info(warehouse.getWarehouseName() + " start reader success");
        } catch (OctaneSdkException ex) {
            logger.error(ex.getMessage());
        }
    }

    public boolean getReaderStatus() {
        Status status;
        boolean isPerforming;
        try {
            status = reader.queryStatus();
            isPerforming = status.getIsSingulating();
        }catch (Exception e){
            return false;
        }
        return isPerforming;
    }
}
