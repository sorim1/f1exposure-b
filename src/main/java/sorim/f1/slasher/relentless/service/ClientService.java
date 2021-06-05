package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.entities.ConstructorStanding;
import sorim.f1.slasher.relentless.entities.Driver;
import sorim.f1.slasher.relentless.entities.DriverStanding;
import sorim.f1.slasher.relentless.entities.SportSurgeEvent;
import sorim.f1.slasher.relentless.model.AllStandings;
import sorim.f1.slasher.relentless.model.CalendarData;
import sorim.f1.slasher.relentless.model.ExposedChart;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ClientService {
    Boolean exposeDrivers(String[] exposedList, String ipAddress);

    void validateHeader(String authorization) throws Exception;

    String validateIp(HttpServletRequest request);

    ExposedChart getExposedChartData();

    CalendarData getCountdownData();

    List<DriverStanding> getDriverStandings();

    List<ConstructorStanding> getConstructorStandings();

    List<Driver> getExposureDriverList();

    AllStandings getStandings();

    List<SportSurgeEvent> getSportSurge();
}
