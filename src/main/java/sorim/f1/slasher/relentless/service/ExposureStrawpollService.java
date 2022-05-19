package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.model.*;

import java.util.List;

public interface ExposureStrawpollService {

    StrawpollModelTwo fetchStrawpollResults();

    String initializeStrawpoll(String id);

    ExposureData getExposedChartData();

    ActiveExposureChart getActiveExposureChart();

    void closeExposurePoll(Boolean showWinnerValue);

    ExposureResponse getExposureDriverList();

    Boolean initializeExposureFrontendVariables(String id);

    Boolean isExposureNow();

    void startPolling();

    boolean exposureOn();

    void setCurrentExposureRound(Integer newCurrentRound);

    FullExposure backupExposure();

    Boolean restoreExposureFromBackup(FullExposure fullExposure);

    void openExposurePoll(Integer minutes);

    List<Integer> incrementExposureRound();

    List<Integer> setCurrentRound(Integer newRound);

    Integer getCurrentRoundUp();

    String getExposureStrawpoll();

    Integer resetLatestPoll();

    String setStrawpoll(String id);

    String changeShowWinner(Boolean value);
}
