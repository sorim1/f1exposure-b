package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.model.*;

import java.util.List;

public interface ExposureStrawpollService {

    StrawpollModel fetchStrawpollResults();

    String initializeStrawpoll(String id);

    ExposureData getExposedChartData();

    ActiveExposureChart getActiveExposureChart();

    void closeExposurePoll();

    ExposureResponse getExposureDriverList();

    Boolean initializeExposureFrontendVariables(String id);

    void startPolling();

    boolean exposureOn();

    void setCurrentExposureRound(Integer newCurrentRound);

    FullExposure backupExposure();

    Boolean restoreExposureFromBackup(FullExposure fullExposure);

    void openExposurePoll(Integer minutes);

    List<Integer> updateCurrentRound();

    List<Integer> setCurrentRound(Integer newRound);

    Integer getCurrentRoundUp();

    String getExposureStrawpoll();

    Integer resetLatestPoll();

    String setStrawpoll(String id);
}
