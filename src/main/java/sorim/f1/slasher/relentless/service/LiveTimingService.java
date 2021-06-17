package sorim.f1.slasher.relentless.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import sorim.f1.slasher.relentless.model.LiveTimingData;

public interface LiveTimingService {

    void getLatestRaceData();

    void getAllRaceDataFromErgastTable();

    LiveTimingData processSingleRace() throws JsonProcessingException;
}
