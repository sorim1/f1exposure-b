package sorim.f1.slasher.relentless.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import sorim.f1.slasher.relentless.model.FrontendGraphWeatherData;
import sorim.f1.slasher.relentless.model.LiveTimingData;
import sorim.f1.slasher.relentless.model.WeatherData;

import java.util.List;

public interface LiveTimingService {

    void getLatestRaceData();

    void getAllRaceDataFromErgastTable(String year);

    LiveTimingData processSingleRace() throws JsonProcessingException;

    WeatherData getWeather() throws JsonProcessingException;

    List<FrontendGraphWeatherData> getWeatherThroughYears(String circuitId) throws JsonProcessingException, Exception;
}
