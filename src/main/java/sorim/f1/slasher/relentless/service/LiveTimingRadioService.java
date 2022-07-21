package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.model.enums.RoundEnum;
import sorim.f1.slasher.relentless.model.livetiming.Driver;
import sorim.f1.slasher.relentless.model.livetiming.RadioData;
import sorim.f1.slasher.relentless.model.livetiming.UpcomingRaceAnalysis;

import java.util.List;

public interface LiveTimingRadioService {

    void enrichUpcomingRaceAnalysisWithRadioData(UpcomingRaceAnalysis upcomingRaceAnalysis, String jsonStream, RoundEnum session);
    List<RadioData> enrichRaceAnalysisWithRadioData(List<Driver> drivers, String jsonStream);

    String generatePostRaceRadio();
}
