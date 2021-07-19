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
import sorim.f1.slasher.relentless.handling.Logger;
import sorim.f1.slasher.relentless.model.livetiming.*;
import sorim.f1.slasher.relentless.repository.CalendarRepository;
import sorim.f1.slasher.relentless.service.ErgastService;
import sorim.f1.slasher.relentless.service.LiveTimingService;
import sorim.f1.slasher.relentless.util.MainUtility;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

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
            Logger.log("LIVETIMING_ERROR", e.getMessage());
        }
        return null;
    };

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
            race.setCircuitId(race.getCircuit().getCircuitId());
            service.saveRace(race);
            race.setRaceAnalysis(fetchNewRaceAnalysis(race.getCircuit().getCircuitId()));

            service.saveRace(race);
            return true;
        } else {
            return false;
        }

    }

    @Override
    public Boolean resetLatestRaceAnalysis() {
        Race race = service.getLatestAnalyzedRace();
        race.setRaceAnalysis(null);
        race.setLiveTiming(null);
        race.setCircuitId(null);
        service.saveRace(race);
        return true;
    }

    public RaceAnalysis fetchNewRaceAnalysis(String circuitId) {
        ObjectMapper mapper = new ObjectMapper();
        List<Race> races = service.findByCircuitIdOrderBySeasonDesc(circuitId);
        List<FrontendGraphWeatherData> weatherChartData = new ArrayList<>();
        AtomicReference<Boolean> onlyFirstOne = new AtomicReference<>(true);
        AtomicReference<List<Driver>> drivers = new AtomicReference<>();
        AtomicReference<String> title = new AtomicReference<>();
        races.forEach(race -> {
            try {
                LiveTimingData response = mapper.readValue(race.getLiveTiming(), LiveTimingData.class);
                FrontendGraphWeatherData weatherRow = new FrontendGraphWeatherData(response.getWeather().getGraph().getData(), Integer.valueOf(race.getSeason()));
                weatherChartData.add(weatherRow);
                if(onlyFirstOne.get()){
                    List<Driver> driversResponse = response.getInit().getData().getDrivers();
                    Map<String, Driver> driversMap = driversResponse.stream()
                            .collect(Collectors.toMap(Driver::getInitials, Function.identity(), (o1, o2) -> o1, TreeMap::new));
                    List<String> driverCodes =MainUtility.extractDriverCodes(driversResponse);
                    title.set((String) response.getFree().data.getDataField("R"));
                    enrichDriversWithScoringData(driversMap, response.getScores().getGraph());
                    enrichDriversWithFreeData(driversMap, response.getFree().data);
                    enrichDriversWithBestData(driversMap, response.getBest().data, driverCodes);
                    enrichDriversWithLapByLapData(driversMap, response.getLapPos().getGraph(), response.getXtra().data, driverCodes);
                  //  leaderboards.set(new FrontendGraphLeaderboardData(response.getFree().data, response.getBest().data));
                  //  lapPosData.set(new FrontendGraphLapPosData(response.getLapPos().getGraph(), response.getXtra().data, driverCodes));
                    onlyFirstOne.set(false);
                    drivers.set(new ArrayList<>(driversMap.values()));
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        return RaceAnalysis.builder()
                .weatherChartData(weatherChartData)
                .driverData(drivers.get())
                .title(title.get()).build();
    }

    private void enrichDriversWithLapByLapData(Map<String, Driver> driversMap, LapPosGraph lapPos, RawData xtraData, List<String> driverCodes) {
        Map<String, Object> map = lapPos.data.getDataFields();
        map.forEach((key, v) -> {
            List<Integer> laps = (List<Integer>) v;
            //im deleting odd rows here because theyre lap counters, not positions
            for (int i = laps.size()-2; i >= 0; i -= 2) {
                laps.remove(i);
            }
            driversMap.get(key.substring(1)).getLapByLapData().setPositions(laps);
        });
        List<LinkedHashMap> dr = (List<LinkedHashMap>) xtraData.getDataField("DR");
        AtomicInteger driverCounter = new AtomicInteger();
        dr.forEach(row ->{
            List<String> xData = (List<String>) row.get("X");
            List<Integer> tiData = (List<Integer>) row.get("TI");
            String tyreSequence= xData.get(9);
            for(int i=0;i<tiData.size();i=i+3){
                driversMap.get(driverCodes.get(driverCounter.get())).getLapByLapData().getTyres()
                        .add(new Tyre(String.valueOf(tyreSequence.charAt(i/3)), tiData.get(i+1)));
            }
            driverCounter.incrementAndGet();
        });
    }

    private void enrichDriversWithBestData(Map<String, Driver> driversMap, RawData bestData, List<String> driverCodes) {
        List<LinkedHashMap> dr = (List<LinkedHashMap>) bestData.getDataField("DR");
       AtomicReference<Integer> counter = new AtomicReference<>(0);
        dr.forEach(row ->{
            List<String> data = (List<String>) row.get("B");
            driversMap.get(driverCodes.get(counter.get())).setFastestLap(data.get(4));
            counter.set(counter.get() + 1);
        });
    }

    private void enrichDriversWithFreeData(Map<String, Driver> driversMap, RawData freeData) {
        List<LinkedHashMap> dr = (List<LinkedHashMap>) freeData.getDataField("DR");
        dr.forEach(row ->{
            List<String> data = (List<String>) row.get("F");
            driversMap.get(data.get(0)).setPosition(Integer.valueOf(data.get(3)));
            driversMap.get(data.get(0)).setFinalGap(data.get(4));
        });
    }

    public void enrichDriversWithScoringData(Map<String, Driver> driversMap, ScoresGraph scoresGraph) {
        scoresGraph.steering.getDataFields().forEach((key,value)->{
            log.info("key sa p: {} " + key);
            List<Integer> list = (List<Integer>) value;
            driversMap.get(key.substring(1)).setSteering(list.get(1));
        });
        scoresGraph.gforceLat.getDataFields().forEach((key,value)->{
            List<Integer> list = (List<Integer>) value;
            driversMap.get(key.substring(1)).setGforceLat(list.get(1));
        });
        scoresGraph.gforceLong.getDataFields().forEach((key,value)->{
            List<Integer> list = (List<Integer>) value;
            driversMap.get(key.substring(1)).setGforceLong(list.get(1));
        });
        scoresGraph.brake.getDataFields().forEach((key,value)->{
            List<Integer> list = (List<Integer>) value;
            driversMap.get(key.substring(1)).setBrake(list.get(1));
        });
        scoresGraph.performance.getDataFields().forEach((key,value)->{
            List<Integer> list = (List<Integer>) value;
            driversMap.get(key.substring(1)).setPerformance(list.get(1));
        });
        scoresGraph.throttle.getDataFields().forEach((key,value)->{
            List<Integer> list = (List<Integer>) value;
            driversMap.get(key.substring(1)).setThrottle(list.get(1));
        });
    }

    private void getDataUrl() {
        ZonedDateTime gmtZoned = ZonedDateTime.now(ZoneId.of("Europe/London"));
        LocalDateTime gmtDateTime = gmtZoned.toLocalDateTime();
        F1Calendar f1calendar = calendarRepository.findFirstByRaceAfterOrderByRace(gmtDateTime);
    }
}
