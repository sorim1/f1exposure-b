package sorim.f1.slasher.relentless.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sorim.f1.slasher.relentless.configuration.MainProperties;
import sorim.f1.slasher.relentless.entities.ergast.Race;
import sorim.f1.slasher.relentless.handling.Logger;
import sorim.f1.slasher.relentless.model.ergast.ErgastResponse;
import sorim.f1.slasher.relentless.model.livetiming.*;
import sorim.f1.slasher.relentless.repository.CalendarRepository;
import sorim.f1.slasher.relentless.service.ErgastService;
import sorim.f1.slasher.relentless.service.LiveTimingService;
import sorim.f1.slasher.relentless.util.MainUtility;

import javax.xml.bind.DatatypeConverter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LiveTimingServiceImpl implements LiveTimingService {

    private final ErgastService ergastService;
    private final MainProperties properties;
    RestTemplate restTemplate = new RestTemplate();

    private static final String liveTimingUrl = "https://livetiming.formula1.com/static/{year}/{grandPrix}/{race}/SPFeed.json";
    private static final String timingAppDataUrl = "https://livetiming.formula1.com/static/{year}/{grandPrix}/{race}/TimingAppData.jsonStream";

    private final CalendarRepository calendarRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void getAllRaceDataFromErgastTable(String year) {
        List<Race> races = ergastService.fetchSeason(year);
        races.forEach(race -> {
            String response = getLiveTimingResponseOfErgastRace(race);
            if(response!=null) {
                race.setLiveTiming(response.substring(response.indexOf("{")));
                race.setCircuitId(race.getCircuit().getCircuitId());
            }
        });
        ergastService.saveRaces(races);
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
            log.error("error1: {}", grandPrix);
            log.error("error2:", e);
            Logger.log("LIVETIMING_ERROR", e.getMessage());
        }
        return null;
    };
    private String getTimingAppDataResponseOfErgastRace(Race race) {
        String grandPrixName = race.getRaceName().replaceAll(" ", "_");
        String grandPrix = race.getDate() + "_" + grandPrixName;
        String raceName = race.getDate() + "_Race";
        try {
            return restTemplate
                    .getForObject(timingAppDataUrl, String.class, race.getSeason(), grandPrix, raceName);
        } catch (Exception e) {
            log.error("timingAppDataUrl error:", e);
            Logger.log("timingAppDataUrl", e.getMessage());
        }
        return null;
    };



    @Override
    public RaceAnalysis getRaceAnalysis(){
        return ergastService.getLatestAnalyzedRace().getRaceAnalysis();
    }

    @Override
    public Boolean analyzeLatestRace() {
        Race race = ergastService.getLatestNonAnalyzedRace(properties.getCurrentYear());
        String liveTimingResponse = getLiveTimingResponseOfErgastRace(race);
        String timingAppDataResponse = getTimingAppDataResponseOfErgastRace(race);
//        log.info("1timingAppDataResponse");
//        log.info(timingAppDataResponse);
        if(liveTimingResponse!=null) {
            race.setLiveTiming(liveTimingResponse.substring(liveTimingResponse.indexOf("{")));
            race.setTimingAppData(timingAppDataResponse.substring(timingAppDataResponse.indexOf("00")));
            race.setCircuitId(race.getCircuit().getCircuitId());
            ergastService.saveRace(race);
            race.setRaceAnalysis(fetchNewRaceAnalysis(race.getCircuit().getCircuitId()));

            ergastService.saveRace(race);
            return true;
        } else {
            return false;
        }

    }

    @Override
    public Boolean resetLatestRaceAnalysis() {
        Race race = ergastService.getLatestAnalyzedRace();
        race.setRaceAnalysis(null);
        race.setLiveTiming(null);
        race.setTimingAppData(null);
        race.setCircuitId(null);
        ergastService.saveRace(race);
        return true;
    }

    @Override
    public String validateLatestRaceAnalysis() {
        String originalInput = "7ZbBasMwDED/Ree0SJZs2b6W/cF22dihjMIGI4eut5B/X6rtsNFSgiCQgi9KsP0wlqWHB3joT8ePwxfUlwGeTm9QIWCgDeqG8iNxRarIW8mqXPIzdLDbH6fVA/A57N73fX/4tAGEih0Ei2xRoBJKB/H3K9MPjqNNXLBSkvzBxVahwfiD0hmNs7b9x9mWycmpkytOjtALkhe8dhuzQG9OKTvBEJwgz0vORbVaybGz1A0Wb3bFW3nR3SLXzikU4m3WjqnuPvE1yoTe8pUGVRRe1lcFm6+ar5qvVuarlO7PVyVpSlmX9VVMzVfNV81X6/JVIL43X8k2oCZe+Hkl2HTVdNV0tS5dEa3zefU6fgM=";
        byte[] result = Base64.getDecoder().decode(originalInput);

        byte[] result2 = DatatypeConverter.parseBase64Binary(originalInput);
        log.info("info");
        log.info(new String(result));
        log.info(new String(result2));
        Race race = ergastService.getLatestAnalyzedRace();
        String response = getLiveTimingResponseOfErgastRace(race);
        if(response!=null) {
            String newLiveTiming = response.substring(response.indexOf("{"));
            String oldLiveTiming = race.getLiveTiming();
            if(newLiveTiming.equals(oldLiveTiming)){
                return "equal";
            }

            return "NOT equal1";
        }

        return "NOT equal2";
    }


    public RaceAnalysis fetchNewRaceAnalysis(String circuitId) {
        List<Race> races = ergastService.findByCircuitIdOrderBySeasonDesc(circuitId);
        List<FrontendGraphWeatherData> weatherChartData = new ArrayList<>();
        AtomicReference<Boolean> onlyFirstOne = new AtomicReference<>(true);
        AtomicReference<List<Driver>> drivers = new AtomicReference<>();
        AtomicReference<String> title = new AtomicReference<>();
        races.forEach(race -> {
            try {
                LiveTimingData response = mapper.readValue(race.getLiveTiming(), LiveTimingData.class);
                FrontendGraphWeatherData weatherRow = new FrontendGraphWeatherData(response.getWeather().getGraph().getData(), race.getSeason() + " (round "+ race.getRound() +")");
                weatherChartData.add(weatherRow);
                if(onlyFirstOne.get()){
                    List<Driver> driversResponse = response.getInit().getData().getDrivers();
                    for(int i = 0; i<driversResponse.size(); i++){
                        driversResponse.get(i).setStartingPosition(i+1);
                    }
                    Map<String, Driver> driversMap = driversResponse.stream()
                            .collect(Collectors.toMap(Driver::getInitials, Function.identity(), (o1, o2) -> o1, TreeMap::new));
                    List<String> driverCodes =MainUtility.extractDriverCodes(driversResponse);

                    title.set((String) response.getFree().data.getDataField("R"));

                    enrichDriversWithScoringData(driversMap, response.getScores().getGraph());
                    enrichDriversWithFreeData(driversMap, response.getFree().data);
                    enrichDriversWithBestData(driversMap, response.getBest().data, driverCodes);
                    enrichDriversWithLapByLapData(driversMap, response.getLapPos().getGraph(), response.getXtra().data, driverCodes);

                    Map<String, String> ergastCodes = ergastService.connectDriverCodesWithErgastCodes();
                        enrichDriversWithErgastLapTimes(driversMap, ergastCodes, race.getSeason(), race.getRound());

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
                .year(properties.getCurrentYear())
                .title(title.get()).build();
    }

    private void enrichDriversWithErgastLapTimes(Map<String, Driver> driversMap, Map<String, String> ergastCodes, String season, Integer round) throws JsonProcessingException {
        ErgastResponse response = ergastService.getRaceLaps(Integer.valueOf(season), round);
        response.getMrData().getRaceTable().getRaces().get(0).getLaps().forEach(lap->{
            lap.getTimings().forEach(timing->{
                driversMap.get(ergastCodes.get(timing.getDriverId())).getLapByLapData().addLapTime(lap.getNumber(), timing);
            });
        });
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
                        .add(new Tyre(String.valueOf(tyreSequence.charAt(tyreSequence.length()-1-(i/3))), tiData.get(i+1)));
            }
            driverCounter.incrementAndGet();
        });
    }

    private void enrichDriversWithBestData(Map<String, Driver> driversMap, RawData bestData, List<String> driverCodes) {
        List<LinkedHashMap> dr = (List<LinkedHashMap>) bestData.getDataField("DR");
       AtomicReference<Integer> counter = new AtomicReference<>(0);
        dr.forEach(row ->{
            List<Object> data = (List<Object>) row.get("B");
            driversMap.get(driverCodes.get(counter.get())).setFastestLap((String) data.get(1));
            driversMap.get(driverCodes.get(counter.get())).setFastestLapNumber((Integer) data.get(2));
            driversMap.get(driverCodes.get(counter.get())).setFastestLapPosition((Integer) data.get(3));
            driversMap.get(driverCodes.get(counter.get())).setFastestLapSector1((String) data.get(4));
            driversMap.get(driverCodes.get(counter.get())).setFastestLapPositionSector1((Integer) data.get(6));
            driversMap.get(driverCodes.get(counter.get())).setFastestLapSector2((String) data.get(7));
            driversMap.get(driverCodes.get(counter.get())).setFastestLapPositionSector2((Integer) data.get(9));
            driversMap.get(driverCodes.get(counter.get())).setFastestLapSector3((String) data.get(10));
            driversMap.get(driverCodes.get(counter.get())).setFastestLapPositionSector3((Integer) data.get(12));
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

}
