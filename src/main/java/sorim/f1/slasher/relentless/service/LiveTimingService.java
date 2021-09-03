package sorim.f1.slasher.relentless.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import sorim.f1.slasher.relentless.model.livetiming.LiveTimingData;
import sorim.f1.slasher.relentless.model.livetiming.RaceAnalysis;
import sorim.f1.slasher.relentless.model.livetiming.UpcomingRaceAnalysis;
import sorim.f1.slasher.relentless.model.livetiming.WeatherData;

public interface LiveTimingService {

    void getAllRaceDataFromErgastTable(String year, Boolean detailed);

    RaceAnalysis getRaceAnalysis() throws Exception;

    Boolean analyzeLatestRace();

    Boolean resetLatestRaceAnalysis();

    String validateLatestRaceAnalysis();

    UpcomingRaceAnalysis getUpcomingRaceAnalysis();

    Boolean upcomingRacesAnalysisInitialLoad(String season);

    Boolean analyzeUpcomingRace();

    Boolean updateAllImageUrlsDev();
}
