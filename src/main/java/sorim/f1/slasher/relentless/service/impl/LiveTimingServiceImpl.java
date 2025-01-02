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
import sorim.f1.slasher.relentless.model.KeyValueInteger;
import sorim.f1.slasher.relentless.model.enums.RoundEnum;
import sorim.f1.slasher.relentless.model.ergast.ErgastResponse;
import sorim.f1.slasher.relentless.model.livetiming.*;
import sorim.f1.slasher.relentless.scheduled.Scheduler;
import sorim.f1.slasher.relentless.service.*;
import sorim.f1.slasher.relentless.util.MainUtility;

import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LiveTimingServiceImpl implements LiveTimingService {

    private static final String LIVE_TIMING_URL_BASE = "https://livetiming.formula1.com/static/{year}/{grandPrix}/{race}/";
    private static final String TIMING_APP_DATA_STREAM = "TimingAppData.jsonStream";

    private static final String TIMING_APP_DATA = "TimingAppData.json";
    private static final String TEAM_RADIO_STREAM = "TeamRadio.jsonStream";
    private static final String DRIVER_LIST = "DriverList.json";
    private static final String TIMING_DATA_F1 = "TimingDataF1.json";
    private static final String CHAMPIONSHIP_PREDICTION = "ChampionshipPrediction.json";
    private static final String RACE_CONTROL_MESSAGES = "RaceControlMessages.json";
    private static final String TIMING_STATS = "TimingStats.json";
    private static final String WIKIPEDIA_IMAGES_URL_1 = "https://en.wikipedia.org/w/api.php?action=query&prop=pageimages&piprop=original&format=json&redirects&titles={title}";
    private static final String WIKIPEDIA_IMAGES_URL_2 = "https://en.wikipedia.org/w/api.php?action=query&prop=pageimages&piprop=thumbnail&pithumbsize=2000&format=json&redirects&titles={title}";
    private static final String SESSION_INFO_URL = "https://livetiming.formula1.com/static/SessionInfo.json";
    private final ErgastService ergastService;
    private final AdminService adminService;
    private final ClientService clientService;
    private final MainProperties properties;
    private final LiveTimingRadioService liveTimingRadioService;
    private final ObjectMapper mapper = new ObjectMapper();
    RestTemplate restTemplate = new RestTemplate();
    private String temporaryUrl;

    @Override
    public void getAllRaceDataFromErgastTable(String year, Boolean detailed, Boolean deleteOld) {
        List<RaceData> raceData = ergastService.fetchSeason(year);
        raceData.forEach(race -> {
            race.setCircuitId(race.getCircuit().getCircuitId());
            race.setImageUrl(getWikipediaOriginalImageUrl(race.getCircuit().getUrl(), race.getCircuit().getCircuitName(), false));
//            String response = getLiveTimingResponseOfErgastRace(race, RoundEnum.RACE, 1);
//            if (response != null) {
//                race.setLiveTimingRace(response.substring(response.indexOf("{")));
//                if (detailed) {
//                    response = getLiveTimingResponseOfErgastRace(race, RoundEnum.QUALIFYING, 1);
//                    if (response != null) {
//                        race.setLiveTimingQuali(response.substring(response.indexOf("{")));
//                    }
//                    response = getLiveTimingResponseOfErgastRace(race, RoundEnum.PRACTICE_1, 1);
//                    if (response != null) {
//                        race.setLiveTimingFp1(response.substring(response.indexOf("{")));
//                    }
//                    response = getLiveTimingResponseOfErgastRace(race, RoundEnum.PRACTICE_2, 1);
//                    if (response != null) {
//                        race.setLiveTimingFp2(response.substring(response.indexOf("{")));
//                    }
//                    response = getLiveTimingResponseOfErgastRace(race, RoundEnum.PRACTICE_3, 1);
//                    if (response != null) {
//                        race.setLiveTimingFp3(response.substring(response.indexOf("{")));
//                    }
//
//                }
//
//            }
        });
        if (deleteOld) {
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
            apiUrl = WIKIPEDIA_IMAGES_URL_2;
            image = "thumbnail";
        } else {
            apiUrl = WIKIPEDIA_IMAGES_URL_1;
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
                    return getWikipediaOriginalImageUrl(url, circuitName, true);
                }
            } catch (Exception ex) {
                log.error(title + " WORKAROUND FAILED");
                log.error("getWikipediaOriginalImageUrl error", ex);
                ex.printStackTrace();
            }
        }
        return null;
    }


    private String getLiveTimingResponseOfErgastRace(RaceData raceData, RoundEnum round, String liveTimingEndpoint) {
        String response;
        String url = LIVE_TIMING_URL_BASE + liveTimingEndpoint;
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
            case SPRINT_SHOOTOUT:
                date = MainUtility.subtractDays(raceDate, 2);
              //  raceName = date + "_Sprint_Shootout";
                raceName = date + "_Sprint_Qualifying";
                break;
            case SPRINT:
                date = MainUtility.subtractDays(raceDate, 1);
                raceName = date + "_Sprint";
                break;
        }
        log.info("https://livetiming.formula1.com/static/" + raceData.getSeason() + "/" + grandPrix + "/" + raceName + "/" + liveTimingEndpoint);

        try {
            response = restTemplate
                    .getForObject(url, String.class, raceData.getSeason(), grandPrix, raceName);
            temporaryUrl = "https://livetiming.formula1.com/static/" + raceData.getSeason() + "/" + grandPrix + "/" + raceName + "/";
            return response;
        } catch (Exception e) {
            Logger.log("getLiveTimingResponseOfErgastRace1 " + grandPrix, e.getMessage());
            if (round.equals(RoundEnum.PRACTICE_2)) {
                date = MainUtility.subtractDays(raceDate, 1);
                raceName = date + "_Practice_2";
            } else if (round.equals(RoundEnum.QUALIFYING)) {
                log.info("checking if quali the same day as race (brazil 2024)");
                date = MainUtility.subtractDays(raceDate, 0);
                raceName = date + "_Qualifying";
            } else {
                return null;
            }
            try {
                response = restTemplate
                        .getForObject(url, String.class, raceData.getSeason(), grandPrix, raceName);
                temporaryUrl = "https://livetiming.formula1.com/static/" + raceData.getSeason() + "/" + grandPrix + "/" + raceName + "/";
                return response;
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
    public Integer analyzeLatestRace(Boolean updateStatistics) {
        RaceData raceData = ergastService.getLatestNonAnalyzedRace(properties.getCurrentSeasonFuture());
        analyzeRaceData(raceData, updateStatistics);
        return getNextRefreshTime(-6000);
    }

    @Override
    public Boolean analyzeRace(Integer season, Integer round) {
        RaceData raceData = ergastService.findRaceBySeasonAndRound(String.valueOf(season), round);
        analyzeRaceData(raceData, false);
        return true;
    }

    private Integer getNextRefreshTime(Integer seconds) {
        Boolean isGenerating = checkIfEventIsGenerating();
        if (isGenerating) {
            //600 sekundi = 10 minuta
            return 600;
        } else {
            return adminService.getNextRefreshTimeUsingCalendar(seconds);
        }
    }

    private void analyzeRaceData(RaceData raceData, Boolean updateStatistics) {
        if (raceData != null) {
            String driverListResponse = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.RACE, DRIVER_LIST);
            String timingDataF1Response = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.RACE, TIMING_DATA_F1);
            if (timingDataF1Response != null) {
                raceData.setLiveTimingRace(timingDataF1Response.substring(timingDataF1Response.indexOf("{")));
                ergastService.saveRace(raceData);
                RaceAnalysis analysis = fetchNewRaceAnalysis(driverListResponse, timingDataF1Response, raceData.getCircuit().getCircuitId(), updateStatistics);
                adminService.updateOverlays(analysis);
                 clientService.setNavbarData();
                analyzeUpcomingRace(false);
                Scheduler.analysisDone = true;
            }
        }
    }

    @Override
    public Boolean resetLatestRaceAnalysis() {
        RaceData raceData = ergastService.getLatestAnalyzedRace();
        ergastService.getLatestAnalyzedRace().getRaceAnalysis();
        String art = raceData.getRaceAnalysis().getArt();
        raceData.setRaceAnalysis(null);
        raceData.setLiveTimingRace(null);
        raceData.setTimingAppData(null);
        ergastService.saveRace(raceData);
        analyzeLatestRace(true);
        raceData = ergastService.getLatestAnalyzedRace();
        raceData.getRaceAnalysis().setArt(art);
        ergastService.saveRace(raceData);
        return true;
    }

    @Override
    public Boolean deleteLatestRaceAnalysis() {
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
        String response = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.RACE, "TODO");
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
        RaceData raceData = ergastService.getUpcomingRace(properties.getCurrentSeasonFuture());
        if (raceData == null) {
            return null;
        }
        return raceData.getUpcomingRaceAnalysis();
    }

    @Override
    public RaceData getUpcomingRace() {
        return ergastService.getUpcomingRace(properties.getCurrentSeasonFuture());
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
    public String updateCircuitImage(String season, Integer round, String newImageUrl) {
        RaceData raceData = ergastService.findRaceBySeasonAndRound(season, round);
        String response = raceData.getUpcomingRaceAnalysis().getImageUrl() + " -> " + newImageUrl;
        raceData.getUpcomingRaceAnalysis().setImageUrl(newImageUrl);
        ergastService.saveRace(raceData);
        return response;
    }


    @Override
    public RaceData backupRaceData(Integer id) {
        return ergastService.findById(id);
    }

    @Override
    public RaceData restoreRaceData(Integer id, RaceData body) {
        body.setId(id);
        ergastService.saveRace(body);
        return body;
    }

    @Override
    public boolean checkIfEventIsGenerating() {
        SessionInfo sessionInfo = getSessionInfo();
        return sessionInfo.getArchiveStatus().getStatus().equals("Generating");
    }

    @Override
    public boolean checkIfRaceIsGenerating() {
        SessionInfo sessionInfo = getSessionInfo();
        Boolean isRace = sessionInfo.getType().equals("Race");
        Boolean isGenerating = sessionInfo.getArchiveStatus().getStatus().equals("Generating");
        log.info("checkIfRaceIsGenerating: {} - {}", isRace, isGenerating);
        return isRace && isGenerating;
    }

    @Override
    public SessionInfo getSessionInfo() {
        return restTemplate.getForObject(SESSION_INFO_URL, SessionInfo.class);
    }

    @Override
    public Boolean upcomingRacesAnalysisInitialLoad(String season) {
        Integer howManySeasonsBack = properties.getHowManySeasonsBack();
        List<RaceData> races = ergastService.findRacesBySeason(season);
        if (!races.isEmpty()) {
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
            for (int i = 0; i <= howManySeasonsBack; i++) {
                Integer round = 1;
                do {
                    response = ergastService.getResultsByRound(properties.getCurrentSeasonPast() - i, round);

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
        }
        return true;
    }

    @Override
    public Integer analyzeUpcomingRace(Boolean redo) {
        if (redo == null) {
            redo = false;
        }
        RaceData raceData = ergastService.getLatestNonAnalyzedRace(properties.getCurrentSeasonFuture());
        if (raceData != null) {
            if (raceData.getLiveTimingFp1() == null || redo) {
                String driverListResponse = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.PRACTICE_1, DRIVER_LIST);
                String timingDataF1Response = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.PRACTICE_1, TIMING_DATA_F1);
                String timingStatsString = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.PRACTICE_1, TIMING_STATS);
                if (timingDataF1Response != null) {
                    raceData.setLiveTimingFp1(timingDataF1Response.substring(timingDataF1Response.indexOf("{")));
                    List<Driver> drivers = createDriverList(driverListResponse.substring(driverListResponse.indexOf("{")), timingDataF1Response.substring(timingDataF1Response.indexOf("{")));

                    List<TimingStat> timingStats = getTimingStats(drivers, timingStatsString.substring(timingStatsString.indexOf("{")));
                    List<KeyValueInteger> topSpeeds =  getTopSpeeds(timingStats);
                    raceData.getUpcomingRaceAnalysis().setFp1(drivers);
                    raceData.getUpcomingRaceAnalysis().getTimingStats().setFp1(timingStats);
                    raceData.getUpcomingRaceAnalysis().getTopSpeeds().setFp1(topSpeeds);

                    String timingAppDataResponse = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.PRACTICE_1, TIMING_APP_DATA_STREAM);
                    Map<String, String> driverMap = drivers.stream()
                            .collect(Collectors.toMap(Driver::getNum, Driver::getFullName));
                    raceData.getUpcomingRaceAnalysis().setFp1Laps(createLapTimeDataList(timingAppDataResponse, driverMap, "Practice 1"));
                    String radioDataResponse = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.PRACTICE_1, TEAM_RADIO_STREAM);
                    raceData.getUpcomingRaceAnalysis().setFp1LivetimingUrl(temporaryUrl);
                    liveTimingRadioService.enrichUpcomingRaceAnalysisWithRadioData(raceData.getUpcomingRaceAnalysis(), radioDataResponse, RoundEnum.PRACTICE_1);

                }
            }
            if (raceData.getLiveTimingFp2() == null || redo) {
                String driverListResponse = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.PRACTICE_2, DRIVER_LIST);
                String timingDataF1Response = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.PRACTICE_2, TIMING_DATA_F1);
                String timingStatsString = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.PRACTICE_2, TIMING_STATS);
                if (timingDataF1Response != null) {
                    raceData.setLiveTimingFp2(timingDataF1Response.substring(timingDataF1Response.indexOf("{")));
                    List<Driver> drivers = createDriverList(driverListResponse.substring(driverListResponse.indexOf("{")), timingDataF1Response.substring(timingDataF1Response.indexOf("{")));

                    List<TimingStat> timingStats = getTimingStats(drivers, timingStatsString.substring(timingStatsString.indexOf("{")));
                    List<KeyValueInteger> topSpeeds =  getTopSpeeds(timingStats);
                    raceData.getUpcomingRaceAnalysis().setFp2(drivers);
                    raceData.getUpcomingRaceAnalysis().getTimingStats().setFp2(timingStats);
                    raceData.getUpcomingRaceAnalysis().getTopSpeeds().setFp2(topSpeeds);

                    String timingAppDataResponse = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.PRACTICE_2, TIMING_APP_DATA_STREAM);
                    Map<String, String> driverMap = drivers.stream()
                            .collect(Collectors.toMap(Driver::getNum, Driver::getFullName));
                    raceData.getUpcomingRaceAnalysis().setFp2Laps(createLapTimeDataList(timingAppDataResponse, driverMap, "Practice 2"));
                    String radioDataResponse = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.PRACTICE_2, TEAM_RADIO_STREAM);
                    raceData.getUpcomingRaceAnalysis().setFp2LivetimingUrl(temporaryUrl);
                    liveTimingRadioService.enrichUpcomingRaceAnalysisWithRadioData(raceData.getUpcomingRaceAnalysis(), radioDataResponse, RoundEnum.PRACTICE_2);
                }
            }
            if (raceData.getLiveTimingFp3() == null || redo) {
                String driverListResponse = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.PRACTICE_3, DRIVER_LIST);
                String timingDataF1Response = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.PRACTICE_3, TIMING_DATA_F1);
                String timingStatsString = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.PRACTICE_3, TIMING_STATS);
                if (timingDataF1Response != null) {
                    raceData.setLiveTimingFp3(timingDataF1Response.substring(timingDataF1Response.indexOf("{")));
                    List<Driver> drivers = createDriverList(driverListResponse.substring(driverListResponse.indexOf("{")), timingDataF1Response.substring(timingDataF1Response.indexOf("{")));

                    List<TimingStat> timingStats = getTimingStats(drivers, timingStatsString.substring(timingStatsString.indexOf("{")));
                    List<KeyValueInteger> topSpeeds =  getTopSpeeds(timingStats);
                    raceData.getUpcomingRaceAnalysis().setFp3(drivers);
                    raceData.getUpcomingRaceAnalysis().getTimingStats().setFp3(timingStats);
                    raceData.getUpcomingRaceAnalysis().getTopSpeeds().setFp3(topSpeeds);

                    String timingAppDataResponse = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.PRACTICE_3, TIMING_APP_DATA_STREAM);
                    Map<String, String> driverMap = drivers.stream()
                            .collect(Collectors.toMap(Driver::getNum, Driver::getFullName));
                    raceData.getUpcomingRaceAnalysis().setFp3Laps(createLapTimeDataList(timingAppDataResponse, driverMap, "Practice 3"));
                    String radioDataResponse = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.PRACTICE_3, TEAM_RADIO_STREAM);
                    raceData.getUpcomingRaceAnalysis().setFp3LivetimingUrl(temporaryUrl);
                    liveTimingRadioService.enrichUpcomingRaceAnalysisWithRadioData(raceData.getUpcomingRaceAnalysis(), radioDataResponse, RoundEnum.PRACTICE_3);

                }
            }
            if (raceData.getLiveTimingQuali() == null || redo) {
                String driverListResponse = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.QUALIFYING, DRIVER_LIST);
                String timingDataF1Response = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.QUALIFYING, TIMING_DATA_F1);
                String timingStatsString = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.QUALIFYING, TIMING_STATS);
                if (timingDataF1Response != null) {
                    raceData.setLiveTimingQuali(timingDataF1Response.substring(timingDataF1Response.indexOf("{")));
                    List<Driver> drivers = createDriverList(driverListResponse.substring(driverListResponse.indexOf("{")), timingDataF1Response.substring(timingDataF1Response.indexOf("{")));

                    List<TimingStat> timingStats = getTimingStats(drivers, timingStatsString.substring(timingStatsString.indexOf("{")));
                    List<KeyValueInteger> topSpeeds =  getTopSpeeds(timingStats);
                    raceData.getUpcomingRaceAnalysis().setQuali(drivers);
                    raceData.getUpcomingRaceAnalysis().getTimingStats().setQuali(timingStats);
                    raceData.getUpcomingRaceAnalysis().getTopSpeeds().setQuali(topSpeeds);

                    String timingAppDataResponse = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.QUALIFYING, TIMING_APP_DATA_STREAM);
                    Map<String, String> driverMap = drivers.stream()
                            .collect(Collectors.toMap(Driver::getNum, Driver::getFullName));
                    raceData.getUpcomingRaceAnalysis().setQualiLaps(createLapTimeDataList(timingAppDataResponse, driverMap, "Qualifying"));
                    String radioDataResponse = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.QUALIFYING, TEAM_RADIO_STREAM);
                    raceData.getUpcomingRaceAnalysis().setQualiLivetimingUrl(temporaryUrl);
                    liveTimingRadioService.enrichUpcomingRaceAnalysisWithRadioData(raceData.getUpcomingRaceAnalysis(), radioDataResponse, RoundEnum.QUALIFYING);
                }
            }
            if (raceData.getLiveTimingSprintQuali() == null || redo) {
                String driverListResponse = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.SPRINT_SHOOTOUT, DRIVER_LIST);
                String timingDataF1Response = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.SPRINT_SHOOTOUT, TIMING_DATA_F1);
                String timingStatsString = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.SPRINT_SHOOTOUT, TIMING_STATS);
                if (timingDataF1Response != null) {
                    raceData.setLiveTimingSprintQuali(timingDataF1Response.substring(timingDataF1Response.indexOf("{")));
                    List<Driver> drivers = createDriverList(driverListResponse.substring(driverListResponse.indexOf("{")), timingDataF1Response.substring(timingDataF1Response.indexOf("{")));

                    List<TimingStat> timingStats = getTimingStats(drivers, timingStatsString.substring(timingStatsString.indexOf("{")));
                    List<KeyValueInteger> topSpeeds =  getTopSpeeds(timingStats);
                    raceData.getUpcomingRaceAnalysis().setSprintQuali(drivers);
                    raceData.getUpcomingRaceAnalysis().getTimingStats().setSprintShootout(timingStats);
                    raceData.getUpcomingRaceAnalysis().getTopSpeeds().setSprintShootout(topSpeeds);

                    String timingAppDataResponse = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.SPRINT_SHOOTOUT, TIMING_APP_DATA_STREAM);
                    Map<String, String> driverMap = drivers.stream()
                            .collect(Collectors.toMap(Driver::getNum, Driver::getFullName));
                    raceData.getUpcomingRaceAnalysis().setSprintQualiLaps(createLapTimeDataList(timingAppDataResponse, driverMap, "Sprint Shootout"));
                    String radioDataResponse = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.SPRINT_SHOOTOUT, TEAM_RADIO_STREAM);
                    raceData.getUpcomingRaceAnalysis().setSprintQualiLivetimingUrl(temporaryUrl);
                    liveTimingRadioService.enrichUpcomingRaceAnalysisWithRadioData(raceData.getUpcomingRaceAnalysis(), radioDataResponse, RoundEnum.SPRINT_SHOOTOUT);
                }
            }
                if (raceData.getLiveTimingSprintQuali() != null) {
                    String driverListResponse = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.SPRINT, DRIVER_LIST);
                    String timingDataF1Response = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.SPRINT, TIMING_DATA_F1);
                    String timingAppData = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.SPRINT, TIMING_APP_DATA);
                    String timingStatsString = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.SPRINT, TIMING_STATS);
                    if (timingDataF1Response != null) {
                        raceData.setLiveTimingFp3(timingDataF1Response.substring(timingDataF1Response.indexOf("{")));
                        List<Driver> drivers = createDriverListWithTyres(driverListResponse.substring(driverListResponse.indexOf("{")),
                                timingDataF1Response.substring(timingDataF1Response.indexOf("{")), timingAppData.substring(timingAppData.indexOf("{")));

                        String lapSeriesString = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.SPRINT, "LapSeries.json");
                        getLapSeries(drivers, lapSeriesString.substring(lapSeriesString.indexOf("{")));
                        List<TimingStat> timingStats = getTimingStats(drivers, timingStatsString.substring(timingStatsString.indexOf("{")));
                        List<KeyValueInteger> topSpeeds =  getTopSpeeds(timingStats);
                        raceData.getUpcomingRaceAnalysis().setSprint(drivers);
                        raceData.getUpcomingRaceAnalysis().getTimingStats().setSprint(timingStats);
                        raceData.getUpcomingRaceAnalysis().getTopSpeeds().setSprint(topSpeeds);

                        String timingAppDataResponse = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.SPRINT, TIMING_APP_DATA_STREAM);
                        Map<String, String> driverMap = drivers.stream()
                                .collect(Collectors.toMap(Driver::getNum, Driver::getFullName));
                        raceData.getUpcomingRaceAnalysis().setSprintLaps(createLapTimeDataList(timingAppDataResponse, driverMap, "Sprint"));


                        String radioDataResponse = getLiveTimingResponseOfErgastRace(raceData, RoundEnum.SPRINT, TEAM_RADIO_STREAM);
                        raceData.getUpcomingRaceAnalysis().setSprintLivetimingUrl(temporaryUrl);
                        liveTimingRadioService.enrichUpcomingRaceAnalysisWithRadioData(raceData.getUpcomingRaceAnalysis(), radioDataResponse, RoundEnum.SPRINT);
                    }
                }



            ergastService.saveRace(raceData);
        } else {
            properties.checkCurrentSeasonFuture();
        }

        clientService.setNavbarData();
        return getNextRefreshTime(-6000);
    }

    private List<LapTimeData> createLapTimeDataList(String timingAppData, Map<String, String> driverMap, String sessionName) {
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
                log.error("createLapTimeDataList JsonProcessingException", e);
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
                            entry.setSessionName(sessionName);
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
        List<RaceData> races = ergastService.findRacesBySeason(String.valueOf(properties.getCurrentSeasonPast()));
        races.forEach(race -> {
            race.setImageUrl(getWikipediaOriginalImageUrl(race.getCircuit().getUrl(), race.getCircuit().getCircuitName(), false));
        });
        ergastService.saveRaces(races);
        return true;
    }

    private List<Driver> createDriverList(String driverList, String timingDataF1) {
        List<Driver> driversResponse = new ArrayList<>();
        try {
            TypeReference<HashMap<String, Driver>> typeRef = new TypeReference<HashMap<String, Driver>>() {
            };
            Map<String, Driver> driverListMap = mapper.readValue(driverList, typeRef);

            TimingDataF1Root root = mapper.readValue(timingDataF1, TimingDataF1Root.class);
            root.getLines().forEach((k, v) -> {
                driverListMap.get(k).setTimingDataF1(v);
                driverListMap.get(k).setPosition(Integer.valueOf(driverListMap.get(k).getTimingDataF1().getPosition()));
                driversResponse.add(driverListMap.get(k));
            });
        } catch (Exception e) {
            log.error("createLapTimeDataList JsonProcessingException", e);
            e.printStackTrace();
        }
        driversResponse.sort(Comparator.comparing(Driver::getPosition));
        return driversResponse;
    }

    private List<Driver> createDriverListWithTyres(String driverList, String timingDataF1, String timingAppData) {
        List<Driver> driversResponse = createDriverList(driverList, timingDataF1);
        try {
            TimingAppData root = mapper.readValue(timingAppData, TimingAppData.class);
            root.getLines().forEach((k, v) -> {
                Optional<Driver> optional = driversResponse.stream().filter(entry -> Objects.equals(k, entry.getNum())).findFirst();
                optional.ifPresent(driver -> driver.getLapByLapData().setTyres(v.getStints()));
            });
            return driversResponse;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public RaceAnalysis fetchNewRaceAnalysis(String driverListResponse, String timingDataF1Response, String circuitId, Boolean updateStatistics) {
        RaceData race = ergastService.findByCircuitIdOrderBySeasonDesc(circuitId);
        String timingAppData = getLiveTimingResponseOfErgastRace(race, RoundEnum.RACE, TIMING_APP_DATA);

        List<Driver> driversResponse = createDriverListWithTyres(driverListResponse.substring(driverListResponse.indexOf("{")),
                timingDataF1Response.substring(timingDataF1Response.indexOf("{")), timingAppData.substring(timingAppData.indexOf("{")));
        Map<String, Driver> driversMap = driversResponse.stream()
                .collect(Collectors.toMap(Driver::getInitials, Function.identity(), (o1, o2) -> o1, TreeMap::new));

        String title = race.getRaceName();
        Map<String, String> ergastCodes = mapErgastWithLiveTiming(driversMap);
        Boolean bool = false;
        try {
            bool = enrichDriversWithErgast(driversMap, ergastCodes, race.getSeason(), race.getRound());
            Boolean ergastDataAvailable = bool;
            List<Driver> drivers = new ArrayList<>(driversMap.values());

            String radioDataResponse = getLiveTimingResponseOfErgastRace(race, RoundEnum.RACE, TEAM_RADIO_STREAM);
            List<RadioData> radioData = liveTimingRadioService.enrichRaceAnalysisWithRadioData(drivers, radioDataResponse);

            String championshipPredictionString = getLiveTimingResponseOfErgastRace(race, RoundEnum.RACE, CHAMPIONSHIP_PREDICTION);
            List<List<ChampionshipPrediction>> championshipPredictions = getChampionshipPrediction(drivers, championshipPredictionString.substring(championshipPredictionString.indexOf("{")));

            String raceControlMessagesString = getLiveTimingResponseOfErgastRace(race, RoundEnum.RACE, RACE_CONTROL_MESSAGES);
            List<RaceControlMessage> raceControlMessages = getRaceControlMessages(drivers, raceControlMessagesString.substring(raceControlMessagesString.indexOf("{")));

            String timingStatsString = getLiveTimingResponseOfErgastRace(race, RoundEnum.RACE, TIMING_STATS);
            List<TimingStat> timingStats = getTimingStats(drivers, timingStatsString.substring(timingStatsString.indexOf("{")));
            List<String> bestSpeedKeys = new ArrayList<>(timingStats.get(0).bestSpeeds.keySet());
            List<KeyValueInteger> topSpeeds =  getTopSpeeds(timingStats);

            String lapSeriesString = getLiveTimingResponseOfErgastRace(race, RoundEnum.RACE, "LapSeries.json");
            List<LapSeries> lapSeries = getLapSeries(drivers, lapSeriesString.substring(lapSeriesString.indexOf("{")));
            drivers.sort(Comparator.comparing(Driver::getPosition));

            RaceAnalysis analysis = RaceAnalysis.builder()
                    .driverData(drivers)
                    .livetimingUrl(temporaryUrl)
                    .year(properties.getCurrentSeasonFuture())
                    .radioData(radioData)
                    .driversChampionshipPrediction(championshipPredictions.get(0))
                    .teamsChampionshipPrediction(championshipPredictions.get(1))
                    .lapSeries(lapSeries)
                    .raceControlMessages(raceControlMessages)
                    .timingStats(timingStats)
                    .bestSpeedKeys(bestSpeedKeys)
                    .topSpeeds(topSpeeds)
                    .status(1)
                    .title(title).build();
            race.setRaceAnalysis(analysis);
            if (!ergastDataAvailable) {
             //   race.setLiveTimingRace(null);
                analysis.setStatus(2);
            }
            race.setRaceAnalysis(analysis);
            ergastService.saveRace(race);
            log.info("raceAnalysis done");
            return analysis;
        } catch (Exception e) {
            log.error("fetchNewRaceAnalysis error", e);
            e.printStackTrace();
        }
   try {
        if (bool) {
            adminService.initializeStandings(updateStatistics);
        } else {
            adminService.initializeStandings(false);
        }
    } catch (Exception e) {
        log.error("fetchNewRaceAnalysis initializeStandings error", e);
        e.printStackTrace();
    }
        return null;
    }

    private List<RaceControlMessage> getRaceControlMessages(List<Driver> drivers, String input) {

        try {
            RaceControlMessageRoot root = mapper.readValue(input, RaceControlMessageRoot.class);
            root.getMessages().forEach(message -> {
                if (message.getRacingNumber() != null) {
                    Optional<Driver> optional = drivers.stream().filter(entry -> message.getRacingNumber().equals(entry.getNum())).findFirst();
                    optional.ifPresent(driver -> message.setName(driver.getLastName()));
                }
            });
            return root.getMessages();
        } catch (JsonProcessingException e) {
            log.error("createLapTimeDataList JsonProcessingException", e);
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private List<LapSeries> getLapSeries(List<Driver> driversList, String input) {
        try {
            List<LapSeries> response = new ArrayList<>();
            TypeReference<HashMap<String, LapSeries>> typeRef = new TypeReference<HashMap<String, LapSeries>>() {
            };
            Map<String, LapSeries> lapSeriesMap = mapper.readValue(input, typeRef);

            lapSeriesMap.forEach((k, v) -> {
                Optional<Driver> optional = driversList.stream().filter(entry -> k.equals(entry.getNum())).findFirst();
                if (optional.isPresent()) {
                    v.setName(optional.get().getLastName());
                    List<Integer> positions = new ArrayList<>();
                    v.getLapPosition().forEach(string->{
                        positions.add(Integer.valueOf(string));
                    });
                    optional.get().getLapByLapData().setPositions(positions);
                    response.add(v);
                }
            });
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<List<ChampionshipPrediction>> getChampionshipPrediction(List<Driver> driversList, String input) {
        try {
            List<List<ChampionshipPrediction>> response = new ArrayList<>();
            ChampionshipPredictionRoot root = mapper.readValue(input, ChampionshipPredictionRoot.class);
            List<ChampionshipPrediction> drivers = new ArrayList<>();
            List<ChampionshipPrediction> teams = new ArrayList<>();
            root.getDrivers().forEach((k, v) -> {
                if (!Objects.equals(k, "")) {
                    Optional<Driver> optional = driversList.stream().filter(entry -> k.equals(entry.getNum())).findFirst();
                    if (optional.isPresent()) {
                        v.setName(optional.get().getLastName());
                        drivers.add(v);
                    }
                }
            });
            root.getTeams().forEach((k, v) -> {
                if (!Objects.equals(k, "")) {
                    teams.add(v);
                }
            });
            drivers.sort(Comparator.comparing(ChampionshipPrediction::getPredictedPosition));
            teams.sort(Comparator.comparing(ChampionshipPrediction::getPredictedPosition));
            response.add(drivers);
            response.add(teams);
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<TimingStat> getTimingStats(List<Driver> driversList, String input) {
        try {
            TimingStats root = mapper.readValue(input, TimingStats.class);
            List<TimingStat> response = new ArrayList<>();
           // List<String> bestSpeedKeys = new ArrayList<>(timingStats.get(0).bestSpeeds.keySet());
            root.getLines().forEach((k, v) -> {
                Optional<Driver> optional = driversList.stream().filter(entry -> k.equals(entry.getNum())).findFirst();
                if (optional.isPresent()) {
                    v.setName(optional.get().getLastName());
                    response.add(v);
                }
            });
            response.sort(Comparator.comparing(n -> n.getPersonalBestLapTime().getPosition(), Comparator.nullsLast(Comparator.naturalOrder())));
            return response;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private List<KeyValueInteger> getTopSpeeds(List<TimingStat> timingStats) {
        List<KeyValueInteger> response = new ArrayList<>();
        timingStats.forEach(driver->{
                AtomicReference<Integer> topSpeed = new AtomicReference<>(0);
                driver.getBestSpeeds().forEach((k,v)->{
                    if(v.getValue()!=null && !v.getValue().isEmpty()){
                        Integer currentSpeed = Integer.valueOf(v.getValue());
                        if(currentSpeed> topSpeed.get()){
                            topSpeed.set(currentSpeed);
                        }
                    }
                });
            driver.setTopSpeed(topSpeed.get());
            response.add(KeyValueInteger.builder().key(driver.getName()).value(topSpeed.get()).build());
            });
           response.sort(Comparator.comparing(KeyValueInteger::getValue).reversed());
            return response;
    }

    private Map<String, String> mapErgastWithLiveTiming(Map<String, Driver> driversMap) {
        Map<String, String> ergastCodes = ergastService.connectDriverCodesWithErgastCodes();
        Boolean missingDriver = addErgastCodesToDrivers(driversMap, ergastCodes);
        if (missingDriver) {
            ergastService.fetchCurrentDrivers();
            ergastCodes = ergastService.connectDriverCodesWithErgastCodes();
            addErgastCodesToDrivers(driversMap, ergastCodes);
        }
        return ergastCodes;
    }

    private Boolean addErgastCodesToDrivers(Map<String, Driver> driversMap, Map<String, String> ergastCodes) {
        AtomicReference<Boolean> driverMissing = new AtomicReference<>(false);
        driversMap.forEach((k, v) -> {
            Optional<Map.Entry<String, String>> driverEntry = ergastCodes.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue().equals(k)).findFirst();
            if (driverEntry.isPresent()) {
                v.setErgastCode(driverEntry.get().getKey());
            } else {
                driverMissing.set(true);
            }
        });
        return driverMissing.get();
    }

    private Boolean enrichDriversWithErgast(Map<String, Driver> driversMap, Map<String, String> ergastCodes, String season, Integer round) throws JsonProcessingException {
        try {
        ErgastResponse response = ergastService.getRaceLaps(Integer.valueOf(season), round);
        if (response.getMrData().getRaceTable().getRaces().size() > 0) {
            //empty livetiming positions array, to fill with ergast positions because sometimes theyre not equal (Rflag..)
            driversMap.forEach((k, v) -> {
                        v.getLapByLapData().setPositions(new ArrayList<>());
                    }
            );
            response.getMrData().getRaceTable().getRaces().get(0).getLaps().forEach(lap -> {
                Map<Integer, List<String>> sortingMap = new HashMap<>();
                List<Integer> listOfTimes = new ArrayList<>();
                lap.getTimings().forEach(timing -> {
                    if (ergastCodes.containsKey(timing.getDriverId())) {
                        String liveTimingCode = ergastCodes.get(timing.getDriverId());
                        Integer msTime = driversMap.get(liveTimingCode).getLapByLapData().addLapTime(lap.getNumber(), timing);
                        if (!sortingMap.containsKey(msTime)) {
                            List<String> codes = new ArrayList<>();
                            codes.add(liveTimingCode);
                            sortingMap.put(msTime, codes);
                            listOfTimes.add(msTime);
                        } else {
                            sortingMap.get(msTime).add(liveTimingCode);
                        }
                    } else {
                        log.error("KEY NOT FOUND: {}", timing.getDriverId());
                    }
                });
                Collections.sort(listOfTimes);
                AtomicReference<Integer> lapPosition = new AtomicReference<>(1);
                for (Integer msTime : listOfTimes) {
                    sortingMap.get(msTime).forEach((ergastCode) -> {
                        driversMap.get(ergastCode).getLapByLapData().addLapTimePosition(ergastCode, lap.getNumber(), lapPosition.get());
                        lapPosition.set(lapPosition.get() + 1);
                    });
                }
            });
            //pitstopi
            ErgastResponse pitstopResponse = ergastService.getRacePitStops(Integer.valueOf(season), round);
            if (pitstopResponse.getMrData().getRaceTable().getRaces().size() > 0) {
                pitstopResponse.getMrData().getRaceTable().getRaces().get(0).getPitStops().forEach(pitstop -> {
                    if (ergastCodes.containsKey(pitstop.getDriverId())) {
                        driversMap.get(ergastCodes.get(pitstop.getDriverId())).getPitstops().add(pitstop.getLap());
                    } else {
                        log.error("KEY NOT FOUND: {}", pitstop.getDriverId());
                    }
                });
            }
            //finalResult
            ErgastResponse resultsResponse = ergastService.getResultsByRound(Integer.valueOf(season), round);
            if (resultsResponse.getMrData().getRaceTable().getRaces().size() > 0) {
                resultsResponse.getMrData().getRaceTable().getRaces().get(0).getResults().forEach(result -> {
                    if (ergastCodes.containsKey(result.getDriver().getDriverId())) {
                        if (driversMap.containsKey(ergastCodes.get(result.getDriver().getDriverId()))) {
                            driversMap.get(ergastCodes.get(result.getDriver().getDriverId())).setFinishStatus(result.getPositionText());
                        }
                    }
                });
            }
            return true;
        } else {
            return false;
        }
    } catch (Exception e) {
        log.error("enrichDriversWithErgast error ", e);
        return false;
    }
    }


    @PostConstruct
    void init() {
        this.restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }

}
