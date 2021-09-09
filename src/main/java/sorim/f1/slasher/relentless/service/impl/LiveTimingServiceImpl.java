package sorim.f1.slasher.relentless.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sorim.f1.slasher.relentless.configuration.MainProperties;
import sorim.f1.slasher.relentless.entities.ergast.RaceData;
import sorim.f1.slasher.relentless.handling.Logger;
import sorim.f1.slasher.relentless.model.enums.RoundEnum;
import sorim.f1.slasher.relentless.model.ergast.ErgastResponse;
import sorim.f1.slasher.relentless.model.livetiming.*;
import sorim.f1.slasher.relentless.scheduled.Scheduler;
import sorim.f1.slasher.relentless.service.AdminService;
import sorim.f1.slasher.relentless.service.ErgastService;
import sorim.f1.slasher.relentless.service.LiveTimingService;
import sorim.f1.slasher.relentless.util.MainUtility;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
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
    private final AdminService adminService;
    private final MainProperties properties;
    RestTemplate restTemplate = new RestTemplate();

    private static final String liveTimingUrl = "https://livetiming.formula1.com/static/{year}/{grandPrix}/{race}/SPFeed.json";
    private static final String timingAppDataUrl = "https://livetiming.formula1.com/static/{year}/{grandPrix}/{race}/TimingAppData.jsonStream";
    private static final String wikipediaImagesUrl1 = "https://en.wikipedia.org/w/api.php?action=query&prop=pageimages&piprop=original&format=json&redirects&titles={title}";
    private static final String wikipediaImagesUrl2 = "https://en.wikipedia.org/w/api.php?action=query&prop=pageimages&piprop=thumbnail&pithumbsize=2000&format=json&redirects&titles={title}";


    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void getAllRaceDataFromErgastTable(String year, Boolean detailed, Boolean deleteOld) {
        List<RaceData> raceData = ergastService.fetchSeason(year);
        raceData.forEach(race -> {
            race.setCircuitId(race.getCircuit().getCircuitId());
            race.setImageUrl(getWikipediaOriginalImageUrl(race.getCircuit().getUrl(), race.getCircuit().getCircuitName(), false));
            String response = getLiveTimingResponseOfErgastRace(race, RoundEnum.RACE, 1);
            if (response != null) {
                race.setLiveTimingRace(response.substring(response.indexOf("{")));
                if (detailed) {
                    response = getLiveTimingResponseOfErgastRace(race, RoundEnum.QUALIFYING, 1);
                    if (response != null) {
                        race.setLiveTimingQuali(response.substring(response.indexOf("{")));
                    }
                    response = getLiveTimingResponseOfErgastRace(race, RoundEnum.PRACTICE_1, 1);
                    if (response != null) {
                        race.setLiveTimingFp1(response.substring(response.indexOf("{")));
                    }
                    response = getLiveTimingResponseOfErgastRace(race, RoundEnum.PRACTICE_2, 1);
                    if (response != null) {
                        race.setLiveTimingFp2(response.substring(response.indexOf("{")));
                    }
                    response = getLiveTimingResponseOfErgastRace(race, RoundEnum.PRACTICE_3, 1);
                    if (response != null) {
                        race.setLiveTimingFp3(response.substring(response.indexOf("{")));
                    }

                }

            }
        });
        if(deleteOld){
            ergastService.deleteRaces(year);
        }
        ergastService.saveRaces(raceData);
        upcomingRacesAnalysisInitialLoad(year);
    }

    private String getWikipediaOriginalImageUrl(String url, String circuitName, Boolean thumbnail) {
        String title = url.substring(url.lastIndexOf("/") + 1);
        String apiUrl;
        String image;
        if (thumbnail) {
            apiUrl = wikipediaImagesUrl2;
            image = "thumbnail";
        } else {
            apiUrl = wikipediaImagesUrl1;
            image = "original";
        }

        try {
            String response = restTemplate
                    .getForObject(apiUrl, String.class, title);
            TypeReference<HashMap<String, Object>> typeRef = new TypeReference<>() {
            };
            Map<String, Object> mapping = mapper.readValue(response, typeRef);
            LinkedHashMap<String, Object> query = (LinkedHashMap<String, Object>) mapping.get("query");
            LinkedHashMap<String, Object> pages = (LinkedHashMap<String, Object>) query.get("pages");
            LinkedHashMap<String, Object> page = (LinkedHashMap<String, Object>) pages.values().stream().findFirst().get();
            LinkedHashMap<String, Object> original = (LinkedHashMap<String, Object>) page.get(image);
            Integer width = (Integer) original.get("width");
            if (width <= 2000 || thumbnail) {
                return (String) original.get("source");
            } else {
                log.info("retrieving thumbnail");
                return getWikipediaOriginalImageUrl(url, circuitName, true);
            }
        } catch (Exception e) {
            log.error("this error is mostly likely cause by bad url received from ergast", e.getMessage());
            log.warn(title + " WORKAROUND:");
            title = circuitName.replaceAll(" ", "_");
            String response = restTemplate
                    .getForObject(apiUrl, String.class, title);
            TypeReference<HashMap<String, Object>> typeRef = new TypeReference<>() {
            };
            Map<String, Object> mapping = null;
            try {
                mapping = new ObjectMapper().readValue(response, typeRef);

                LinkedHashMap<String, Object> query = (LinkedHashMap<String, Object>) mapping.get("query");
                LinkedHashMap<String, Object> pages = (LinkedHashMap<String, Object>) query.get("pages");
                LinkedHashMap<String, Object> page = (LinkedHashMap<String, Object>) pages.values().stream().findFirst().get();
                LinkedHashMap<String, Object> original = (LinkedHashMap<String, Object>) page.get(image);
                Integer width = (Integer) original.get("width");
                if (width <= 2000 || thumbnail) {
                    return (String) original.get("source");
                } else {
                    log.info("retrieving thumbnail");
                    return getWikipediaOriginalImageUrl(url, circuitName, true);
                }
            } catch (Exception ex) {
                log.error(title + " WORKAROUND FAILED");
                ex.printStackTrace();
            }
        }
        return null;
    }


    private String getLiveTimingResponseOfErgastRace(RaceData raceData, RoundEnum round, Integer liveTimingEndpoint) {
        String url;
        switch (liveTimingEndpoint) {
            case 1:
                url = liveTimingUrl;
                break;
            case 2:
                url = timingAppDataUrl;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + liveTimingEndpoint);
        }
        String grandPrixName = raceData.getRaceName().replaceAll(" ", "_");
        String grandPrix = raceData.getDate() + "_" + grandPrixName;
        String raceName = null;
        String raceDate = raceData.getDate();
        String date;
        switch (round) {
            case RACE:
                raceName = raceDate + "_Race";
                break;
            case QUALIFYING:
                date = MainUtility.subtractDays(raceDate, 1);
                raceName = date + "_Qualifying";
                break;
            case PRACTICE_1:
                date = MainUtility.subtractDays(raceDate, 2);
                raceName = date + "_Practice_1";
                break;
            case PRACTICE_2:
                date = MainUtility.subtractDays(raceDate, 2);
                raceName = date + "_Practice_2";
                break;
            case PRACTICE_3:
                date = MainUtility.subtractDays(raceDate, 1);
                raceName = date + "_Practice_3";
                break;
            case SPRINT_QUALIFYING:
                date = MainUtility.subtractDays(raceDate, 1);
                raceName = date + "_Sprint_Qualifying";
                break;
        }
        System.out.println("https://livetiming.formula1.com/static/" + raceData.getSeason() + "/" + grandPrix + "/" + raceName + "/SPFeed.json");
        try {
            return restTemplate
                    .getForObject(url, String.class, raceData.getSeason(), grandPrix, raceName);
        } catch (Exception e) {
            Logger.log("getLiveTimingResponseOfErgastRace1 " + grandPrix, e.getMessage());
            if(round.equals(RoundEnum.PRACTICE_2)){
                date = MainUtility.subtractDays(raceDate, 1);
                raceName = date + "_Practice_2";
            } else if(round.equals(RoundEnum.QUALIFYING)){
                date = MainUtility.subtractDays(raceDate, 2);
                raceName = date + "_Qualifying";
            } else {
                return null;
            }
            try {
                System.out.println("2: https://livetiming.formula1.com/static/" + raceData.getSeason() + "/" + grandPrix + "/" + raceName + "/SPFeed.json");
                return restTemplate
                        .getForObject(url, String.class, raceData.getSeason(), grandPrix, raceName);
            } catch (Exception ex) {
                Logger.log("getLiveTimingResponseOfErgastRace2 " + grandPrix, ex.getMessage());
                return null;
            }
        }
    }

    @Override
    public RaceAnalysis getRaceAnalysis() {
        return ergastService.getLatestAnalyzedRace().getRaceAnalysis();
    }

    @Override
    public Boolean analyzeLatestRace() {
        RaceData raceData = ergastService.getLatestNonAnalyzedRace(properties.getCurrentYear());
        String liveTimingResponse = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.RACE, 1);
        //TODO timingAppData zasad ne koristim u RaceAnalysis
        //String timingAppDataResponse = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.RACE, 2);
        if (liveTimingResponse != null) {
            raceData.setLiveTimingRace(liveTimingResponse.substring(liveTimingResponse.indexOf("{")));
            //raceData.setTimingAppData(timingAppDataResponse.substring(timingAppDataResponse.indexOf("00")));
            ergastService.saveRace(raceData);
            raceData.setRaceAnalysis(fetchNewRaceAnalysis(raceData.getCircuit().getCircuitId()));

            ergastService.saveRace(raceData);
            analyzeUpcomingRace();
            Scheduler.analysisDone = true;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean resetLatestRaceAnalysis() {
        RaceData raceData = ergastService.getLatestAnalyzedRace();
        raceData.setRaceAnalysis(null);
        raceData.setLiveTimingRace(null);
        raceData.setTimingAppData(null);
        ergastService.saveRace(raceData);
        return true;
    }

    @Override
    public String validateLatestRaceAnalysis() {
        RaceData raceData = ergastService.getLatestAnalyzedRace();
        String response = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.RACE, 1);
        if (response != null) {
            String newLiveTiming = response.substring(response.indexOf("{"));
            String oldLiveTiming = raceData.getLiveTimingRace();
            if (newLiveTiming.equals(oldLiveTiming)) {
                return "equal";
            }

            return "NOT equal1";
        }

        return "NOT equal2";
    }

    @Override
    public UpcomingRaceAnalysis getUpcomingRaceAnalysis() {
        return ergastService.getUpcomingRace(properties.getCurrentYear()).getUpcomingRaceAnalysis();
    }

    @Override
    public List<RaceData> findRacesBySeason(String season) {
        return ergastService.findRacesBySeason(season);
    }

    @Override
    public Boolean deleteRacesBySeason(String season) {
        ergastService.deleteRaces(season);
        return true;
    }

    @Override
    public Boolean upcomingRacesAnalysisInitialLoad(String season) {
        Integer howManySeasonsBack = properties.getHowManySeasonsBack();
        List<RaceData> races = ergastService.findRacesBySeason(season);
        Map<String, UpcomingRaceAnalysis> upcomingRaceAnalysisMapByCircuitId =
                races.stream()
                        .collect(Collectors.toMap(
                                RaceData::getCircuitId,
                                UpcomingRaceAnalysis::new,
                                (circuitId1, circuitId2) -> {
                                    System.out.println("duplicate circuitId found: " + circuitId1);
                                    return circuitId1;
                                }
                        ));
        ErgastResponse response;
        for (int i = 1; i <= howManySeasonsBack; i++) {
            Integer round = 1;
            do {
                response = ergastService.getResultsByRound(properties.getCurrentYear() - i, round);

                if ((!response.getMrData().getRaceTable().getRaces().isEmpty())) {
                    String circuitId = response.getMrData().getRaceTable().getRaces().get(0).getCircuit().getCircuitId();
                    if (upcomingRaceAnalysisMapByCircuitId.containsKey(circuitId)) {
                        upcomingRaceAnalysisMapByCircuitId.get(circuitId).getBasicRaces().add(new BasicRaceData(response.getMrData().getRaceTable().getRaces().get(0)));
                    }
                }
                round++;

            } while (!response.getMrData().getRaceTable().getRaces().isEmpty());
        }
        races.forEach(race -> {
            race.setUpcomingRaceAnalysis(upcomingRaceAnalysisMapByCircuitId.get(race.getCircuitId()));
        });
        ergastService.saveRaces(races);
        return true;
    }

    @Override
    public Integer analyzeUpcomingRace() {
        RaceData raceData = ergastService.getLatestNonAnalyzedRace(properties.getCurrentYear());

        if (raceData.getLiveTimingFp1() == null) {
            String liveTimingResponse = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.PRACTICE_1, 1);
            if (liveTimingResponse != null) {
                raceData.setLiveTimingFp1(liveTimingResponse.substring(liveTimingResponse.indexOf("{")));
                List<Driver> drivers = createDriverListOfEvent(liveTimingResponse.substring(liveTimingResponse.indexOf("{")));
                raceData.getUpcomingRaceAnalysis().setFp1(drivers);
                String timingAppDataResponse = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.PRACTICE_1, 2);
                Map<String, String> driverMap = drivers.stream()
                        .collect(Collectors.toMap(Driver::getNum, Driver::getFullName));
                raceData.getUpcomingRaceAnalysis().setFp1Laps(createLapTimeDataList(timingAppDataResponse, driverMap));
            }
        }
        if (raceData.getLiveTimingFp2() == null) {
            String liveTimingResponse = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.PRACTICE_2, 1);
            if (liveTimingResponse != null) {
                raceData.setLiveTimingFp2(liveTimingResponse.substring(liveTimingResponse.indexOf("{")));
                List<Driver> drivers = createDriverListOfEvent(liveTimingResponse.substring(liveTimingResponse.indexOf("{")));
                raceData.getUpcomingRaceAnalysis().setFp2(drivers);
                String timingAppDataResponse = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.PRACTICE_2, 2);
                Map<String, String> driverMap = drivers.stream()
                        .collect(Collectors.toMap(Driver::getNum, Driver::getFullName));
                raceData.getUpcomingRaceAnalysis().setFp2Laps(createLapTimeDataList(timingAppDataResponse, driverMap));
            }
        }
        if (raceData.getLiveTimingFp3() == null) {
            String liveTimingResponse = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.PRACTICE_3, 1);
            if (liveTimingResponse != null) {
                raceData.setLiveTimingFp3(liveTimingResponse.substring(liveTimingResponse.indexOf("{")));
                List<Driver> drivers = createDriverListOfEvent(liveTimingResponse.substring(liveTimingResponse.indexOf("{")));
                raceData.getUpcomingRaceAnalysis().setFp3(drivers);
                String timingAppDataResponse = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.PRACTICE_3, 2);
                Map<String, String> driverMap = drivers.stream()
                        .collect(Collectors.toMap(Driver::getNum, Driver::getFullName));
                raceData.getUpcomingRaceAnalysis().setFp3Laps(createLapTimeDataList(timingAppDataResponse, driverMap));
            }
        }
        if (raceData.getLiveTimingQuali() == null) {
            String liveTimingResponse = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.QUALIFYING, 1);
            if (liveTimingResponse != null) {
                raceData.setLiveTimingQuali(liveTimingResponse.substring(liveTimingResponse.indexOf("{")));
                List<Driver> drivers = createDriverListOfEvent(liveTimingResponse.substring(liveTimingResponse.indexOf("{")));
                raceData.getUpcomingRaceAnalysis().setQuali(drivers);
                String timingAppDataResponse = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.QUALIFYING, 2);
                Map<String, String> driverMap = drivers.stream()
                        .collect(Collectors.toMap(Driver::getNum, Driver::getFullName));
                raceData.getUpcomingRaceAnalysis().setQualiLaps(createLapTimeDataList(timingAppDataResponse, driverMap));
            }
        }
        if (raceData.getLiveTimingSprintQuali() == null) {
            String liveTimingResponse = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.SPRINT_QUALIFYING, 1);
            if (liveTimingResponse != null) {
                raceData.setLiveTimingSprintQuali(liveTimingResponse.substring(liveTimingResponse.indexOf("{")));
               // List<Driver> drivers = createDriverListOfEvent(liveTimingResponse.substring(liveTimingResponse.indexOf("{")));
                List<Driver> drivers = analyzeSprintRace(liveTimingResponse.substring(liveTimingResponse.indexOf("{")));
                raceData.getUpcomingRaceAnalysis().setSprintQuali(drivers);
                String timingAppDataResponse = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.SPRINT_QUALIFYING, 2);
                Map<String, String> driverMap = drivers.stream()
                        .collect(Collectors.toMap(Driver::getNum, Driver::getFullName));
                raceData.getUpcomingRaceAnalysis().setSprintQualiLaps(createLapTimeDataList(timingAppDataResponse, driverMap));
            }
        }

        ergastService.saveRace(raceData);
        return adminService.getNextRefreshTick(-1000);
    }

    private List<LapTimeData> createLapTimeDataList(String timingAppData, Map<String, String> driverMap) {
        TypeReference<HashMap<String, Object>> typeRef = new TypeReference<>() {
        };
        List<LapTimeData> response = new ArrayList<>();
        List<String> timingAppDataLines = Arrays.asList(timingAppData.split(System.lineSeparator()));
        timingAppDataLines.forEach(stringLine -> {
            String jsonLine = stringLine.substring(stringLine.indexOf("{"));
            Map<String, Object> mapping = null;
            try {
                mapping = mapper.readValue(jsonLine, typeRef);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            LinkedHashMap<String, Object> lines = (LinkedHashMap<String, Object>) mapping.get("Lines");
            lines.forEach((k, v) -> {
                LinkedHashMap<Integer, Object> line = (LinkedHashMap<Integer, Object>) v;
                Object stintsObject = line.get("Stints");
                if (stintsObject != null && stintsObject instanceof LinkedHashMap) {
                    LinkedHashMap<String, Object> stints = (LinkedHashMap<String, Object>) stintsObject;
                    stints.forEach((k2, v2) -> {
                        LinkedHashMap<String, Object> dataMap = (LinkedHashMap<String, Object>) v2;
                        if (dataMap.containsKey("LapTime")) {
                            LapTimeData entry = new LapTimeData(Integer.valueOf(k));
                            entry.setDriverName(driverMap.get(k));
                            entry.setLapTime((String) dataMap.get("LapTime"));
                            entry.setLapTimeMs(MainUtility.lapTimeToMiliseconds((String) dataMap.get("LapTime")));
                            entry.setLapNumber((Integer) dataMap.get("LapNumber"));
                            response.add(entry);
                        }

                    });
                }
            });

        });
        response.sort(Comparator.comparing(LapTimeData::getLapTimeMs));
        for (int i = 0; i < response.size(); i++) {
            response.get(i).setPosition(i + 1);
        }
        return response;
    }

    @Override
    public Boolean updateAllImageUrlsDev() {
        List<RaceData> races = ergastService.findRacesBySeason(String.valueOf(properties.getCurrentYear()));
        races.forEach(race -> {
            race.setImageUrl(getWikipediaOriginalImageUrl(race.getCircuit().getUrl(), race.getCircuit().getCircuitName(), false));
        });
        ergastService.saveRaces(races);
        return null;
    }

    private List<Driver> createDriverListOfEvent(String liveTimingResponse) {
        List<Driver> driversResponse = null;
        try {
            LiveTimingData response = mapper.readValue(liveTimingResponse, LiveTimingData.class);
            driversResponse = response.getInit().getData().getDrivers();
            List<LinkedHashMap> freeDr = (List<LinkedHashMap>) response.getFree().data.getDataField("DR");
            List<LinkedHashMap> bestDr = (List<LinkedHashMap>) response.getBest().data.getDataField("DR");

            for (int i = 0; i < driversResponse.size(); i++) {
                List<String> fData = (List<String>) freeDr.get(i).get("F");
                driversResponse.get(i).setPosition(Integer.valueOf(fData.get(3)));
                driversResponse.get(i).setFinalGap(fData.get(4));
                List<Object> bData = (List<Object>) bestDr.get(i).get("B");
                driversResponse.get(i).setFastestLap((String) bData.get(1));
                driversResponse.get(i).setFastestLapNumber((Integer) bData.get(2));
                driversResponse.get(i).setFastestLapPosition((Integer) bData.get(3));
                driversResponse.get(i).setFastestLapSector1((String) bData.get(4));
                driversResponse.get(i).setFastestLapPositionSector1((Integer) bData.get(6));
                driversResponse.get(i).setFastestLapSector2((String) bData.get(7));
                driversResponse.get(i).setFastestLapPositionSector2((Integer) bData.get(9));
                driversResponse.get(i).setFastestLapSector3((String) bData.get(10));
                driversResponse.get(i).setFastestLapPositionSector3((Integer) bData.get(12));
            }
        } catch (JsonProcessingException e) {
            Logger.log("createBasicEventData failed", e.getMessage());
        }
        driversResponse.sort(Comparator.comparing(Driver::getPosition));
        return driversResponse;
    }


    public RaceAnalysis fetchNewRaceAnalysis(String circuitId) {
        List<RaceData> raceData = ergastService.findByCircuitIdOrderBySeasonDesc(circuitId);
        List<FrontendGraphWeatherData> weatherChartData = new ArrayList<>();
        AtomicReference<Boolean> onlyFirstOne = new AtomicReference<>(true);
        AtomicReference<List<Driver>> drivers = new AtomicReference<>();
        AtomicReference<String> title = new AtomicReference<>();
        raceData.forEach(race -> {
            try {
                LiveTimingData response = mapper.readValue(race.getLiveTimingRace(), LiveTimingData.class);
                FrontendGraphWeatherData weatherRow = new FrontendGraphWeatherData(response.getWeather().getGraph().getData(), race.getSeason() + " (round " + race.getRound() + ")");
                weatherChartData.add(weatherRow);
                if (onlyFirstOne.get()) {
                    List<Driver> driversResponse = response.getInit().getData().getDrivers();
                    for (int i = 0; i < driversResponse.size(); i++) {
                        driversResponse.get(i).setStartingPosition(i + 1);
                    }
                    Map<String, Driver> driversMap = driversResponse.stream()
                            .collect(Collectors.toMap(Driver::getInitials, Function.identity(), (o1, o2) -> o1, TreeMap::new));
                    List<String> driverCodes = MainUtility.extractDriverCodes(driversResponse);

                   // title.set((String) response.getFree().data.getDataField("R"));
                    title.set(race.getRaceName());

                    enrichDriversWithScoringData(driversMap, response.getScores().getGraph());
                    enrichDriversWithFreeData(driversMap, response.getFree().data);
                    enrichDriversWithBestData(driversMap, response.getBest().data, driverCodes);
                    enrichDriversWithLapByLapData(driversMap, response.getLapPos().getGraph(), response.getXtra().data, driverCodes);

                    Map<String, String> ergastCodes = ergastService.connectDriverCodesWithErgastCodes();
                    enrichDriversWithErgastLapTimes(driversMap, ergastCodes, race.getSeason(), race.getRound());

                    onlyFirstOne.set(false);
                    drivers.set(new ArrayList<>(driversMap.values()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return RaceAnalysis.builder()
                .weatherChartData(weatherChartData)
                .driverData(drivers.get())
                .year(properties.getCurrentYear())
                .title(title.get()).build();
    }

    public List<Driver> analyzeSprintRace(String liveTimingDataResponse) {
        List<Driver> drivers = new ArrayList<>();
        String title;
            try {
                LiveTimingData response = mapper.readValue(liveTimingDataResponse, LiveTimingData.class);
                    List<Driver> driversResponse = response.getInit().getData().getDrivers();
                    for (int i = 0; i < driversResponse.size(); i++) {
                        driversResponse.get(i).setStartingPosition(i + 1);
                    }
                    Map<String, Driver> driversMap = driversResponse.stream()
                            .collect(Collectors.toMap(Driver::getInitials, Function.identity(), (o1, o2) -> o1, TreeMap::new));
                    List<String> driverCodes = MainUtility.extractDriverCodes(driversResponse);

                    // title.set((String) response.getFree().data.getDataField("R"));

                    enrichDriversWithScoringData(driversMap, response.getScores().getGraph());
                    enrichDriversWithFreeData(driversMap, response.getFree().data);
                    enrichDriversWithBestData(driversMap, response.getBest().data, driverCodes);
                    enrichDriversWithLapByLapData(driversMap, response.getLapPos().getGraph(), response.getXtra().data, driverCodes);


                    //TODO ERGAST SPRINT LAP TIMES URL?
              //  Map<String, String> ergastCodes = ergastService.connectDriverCodesWithErgastCodes();
                //enrichDriversWithErgastLapTimes(driversMap, ergastCodes, race.getSeason(), race.getRound());
                   drivers = new ArrayList<>(driversMap.values());
            } catch (Exception e) {
                e.printStackTrace();
            }
        drivers.sort(Comparator.comparing(Driver::getPosition));
        return drivers;
    }

    private void enrichDriversWithErgastLapTimes(Map<String, Driver> driversMap, Map<String, String> ergastCodes, String season, Integer round) throws JsonProcessingException {
        ErgastResponse response = ergastService.getRaceLaps(Integer.valueOf(season), round);
        response.getMrData().getRaceTable().getRaces().get(0).getLaps().forEach(lap -> {
            lap.getTimings().forEach(timing -> {
                driversMap.get(ergastCodes.get(timing.getDriverId())).getLapByLapData().addLapTime(lap.getNumber(), timing);
            });
        });
    }

    private void enrichDriversWithLapByLapData(Map<String, Driver> driversMap, LapPosGraph lapPos, RawData xtraData, List<String> driverCodes) {
        Map<String, Object> map = lapPos.data.getDataFields();
        map.forEach((key, v) -> {
            List<Integer> laps = (List<Integer>) v;
            //im deleting odd rows here because theyre lap counters, not positions
            for (int i = laps.size() - 2; i >= 0; i -= 2) {
                laps.remove(i);
            }
            driversMap.get(key.substring(1)).getLapByLapData().setPositions(laps);
        });
        List<LinkedHashMap> dr = (List<LinkedHashMap>) xtraData.getDataField("DR");
        AtomicInteger driverCounter = new AtomicInteger();
        dr.forEach(row -> {
            List<String> xData = (List<String>) row.get("X");
            List<Integer> tiData = (List<Integer>) row.get("TI");
            String tyreSequence = xData.get(9);
            for (int i = 0; i < tiData.size(); i = i + 3) {
                driversMap.get(driverCodes.get(driverCounter.get())).getLapByLapData().getTyres()
                        .add(new Tyre(String.valueOf(tyreSequence.charAt(tyreSequence.length() - 1 - (i / 3))), tiData.get(i + 1)));
            }
            driverCounter.incrementAndGet();
        });
    }

    private void enrichDriversWithBestData(Map<String, Driver> driversMap, RawData bestData, List<String> driverCodes) {
        List<LinkedHashMap> dr = (List<LinkedHashMap>) bestData.getDataField("DR");
        AtomicReference<Integer> counter = new AtomicReference<>(0);
        dr.forEach(row -> {
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
        dr.forEach(row -> {
            List<String> data = (List<String>) row.get("F");
            driversMap.get(data.get(0)).setPosition(Integer.valueOf(data.get(3)));
            driversMap.get(data.get(0)).setFinalGap(data.get(4));
        });
    }

    public void enrichDriversWithScoringData(Map<String, Driver> driversMap, ScoresGraph scoresGraph) {
        scoresGraph.steering.getDataFields().forEach((key, value) -> {
            List<Integer> list = (List<Integer>) value;
            driversMap.get(key.substring(1)).setSteering(list.get(1));
        });
        scoresGraph.gforceLat.getDataFields().forEach((key, value) -> {
            List<Integer> list = (List<Integer>) value;
            driversMap.get(key.substring(1)).setGforceLat(list.get(1));
        });
        scoresGraph.gforceLong.getDataFields().forEach((key, value) -> {
            List<Integer> list = (List<Integer>) value;
            driversMap.get(key.substring(1)).setGforceLong(list.get(1));
        });
        scoresGraph.brake.getDataFields().forEach((key, value) -> {
            List<Integer> list = (List<Integer>) value;
            driversMap.get(key.substring(1)).setBrake(list.get(1));
        });
        scoresGraph.performance.getDataFields().forEach((key, value) -> {
            List<Integer> list = (List<Integer>) value;
            driversMap.get(key.substring(1)).setPerformance(list.get(1));
        });
        scoresGraph.throttle.getDataFields().forEach((key, value) -> {
            List<Integer> list = (List<Integer>) value;
            driversMap.get(key.substring(1)).setThrottle(list.get(1));
        });
    }

    @PostConstruct
    void init() {
        this.restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }

}
