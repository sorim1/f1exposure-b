package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.entities.*;
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

    List<F1Comment> postComment(F1Comment comment);

    List<F1Comment> getComments(String page);
}
