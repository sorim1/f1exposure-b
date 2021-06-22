package sorim.f1.slasher.relentless.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import sorim.f1.slasher.relentless.model.livetiming.LiveTimingData;
import sorim.f1.slasher.relentless.model.livetiming.RaceAnalysis;
import sorim.f1.slasher.relentless.model.livetiming.WeatherData;

public interface LiveTimingService {

    void getLatestRaceData();

    void getAllRaceDataFromErgastTable(String year);

    LiveTimingData processSingleRace() throws JsonProcessingException;

    WeatherData getWeather() throws JsonProcessingException;

    RaceAnalysis getRaceAnalysis() throws JsonProcessingException, Exception;

}
