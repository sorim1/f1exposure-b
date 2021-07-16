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
import sorim.f1.slasher.relentless.handling.ExceptionHandling;
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
    RestTemplate restTemplate = new RestTemplate();

    String liveTimingUrl = "https://livetiming.formula1.com/static/{year}/{grandPrix}/{race}/SPFeed.json";

    private final CalendarRepository calendarRepository;

    @Override
    public void getAllRaceDataFromErgastTable(String year) {
        List<Race> races = service.fetchSeason(year);
        races.forEach(race -> {
            String response = getLiveTimingResponseOfErgastRace(race);
            if(response!=null) {
                race.setLiveTiming(response.substring(response.indexOf("{")));
                race.setCircuitId(race.getCircuit().getCircuitId());
            }
        });
        service.saveRaces(races);
    }

    private String getLiveTimingResponseOfErgastRace(Race race) {
        String grandPrixName = race.getRaceName().replaceAll(" ", "_");
        String grandPrix = race.getDate() + "_" + grandPrixName;
        String raceName = race.getDate() + "_Race";
        // System.out.println("https://livetiming.formula1.com/static/"+ race.getSeason()+ "/"+ grandPrix + "/"+ raceName + "/SPFeed.json");
        try {
            return restTemplate
                    .getForObject(liveTimingUrl, String.class, race.getSeason(), grandPrix, raceName);
        } catch (Exception e) {
            log.error("error1:", e);
            ExceptionHandling.logException("LIVETIMING_ERROR", e.getMessage());
        }
        return null;
    };

    @Override
    public LiveTimingData processLatestRace() throws JsonProcessingException {
        Race race = service.getLatestAnalyzedRace();
        ObjectMapper mapper = new ObjectMapper();
        LiveTimingData response = mapper.readValue(race.getLiveTiming(), LiveTimingData.class);
        return response;
    }

    @Override
    public RaceAnalysis getRaceAnalysis(){
        return service.getLatestAnalyzedRace().getRaceAnalysis();
    }

    @Override
    public Boolean analyzeLatestRace() {
        Race race = service.getLatestNonAnalyzedRace();
        String response = getLiveTimingResponseOfErgastRace(race);
        if(response!=null) {
            race.setLiveTiming(response.substring(response.indexOf("{")));
            race.setRaceAnalysis(fetchNewRaceAnalysis(race.getCircuit().getCircuitId()));
            race.setCircuitId(race.getCircuit().getCircuitId());
            service.saveRace(race);
            return true;
        } else {
            return false;
        }

    }

    public RaceAnalysis fetchNewRaceAnalysis(String circuitId) {
        ObjectMapper mapper = new ObjectMapper();
        List<Race> races = service.findByCircuitIdOrderBySeasonDesc(circuitId);
        List<FrontendGraphWeatherData> weatherChartData = new ArrayList<>();
        AtomicReference<Boolean> zeroBoolean = new AtomicReference<>(true);
        AtomicReference<FrontendGraphScoringData> scoringData = new AtomicReference<>();
        AtomicReference<FrontendGraphLapPosData> lapPosData = new AtomicReference<>();
        AtomicReference<List<Driver>> drivers = new AtomicReference<>();
        AtomicReference<FrontendGraphLeaderboardData> leaderboards = new AtomicReference<>();
        races.forEach(race -> {
            try {
                LiveTimingData response = mapper.readValue(race.getLiveTiming(), LiveTimingData.class);
                FrontendGraphWeatherData weatherRow = new FrontendGraphWeatherData(response.getWeather().getGraph().getData(), Integer.valueOf(race.getSeason()));
                weatherChartData.add(weatherRow);
                if(zeroBoolean.get()){
                    drivers.set(response.getInit().getData().getDrivers());
                    List<String> driverCodes =MainUtility.extractDriverCodesOrdered(drivers.get());
                    scoringData.set(new FrontendGraphScoringData(response.getScores().getGraph(), Integer.valueOf(race.getSeason()), driverCodes));
                    leaderboards.set(new FrontendGraphLeaderboardData(response.getFree().data, response.getBest().data));
                    lapPosData.set(new FrontendGraphLapPosData(response.getLapPos().getGraph(), response.getXtra().data, driverCodes));
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
                .leaderboardData(leaderboards.get())
                .title(leaderboards.get().title).build();
    }

    public RaceAnalysis getRaceAnalysisOld() {
        ObjectMapper mapper = new ObjectMapper();
        String circuitId = service.getLatestAnalyzedRace().getCircuitId();
        List<Race> races = service.findByCircuitIdOrderBySeasonDesc(circuitId);
        List<FrontendGraphWeatherData> weatherChartData = new ArrayList<>();
        AtomicReference<Boolean> zeroBoolean = new AtomicReference<>(true);
        AtomicReference<FrontendGraphScoringData> scoringData = new AtomicReference<>();
        AtomicReference<FrontendGraphLapPosData> lapPosData = new AtomicReference<>();
        AtomicReference<List<Driver>> drivers = new AtomicReference<>();
        AtomicReference<FrontendGraphLeaderboardData> leaderboards = new AtomicReference<>();
        races.forEach(race -> {
            try {
                LiveTimingData response = mapper.readValue(race.getLiveTiming(), LiveTimingData.class);
                FrontendGraphWeatherData weatherRow = new FrontendGraphWeatherData(response.getWeather().getGraph().getData(), Integer.valueOf(race.getSeason()));
                weatherChartData.add(weatherRow);
                if(zeroBoolean.get()){
                    drivers.set(response.getInit().getData().getDrivers());
                    List<String> driverCodes =MainUtility.extractDriverCodesOrdered(drivers.get());
                    scoringData.set(new FrontendGraphScoringData(response.getScores().getGraph(), Integer.valueOf(race.getSeason()), driverCodes));
                    leaderboards.set(new FrontendGraphLeaderboardData(response.getFree().data, response.getBest().data));
                    lapPosData.set(new FrontendGraphLapPosData(response.getLapPos().getGraph(), response.getXtra().data, driverCodes));
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
                .leaderboardData(leaderboards.get())
                .title(leaderboards.get().title).build();
    }

    private void getDataUrl() {
        ZonedDateTime gmtZoned = ZonedDateTime.now(ZoneId.of("Europe/London"));
        LocalDateTime gmtDateTime = gmtZoned.toLocalDateTime();
        F1Calendar f1calendar = calendarRepository.findFirstByRaceAfterOrderByRace(gmtDateTime);
    }
}
