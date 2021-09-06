package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.entities.*;
import sorim.f1.slasher.relentless.model.*;

import java.util.List;

public interface ExposureService {
    Boolean exposeDrivers(String[] exposedList, String ipAddress) throws Exception;

    ActiveExposureChart getExposedChartData();

    ExposureResponse getExposureDriverList();

    boolean closeExposurePoll();

    Integer getCurrentExposureRound();

    void setCurrentExposureRound(Integer newCurrentRound);

    boolean exposureOn();

    void setNextRoundOfExposure(List<DriverStanding> driverStandings, int round);

    List<ExposureChampionshipData>  getExposureChampionshipData();

    List<ExposureChampionshipStanding> getExposureStandings();

    List<Integer> getExposureVoters();

    String getTitle();

    FullExposure backupExposure();

    void initializeExposure();
}
