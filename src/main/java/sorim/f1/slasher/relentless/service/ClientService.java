package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.model.CountdownData;
import sorim.f1.slasher.relentless.model.ExposedChart;

import javax.servlet.http.HttpServletRequest;

public interface ClientService {
    Boolean exposeDrivers(String[] exposedList, String ipAddress);

    void validateHeader(String authorization) throws Exception;

    String validateIp(HttpServletRequest request);

    ExposedChart getExposedChartData();

    CountdownData getCountdownData();
}
