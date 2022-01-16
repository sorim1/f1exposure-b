package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.entities.ergast.RaceData;
import sorim.f1.slasher.relentless.model.livetiming.RaceAnalysis;
import sorim.f1.slasher.relentless.model.livetiming.UpcomingRaceAnalysis;

import java.util.List;

public interface LiveTimingService {

    void getAllRaceDataFromErgastTable(String year, Boolean detailed, Boolean deleteOld);

    RaceAnalysis getRaceAnalysis() throws Exception;

    Integer analyzeLatestRace();

    Integer analyzeRace(Integer season, Integer round);

    Boolean resetLatestRaceAnalysis();

    Boolean deleteLatestRaceAnalysis();

    String validateLatestRaceAnalysis();

    UpcomingRaceAnalysis getUpcomingRaceAnalysis();

    RaceData getUpcomingRace();

    Boolean upcomingRacesAnalysisInitialLoad(String season);

    Integer analyzeUpcomingRace(Boolean redo);

    Boolean updateAllImageUrlsDev();

    List<RaceData> findRacesBySeason(String season);

    Boolean deleteRacesBySeason(String season);

    String updateCircuitImage(String season, Integer round, String newImageUrl);

    Boolean setLatestTreeMap(Boolean ergastStandingsUpdated);

    RaceData backupRaceData(Integer id);

    RaceData restoreRaceData(Integer id, RaceData body);
}
