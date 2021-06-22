package sorim.f1.slasher.relentless.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sorim.f1.slasher.relentless.configuration.MainProperties;
import sorim.f1.slasher.relentless.entities.F1Calendar;
import sorim.f1.slasher.relentless.entities.ergast.Race;
import sorim.f1.slasher.relentless.model.livetiming.*;
import sorim.f1.slasher.relentless.repository.CalendarRepository;
import sorim.f1.slasher.relentless.service.ErgastService;
import sorim.f1.slasher.relentless.service.LiveTimingService;
import sorim.f1.slasher.relentless.util.MainUtility;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LiveTimingServiceImpl implements LiveTimingService {

    private final ErgastService service;
    private final MainProperties properties;

    String urlExample = "https://livetiming.formula1.com/static/2019/2019-03-17_Australian_Grand_Prix/2019-03-17_Race/SPFeed.json";
    String LIVETIMING_URL = "https://livetiming.formula1.com/static/";
    // 2019/2019-03-17_Australian_Grand_Prix/2019-03-17_Race/SPFeed.json
    String liveTimingUrl = "https://livetiming.formula1.com/static/{year}/{grandPrix}/{race}/SPFeed.json";

    private final CalendarRepository calendarRepository;

    @Override
    public void getLatestRaceData() {
        getDataUrl();
    }

    @Override
    public void getAllRaceDataFromErgastTable(String year) {
        RestTemplate restTemplate = new RestTemplate();
        List<Race> races = service.fetchSeason(year);
        races.forEach(race -> {
            String grandPrixName = race.getRaceName().replaceAll(" ", "_");
            String grandPrix = race.getDate() + "_" + grandPrixName;
            String raceName = race.getDate() + "_Race";
            String response = null;
            try {
                response = restTemplate
                        .getForObject(liveTimingUrl, String.class, race.getSeason(), grandPrix, raceName);
                // race.setLiveTiming(response.substring(1));
                race.setLiveTiming(response.substring(response.indexOf("{")));
                race.setCircuitId(race.getCircuit().getCircuitId());
            } catch (Exception e) {
                race.setLiveTiming("ERROR OCCURED: " + e.getMessage());
                log.error("error1:", e);
            }
        });
        service.saveRaces(races);
    }

    @Override
    public LiveTimingData processSingleRace() throws JsonProcessingException {
        Race race = service.fetchLatestRace();
        ObjectMapper mapper = new ObjectMapper();
        LiveTimingData response = mapper.readValue(race.getLiveTiming(), LiveTimingData.class);
        return response;
    }

    @Override
    public WeatherData getWeather() throws JsonProcessingException {
        return processSingleRace().getWeather().getGraph().getData();
    }

    @Override
    public RaceAnalysis getRaceAnalysis() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String circuitId = service.fetchLatestRace().getCircuitId();
        List<Race> races = service.findByCircuitId(circuitId);
        List<FrontendGraphWeatherData> weatherChartData = new ArrayList<>();
        AtomicReference<Boolean> zeroBoolean = new AtomicReference<>(true);
        AtomicReference<FrontendGraphScoringData> scoringData = new AtomicReference<>();;
        AtomicReference<FrontendGraphLapPosData> lapPosData = new AtomicReference<>();;
        AtomicReference<List<Driver>> drivers = new AtomicReference<>();;
        races.forEach(race -> {
            try {
                LiveTimingData response = mapper.readValue(race.getLiveTiming(), LiveTimingData.class);
                FrontendGraphWeatherData weatherRow = new FrontendGraphWeatherData(response.getWeather().getGraph().getData(), Integer.valueOf(race.getSeason()));
                weatherChartData.add(weatherRow);
                if(zeroBoolean.get()){
                    drivers.set(response.getInit().getData().getDrivers());
                    List<String> driverCodes =MainUtility.extractDriverCodesOrdered(drivers.get());
                    scoringData.set(new FrontendGraphScoringData(response.getScores().getGraph(), Integer.valueOf(race.getSeason()), driverCodes));
                    lapPosData.set(new FrontendGraphLapPosData(response.getLapPos().getGraph(), driverCodes));
                    zeroBoolean.set(false);
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
                return RaceAnalysis.builder()
                .weatherChartData(weatherChartData)
                .scoringChartData(scoringData.get())
                .lapPosChartData(lapPosData.get())
                .driverData(drivers.get())
                .title(races.get(0).getRaceName()).build();
    }

    private void getDataUrl() {
        ZonedDateTime gmtZoned = ZonedDateTime.now(ZoneId.of("Europe/London"));
        LocalDateTime gmtDateTime = gmtZoned.toLocalDateTime();
        F1Calendar f1calendar = calendarRepository.findFirstByRaceAfterOrderByRace(gmtDateTime);
    }
}
