package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.entities.ExposedChart;
import sorim.f1.slasher.relentless.entities.Tempo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface MainService {
    Tempo getTempo();

    Boolean exposeDrivers(String[] exposedList, String ipAddress);

    void validateHeader(String authorization) throws Exception;

    String validateIp(HttpServletRequest request);

    ExposedChart getExposedChartData();
}
