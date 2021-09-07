package sorim.f1.slasher.relentless.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import sorim.f1.slasher.relentless.entities.ergast.RaceData;
import sorim.f1.slasher.relentless.model.livetiming.LiveTimingData;
import sorim.f1.slasher.relentless.model.livetiming.RaceAnalysis;
import sorim.f1.slasher.relentless.model.livetiming.UpcomingRaceAnalysis;
import sorim.f1.slasher.relentless.model.livetiming.WeatherData;

import java.util.List;

public interface LiveTimingService {

    void getAllRaceDataFromErgastTable(String year, Boolean detailed, Boolean deleteOld);

    RaceAnalysis getRaceAnalysis() throws Exception;

    Boolean analyzeLatestRace();

    Boolean resetLatestRaceAnalysis();

    String validateLatestRaceAnalysis();

    UpcomingRaceAnalysis getUpcomingRaceAnalysis();

    Boolean upcomingRacesAnalysisInitialLoad(String season);

    Integer analyzeUpcomingRace();

    Boolean updateAllImageUrlsDev();

    List<RaceData> findRacesBySeason(String season);

    Boolean deleteRacesBySeason(String season);
}
