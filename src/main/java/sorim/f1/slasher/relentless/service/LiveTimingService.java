package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.entities.ergast.RaceData;
import sorim.f1.slasher.relentless.model.livetiming.RaceAnalysis;
import sorim.f1.slasher.relentless.model.livetiming.SessionInfo;
import sorim.f1.slasher.relentless.model.livetiming.UpcomingRaceAnalysis;
import sorim.f1.slasher.relentless.model.youtube.YouTubeVideo;

import java.util.List;

public interface LiveTimingService {

    void getAllRaceDataFromErgastTable(String year, Boolean detailed, Boolean deleteOld) throws InterruptedException;

    RaceAnalysis getRaceAnalysis() throws Exception;

    Integer analyzeLatestRace(Boolean updateStatistics);

    Boolean analyzeRace(Integer season, Integer round);

    Boolean resetLatestRaceAnalysis();

    Boolean deleteLatestRaceAnalysis();

    String validateLatestRaceAnalysis();

    UpcomingRaceAnalysis getUpcomingRaceAnalysis();

    RaceData getUpcomingRace();

    Boolean upcomingRacesAnalysisInitialLoad(String season) throws InterruptedException;

    Integer analyzeUpcomingRace(Boolean redo);

    Boolean updateAllImageUrlsDev();

    List<RaceData> findRacesBySeason(String season);

    Boolean deleteRacesBySeason(String season);

    String updateCircuitImage(String season, Integer round, String newImageUrl);


    RaceData backupRaceData(Integer id);

    RaceData restoreRaceData(Integer id, RaceData body);

    boolean checkIfEventIsGenerating();

    boolean checkIfRaceIsGenerating();

    SessionInfo getSessionInfo();

    List<YouTubeVideo> getYoutubeVideos();

    public void enrichLatestRaceWithYoutubeWithDelay();

}
