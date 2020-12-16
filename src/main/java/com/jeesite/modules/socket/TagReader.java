package com.jeesite.modules.socket;

import com.impinj.octanesdk.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TagReader implements Runnable {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String hostname = "192.168.1.100";
    private Long warehouseId = 123L;

    public TagReader() {
    }

    public TagReader(String hostname, Long warehouseId) {
        this.hostname = hostname;
        this.warehouseId = warehouseId;
    }

    @Override
    public void run() {
        try {

            ImpinjReader reader = new ImpinjReader();

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

            reader.start();

        } catch (OctaneSdkException ex) {

        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
    }
}
