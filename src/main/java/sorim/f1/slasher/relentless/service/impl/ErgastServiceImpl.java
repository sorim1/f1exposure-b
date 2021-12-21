package sorim.f1.slasher.relentless.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sorim.f1.slasher.relentless.configuration.MainProperties;
import sorim.f1.slasher.relentless.entities.*;
import sorim.f1.slasher.relentless.entities.ergast.RaceData;
import sorim.f1.slasher.relentless.handling.Logger;
import sorim.f1.slasher.relentless.model.*;
import sorim.f1.slasher.relentless.model.ergast.Circuit;
import sorim.f1.slasher.relentless.model.ergast.ErgastDriver;
import sorim.f1.slasher.relentless.model.ergast.ErgastResponse;
import sorim.f1.slasher.relentless.model.ergast.ErgastStanding;
import sorim.f1.slasher.relentless.repository.DriverRepository;
import sorim.f1.slasher.relentless.repository.ErgastRaceRepository;
import sorim.f1.slasher.relentless.repository.JsonRepository;
import sorim.f1.slasher.relentless.service.ErgastService;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ErgastServiceImpl implements ErgastService {
    private final static String CURRENT_SEASON = "http://ergast.com/api/f1/current.json";
    private final static String GET_SEASON = "https://ergast.com/api/f1/{year}.json";
    private final static String ERGAST_URL = "https://ergast.com/api/f1/";
    private final static String WIKIPEDIA_URL = "https://en.wikipedia.org/api/rest_v1/page/summary/";
    private final MainProperties properties;
    private final ErgastRaceRepository ergastRaceRepository;
    private final DriverRepository driverRepository;
    private final JsonRepository jsonRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private static Map<Integer, Integer> roundDriverCount = new HashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public List<RaceData> fetchSeason(String year) {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("year", year);

        ErgastResponse ergastResponse = restTemplate
                .getForObject(GET_SEASON, ErgastResponse.class, uriVariables);
        return ergastResponse.getMrData().getRaceTable().getRaces();
    }

    @Override
    public void saveRace(RaceData raceData) {
        ergastRaceRepository.save(raceData);
    }

    @Override
    public List<RaceData> fetchCurrentSeason() {
        ErgastResponse ergastResponse = restTemplate
                .getForObject(CURRENT_SEASON, ErgastResponse.class);
        return ergastResponse.getMrData().getRaceTable().getRaces();
    }

    @Override
    public void saveRaces(List<RaceData> raceData) {
        ergastRaceRepository.saveAll(raceData);
    }

    @Override
    public RaceData getLatestAnalyzedRace() {
        return ergastRaceRepository.findFirstByRaceAnalysisNotNullOrderByDateDesc();
    }

    @Override
    public RaceData getUpcomingRace(Integer currentYear) {
        RaceData response = ergastRaceRepository.findFirstByRaceAnalysisNullAndSeasonOrderByDateAsc(currentYear.toString());
        if (response == null) {
            Integer nextYear = currentYear + 1;
            response = ergastRaceRepository.findFirstByRaceAnalysisIsNullAndSeasonOrderByDateAsc(nextYear.toString());
        }
        return response;
    }

    @Override
    public RaceData getLatestNonAnalyzedRace(Integer currentYear) {
        RaceData response = ergastRaceRepository.findFirstByRaceAnalysisIsNotNullAndLiveTimingRaceIsNullAndSeasonOrderByDateAsc(String.valueOf(currentYear));
        if (response != null) {
            return response;
        }
        return ergastRaceRepository.findFirstByRaceAnalysisIsNullAndSeasonOrderByDateAsc(String.valueOf(currentYear));
    }

    @Override
    public List<RaceData> findByCircuitIdOrderBySeasonDesc(String circuitId) {
        return ergastRaceRepository.findByCircuitIdOrderBySeasonDesc(circuitId);
    }

    @Override
    public List<RaceData> findRacesBySeason(String season) {
        return ergastRaceRepository.findAllBySeason(season);
    }

    @Override
    public RaceData findRaceBySeasonAndRound(String season, Integer round) {
        return ergastRaceRepository.findAllBySeasonAndRound(season, round);
    }

    @Override
    public RaceData findById(Integer id) {
        return ergastRaceRepository.findAllById(id);
    }

    @Override
    public ErgastResponse getCurrentDriverStandings() {
        return restTemplate
                .getForObject(ERGAST_URL + "current/driverStandings.json", ErgastResponse.class);
    }

    @Override
    public ErgastResponse getConstructorStandings() {
        return restTemplate
                .getForObject(ERGAST_URL + "current/constructorStandings.json", ErgastResponse.class);
    }

    @Override
    public ErgastResponse getDriverStandingsByRound(Integer season, Integer round) {
        return restTemplate
                .getForObject(ERGAST_URL + season + "/" + round + "/driverStandings.json", ErgastResponse.class);
    }

    @Override
    public ErgastResponse getConstructorStandingsByRound(Integer season, Integer round) {
        return restTemplate
                .getForObject(ERGAST_URL + season + "/" + round + "/constructorStandings.json", ErgastResponse.class);
    }

    @Override
    public ErgastResponse getResultsByRound(Integer season, Integer round) {
        return restTemplate
                .getForObject(ERGAST_URL + season + "/" + round + "/results.json?limit=70", ErgastResponse.class);
    }

    private List<ErgastDriver> getAllErgastDrivers() {
        ErgastResponse response = restTemplate
                .getForObject(ERGAST_URL + "drivers.json?limit=1000", ErgastResponse.class);
        return response.getMrData().getDriverTable().getDrivers();
    }

    private List<Circuit> getAllErgastCircuits() {
        ErgastResponse response = restTemplate
                .getForObject(ERGAST_URL + "circuits.json?limit=500", ErgastResponse.class);
        return response.getMrData().getCircuitTable().getCircuits();
    }

    @Override
    public List<FrontendRace> getRacesSoFar(String season, Integer round) {
        return ergastRaceRepository.findAllBySeasonAndRoundLessThanEqualOrderByRoundAsc(season, round);
    }

    @Override
    public List<FrontendRace> getRacesOfSeason(String season) {
        return ergastRaceRepository.findAllBySeasonOrderByRoundAsc(season);
    }

    @Override
    public Map<String, String> connectDriverCodesWithErgastCodes() {
        List<ExposureDriver> drivers = driverRepository.findAll();
        Map<String, String> map = drivers.stream()
                .collect(Collectors.toMap(ExposureDriver::getErgastCode, ExposureDriver::getCode));
        return map;
    }

    @Override
    public ErgastResponse getRaceLaps(Integer season, Integer round) {
        return restTemplate
                .getForObject(ERGAST_URL + season + "/" + round + "/laps.json?limit=2000", ErgastResponse.class);
    }

    @Override
    public ErgastResponse getRacePitStops(Integer season, Integer round) {
        return restTemplate
                .getForObject(ERGAST_URL + season + "/" + round + "/pitstops.json?limit=1000", ErgastResponse.class);
    }

    @Override
    public void deleteRaces(String season) {
        ergastRaceRepository.deleteAllBySeason(season);
    }

    @Override
    public AllStandings fetchHistoricSeason(Integer season) {
        List<DriverStanding> driverStandings = getDriverStandingsByYear(season);
        List<ConstructorStanding> constructorStandings = getConstructorStandingsByYear(season);
        Map<String, DriverStandingByRound> driverStandingsByRound = new HashMap<>();
        Map<String, ConstructorStandingByRound> constructorStandingByRound = new HashMap<>();
        List<FrontendRace> races = new ArrayList<>();
        generateDriverStandingByRound(season, driverStandingsByRound, constructorStandingByRound, races);
        List<DriverStandingByRound> driverStandingsByRoundList = new ArrayList<>(driverStandingsByRound.values());
        List<JsonRepositoryModel> driverCharts = generateChartsDriverStandingsByRound(driverStandingsByRoundList);

        List<ChartSeries> constructorStandingByRoundChart = null;
        List<ChartSeries> constructorPointsByRoundChart = null;
        if(constructorStandingByRound.size()>0) {
            List<ConstructorStandingByRound> constructorStandingByRoundList = new ArrayList<>(constructorStandingByRound.values());
            List<JsonRepositoryModel> constructorCharts = generateChartsConstructorStandingsByRound(constructorStandingByRoundList);
            constructorStandingByRoundChart = (List<ChartSeries>) constructorCharts.get(0).getJson();
            constructorPointsByRoundChart = (List<ChartSeries>) constructorCharts.get(1).getJson();
        }
        AllStandings championship = AllStandings.builder()
                .driverStandings(driverStandings)
                .constructorStandings(constructorStandings)
                .driverStandingByRound((List<ChartSeries>) driverCharts.get(0).getJson())
                .driverPointsByRound((List<ChartSeries>) driverCharts.get(1).getJson())
                .driverResultByRound((List<ChartSeries>) driverCharts.get(2).getJson())
                .gridToResultChartWithDnf((List<ChartSeries>) driverCharts.get(3).getJson())
                .gridToResultChartWithoutDnf((List<ChartSeries>) driverCharts.get(4).getJson())
                .constructorStandingByRound(constructorStandingByRoundChart)
                .constructorPointsByRound(constructorPointsByRoundChart)
                .races(races)
                .currentYear(season).build();
        JsonRepositoryModel saveData = JsonRepositoryModel.builder().id("CHAMPIONSHIP_" + season)
                .json(championship).build();
        jsonRepository.save(saveData);
        return championship;
    }

    @Override
    public Object getHistoricSeason(Integer season) {
        JsonRepositoryModel jrm = jsonRepository.findAllById("CHAMPIONSHIP_" + season);
        return jrm.getJson();
    }

    @Override
    public Boolean fetchHistoricSeasonFull() throws InterruptedException {
        for(int i=1978;i<=2021;i++){
            log.info("fetch year: {}", i );
            fetchHistoricSeason(i);
            Thread.sleep(1000);
        }

        return true;
    }

    @Override
    public Boolean fetchDriverStatistics() {
        int firstSeason = 2005;
        List<ErgastDriver> allDrivers = generateAllErgastDrivers();
        Map<String, DriverStatistics> driversMap = new HashMap<>();
        allDrivers.forEach(ergastDriver -> driversMap.put(ergastDriver.getDriverId(), new DriverStatistics(ergastDriver)));
        for(int season = firstSeason; season<=properties.getCurrentYear()+1; season++){
           List<ErgastStanding> list = getErgastStandingsByYear(season);
            int finalSeason = season;
            list.forEach(es->{
                driversMap.get(es.getDriver().getDriverId()).pushSeasonStanding(finalSeason, es);
            });
            try {
                Thread.sleep(2000);
                log.info("seasonA over: {}", season);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }        }

        ErgastResponse response = getCurrentDriverStandings();
        response.getMrData().getStandingsTable().getStandingsLists().get(0)
                .getDriverStandings().forEach(es->{
                    driversMap.get(es.getDriver().getDriverId()).setCurrentConstructor(es.getConstructors().get(0));
                });

        for(int season = firstSeason; season<=properties.getCurrentYear(); season++){
            updateDriverWithRaceByRaceData(season, driversMap);
            try {
                Thread.sleep(2000);
                log.info("seasonB over: {}", season);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        driversMap.forEach((k,v)->{
            generateWikiProperties(v);
            JsonRepositoryModel saveData = JsonRepositoryModel.builder().id("DRIVER_"+k)
                    .json(v).build();
            jsonRepository.save(saveData);
        });

        return true;
    }

    private Boolean generateWikiProperties(DriverStatistics driver) {
            String driverId = driver.getUrl().substring(driver.getUrl().lastIndexOf("/")+1) ;
        Map<String, Object> mapping;
            try {
                String response =  restTemplate
                        .getForObject(WIKIPEDIA_URL +driverId, String.class);
                TypeReference<HashMap<String, Object>> typeRef = new TypeReference<>() {
            };
            mapping = mapper.readValue(response, typeRef);
            } catch (Exception e) {
                try {
                    String driverId2 = driver.getGivenName() + "_" + driver.getFamilyName() ;
                    String response =  restTemplate
                            .getForObject(WIKIPEDIA_URL +driverId2, String.class);
                    TypeReference<HashMap<String, Object>> typeRef = new TypeReference<>() {
                    };
                    mapping = mapper.readValue(response, typeRef);
                } catch (Exception e3) {
                    log.error("ERROR3: {}", driverId);
                    e3.printStackTrace();
                    return false;
                }
            }
            LinkedHashMap<String, Object> query = (LinkedHashMap<String, Object>) mapping.get("thumbnail");
                if(query!=null) {
                    String thumbnail = (String) query.get("source");
                    driver.setWikiImage(thumbnail);
                }
                String summary = (String) mapping.get("extract");
                driver.setWikiSummary(summary);

            return true;
    }

    private void updateDriverWithRaceByRaceData(int season, Map<String, DriverStatistics> driversMap) {
        Map<String, CircuitStatistics> circuitsMap = generateAllCircuits();
        List<RaceData> races = new ArrayList<>();
        boolean iterate;
        Integer round = 1;
        do {
            ErgastResponse response = getDriverStandingsByRound(season, round);
            if (response.getMrData().getTotal() > 0) {
                Integer finalRound = round;
                response.getMrData().getStandingsTable().getStandingsLists().get(0).getDriverStandings()
                        .forEach(es -> {
                            driversMap.get(es.getDriver().getDriverId()).addPointThroughSeason(season, finalRound, es);
                        });
                round++;
                iterate = true;
            } else {
                iterate = false;
            }
        } while (iterate);
        round = 1;
        do {
            ErgastResponse response = getResultsByRound(season, round);
            if (response.getMrData().getTotal() > 0) {
                response.getMrData().getRaceTable().getRaces().get(0).getResults()
                        .forEach(es -> {
                            driversMap.get(es.getDriver().getDriverId()).incrementRaceCounts(es);
                        });
                RaceData race = response.getMrData().getRaceTable().getRaces().get(0);
                races.add(race);
                circuitsMap.get(race.getCircuit().getCircuitId()).addRace(race);
                round++;
                iterate = true;
            } else {
                iterate = false;
            }
        } while (iterate);
        saveCircuitsAndRace(circuitsMap, races);
    }

    private void saveCircuitsAndRace(Map<String, CircuitStatistics> circuitsMap, List<RaceData> races) {
        List<JsonRepositoryModel> saveList = new ArrayList<>();
        circuitsMap.forEach((id,circuit) ->{
            JsonRepositoryModel entry = JsonRepositoryModel.builder().id("CIRCUIT_"+id)
                    .json(circuit).build();
            saveList.add(entry);
        });
        races.forEach((race) ->{
            JsonRepositoryModel entry = JsonRepositoryModel.builder().id("RACE_"+race.getSeason()+"_"+race.getRound())
                    .json(race).build();
            saveList.add(entry);
        });
        jsonRepository.saveAll(saveList);
    }

    private Map<String, CircuitStatistics> generateAllCircuits() {
        Map<String, CircuitStatistics> circuitsMap = new HashMap<>();
        List<Circuit> circuits = getAllErgastCircuits();
        circuits.forEach(circuit ->{
            circuitsMap.put(circuit.getCircuitId(), new CircuitStatistics(circuit));
        });
        return circuitsMap;
    }

    @Override
    public Object getErgastDrivers() {
        JsonRepositoryModel jrm = jsonRepository.findAllById("ALL_ERGAST_DRIVERS");
        return jrm.getJson();
    }

    @Override
    public Object getDriverStatistics(String driverId) {
        JsonRepositoryModel jrm = jsonRepository.findAllById("DRIVER_"+driverId);
        return jrm.getJson();
    }

    @Override
    public List<ErgastDriver> generateAllErgastDrivers() {
        List<ErgastDriver> allDrivers = getAllErgastDrivers();
        allDrivers.sort((o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.getFamilyName(), o2.getFamilyName()));
        JsonRepositoryModel saveData = JsonRepositoryModel.builder().id("ALL_ERGAST_DRIVERS")
                .json(allDrivers).build();
        jsonRepository.save(saveData);
        return allDrivers;
    }

    private List<JsonRepositoryModel> generateChartsDriverStandingsByRound(List<DriverStandingByRound> standingsBySeason) {
        Map<String, ChartSeries> totalPoints = new TreeMap<>();
        Map<String, ChartSeries> roundPoints = new TreeMap<>();
        Map<String, ChartSeries> roundResults = new TreeMap<>();
        Map<String, ChartSeries> gridToResultChartIncludingDnf = new TreeMap<>();
        Map<String, ChartSeries> gridToResultChartWithoutDnf = new TreeMap<>();
        standingsBySeason.forEach(standing -> {
            if (!totalPoints.containsKey(standing.getId().getId())) {
                String driverName = null;
                if(standing.getCode()!=null){
                    driverName = standing.getCode();
                } else {
                    driverName = standing.getId().getId();
                }
                totalPoints.put(standing.getId().getId(), ChartSeries.builder()
                        .name(driverName)
                        .color(standing.getColor())
                        .series(new ArrayList<>()).build());
                roundPoints.put(standing.getId().getId(), ChartSeries.builder()
                        .name(driverName)
                        .color(standing.getColor())
                        .series(new ArrayList<>()).build());
                roundResults.put(standing.getId().getId(), ChartSeries.builder()
                        .name(driverName)
                        .color(standing.getColor())
                        .series(new ArrayList<>()).build());
                gridToResultChartIncludingDnf.put(standing.getId().getId(), ChartSeries.builder()
                        .name(driverName)
                        .color(standing.getColor())
                        .series(new ArrayList<>())
                        .series2(new ArrayList<>()).build());
                gridToResultChartWithoutDnf.put(standing.getId().getId(), ChartSeries.builder()
                        .name(driverName)
                        .color(standing.getColor())
                        .series(new ArrayList<>())
                        .series2(new ArrayList<>()).build());
            }
            totalPoints.get(standing.getId().getId()).add(standing.getId().getRound(), standing.getPoints());
            roundPoints.get(standing.getId().getId()).add(standing.getId().getRound(), standing.getPointsThisRound());
            if (standing.getResultThisRound() != null) {
                roundResults.get(standing.getId().getId()).add(standing.getId().getRound(), new BigDecimal(standing.getResultThisRound()));
            }

            if (standing.getResultThisRound() != null) {
                gridToResultChartIncludingDnf.get(standing.getId().getId()).add2(standing.getGrid(), standing.getResultThisRoundDnf());
                if (standing.getResultThisRound() < this.roundDriverCount.get(standing.getId().getRound())) {
                    gridToResultChartWithoutDnf.get(standing.getId().getId()).add2(standing.getGrid(), standing.getResultThisRoundDnf());
                }
            }
        });
        List<ChartSeries> gridToResultChartIncludingDnfList = new ArrayList<>(gridToResultChartIncludingDnf.values());
        List<ChartSeries> gridToResultChartWithoutDnfList = new ArrayList<>(gridToResultChartWithoutDnf.values());
        for (ChartSeries serie : gridToResultChartIncludingDnfList) {
            serie.calcSeries2Averages();
        }
        for (ChartSeries serie : gridToResultChartWithoutDnfList) {
            serie.calcSeries2Averages();
        }

        List<JsonRepositoryModel> output = new ArrayList<>();
        List<ChartSeries> totalPointsChartSeries = new ArrayList<>(totalPoints.values());
        totalPointsChartSeries.forEach(serie -> serie.getSeries().sort(Comparator.comparing(n -> n.get(0))));

        List<ChartSeries> roundPointsChartSeries = new ArrayList<>(roundPoints.values());
        roundPointsChartSeries.forEach(serie -> serie.getSeries().sort(Comparator.comparing(n -> n.get(0))));

        List<ChartSeries> roundResultsChartSeries = new ArrayList<>(roundResults.values());
        roundResultsChartSeries.forEach(serie -> serie.getSeries().sort(Comparator.comparing(n -> n.get(0))));

        JsonRepositoryModel data1 = JsonRepositoryModel.builder().id("DRIVERS_TOTAL_POINTS")
                .json(totalPointsChartSeries).build();
        JsonRepositoryModel data2 = JsonRepositoryModel.builder().id("DRIVERS_ROUND_POINTS")
                .json(roundPointsChartSeries).build();
        JsonRepositoryModel data3 = JsonRepositoryModel.builder().id("DRIVERS_ROUND_RESULTS")
                .json(roundResultsChartSeries).build();
        JsonRepositoryModel data4 = JsonRepositoryModel.builder().id("GRID_TO_RESULT_WITH_DNF")
                .json(gridToResultChartIncludingDnfList).build();
        JsonRepositoryModel data5 = JsonRepositoryModel.builder().id("GRID_TO_RESULT_WITHOUT_DNF")
                .json(gridToResultChartWithoutDnfList).build();
        output.add(data1);
        output.add(data2);
        output.add(data3);
        output.add(data4);
        output.add(data5);
        return output;
    }

    private List<JsonRepositoryModel> generateChartsConstructorStandingsByRound(List<ConstructorStandingByRound> standingsBySeason) {
        Map<String, ChartSeries> totalPoints = new TreeMap<>();
        Map<String, ChartSeries> roundPoints = new TreeMap<>();
        standingsBySeason.forEach(standing -> {
            if (!totalPoints.containsKey(standing.getId().getId())) {
                totalPoints.put(standing.getId().getId(), ChartSeries.builder()
                        .name(standing.getName())
                        .color(standing.getColor())
                        .series(new ArrayList<>()).build());
                roundPoints.put(standing.getId().getId(), ChartSeries.builder()
                        .name(standing.getName())
                        .color(standing.getColor())
                        .series(new ArrayList<>()).build());
            }
            roundPoints.get(standing.getId().getId()).add(standing.getId().getRound(), standing.getPointsThisRound());
            totalPoints.get(standing.getId().getId()).add(standing.getId().getRound(), standing.getPoints());
        });

        List<JsonRepositoryModel> output = new ArrayList<>();
        List<ChartSeries> totalPointsChartSeries = new ArrayList<>(totalPoints.values());
        totalPointsChartSeries.forEach(serie -> serie.getSeries().sort(Comparator.comparing(n -> n.get(0))));
        List<ChartSeries> roundPointsChartSeries = new ArrayList<>(roundPoints.values());
        roundPointsChartSeries.forEach(serie -> serie.getSeries().sort(Comparator.comparing(n -> n.get(0))));

        JsonRepositoryModel data1 = JsonRepositoryModel.builder().id("CONSTRUCTOR_TOTAL_POINTS")
                .json(totalPointsChartSeries).build();
        JsonRepositoryModel data2 = JsonRepositoryModel.builder().id("CONSTRUCTOR_ROUND_POINTS")
                .json(roundPointsChartSeries).build();
        output.add(data1);
        output.add(data2);
        return output;
    }

    private void generateDriverStandingByRound(Integer season, Map<String, DriverStandingByRound> driverStandingsByRound,
                                               Map<String, ConstructorStandingByRound> constructorStandingByRound, List<FrontendRace> races) {
        Logger.log("generateDriverStandingByRound");
        boolean iterate;
        Integer round = 1;
        Integer maxPosition;
        do {

            ErgastResponse response = getDriverStandingsByRound(season, round);
            if (response.getMrData().getTotal() > 0) {
                Integer finalRound = round;
                response.getMrData().getStandingsTable().getStandingsLists().get(0).getDriverStandings()
                        .forEach(ergastStanding -> {
                            driverStandingsByRound.put(ergastStanding.getDriver().getDriverId() + finalRound, new DriverStandingByRound(ergastStanding, season, finalRound));
                        });
                round++;
                iterate = true;
            } else {
                iterate = false;
            }
        } while (iterate);
        round = 1;

        do {
            ErgastResponse response = getConstructorStandingsByRound(season, round);
            if (response.getMrData().getTotal() > 0) {
                Integer finalRound = round;
                response.getMrData().getStandingsTable().getStandingsLists().get(0).getConstructorStandings()
                        .forEach(ergastStanding -> {
                            constructorStandingByRound.put(ergastStanding.getConstructor().getConstructorId() + finalRound, new ConstructorStandingByRound(ergastStanding, season, finalRound));
                        });
                round++;
                iterate = true;
            } else {
                iterate = false;
            }
        } while (iterate);
        round = 1;
        do {
            ErgastResponse response = getResultsByRound(season, round);
            if (response.getMrData().getTotal() > 0) {
                Integer finalRound = round;
                maxPosition = response.getMrData().getRaceTable().getRaces().get(0).getResults().size();
                this.roundDriverCount.put(round, maxPosition+1);
                Integer finalMaxPosition = maxPosition;
                races.add(new FrontendRace(response.getMrData().getRaceTable().getRaces().get(0)));
                response.getMrData().getRaceTable().getRaces().get(0).getResults()
                        .forEach(ergastStanding -> {
                            if(!driverStandingsByRound.containsKey(ergastStanding.getDriver().getDriverId() + finalRound)){
                                driverStandingsByRound.put(ergastStanding.getDriver().getDriverId() + finalRound, new DriverStandingByRound(ergastStanding, season, finalRound, false));
                            }
                            driverStandingsByRound.get(ergastStanding.getDriver().getDriverId() + finalRound).setDataFromARound(ergastStanding, finalMaxPosition);
                            if(constructorStandingByRound.size()>0) {
                                if(!constructorStandingByRound.containsKey(ergastStanding.getConstructor().getConstructorId() + finalRound)){
                                    Logger.log("CONSTRUCTOR NE POSTOJI", ergastStanding.getConstructor().getConstructorId() + finalRound);
                                    constructorStandingByRound.put(ergastStanding.getConstructor().getConstructorId() + finalRound, new ConstructorStandingByRound(ergastStanding, season, finalRound, false));
                                }
                                constructorStandingByRound.get(ergastStanding.getConstructor().getConstructorId() + finalRound).incrementPointsThisRound(ergastStanding.getPoints());
                            }
                            });
                round++;
                iterate = true;
            } else {
                iterate = false;
            }
        } while (iterate);
    }

    private List<ConstructorStanding> getConstructorStandingsByYear(Integer season) {
        List<ConstructorStanding> constructorStandings = new ArrayList<>();
        ErgastResponse response =  restTemplate
                .getForObject(ERGAST_URL + season + "/constructorStandings.json", ErgastResponse.class);
        if(response.getMrData().getStandingsTable().getStandingsLists().size()>0) {
            response.getMrData().getStandingsTable().getStandingsLists().get(0).getConstructorStandings()
                    .forEach(ergastStanding -> {
                        constructorStandings.add(new ConstructorStanding(ergastStanding));
                    });
        }
        return constructorStandings;
    }

    private List<DriverStanding> getDriverStandingsByYear(Integer season) {
        List<DriverStanding> driverStandings = new ArrayList<>();
        ErgastResponse response = restTemplate
                .getForObject(ERGAST_URL + season + "/driverStandings.json?limit=100", ErgastResponse.class);
        response.getMrData().getStandingsTable().getStandingsLists().get(0).getDriverStandings()
                .forEach(ergastStanding -> {
                    driverStandings.add(new DriverStanding(ergastStanding));
                     });
        return driverStandings;
    }

    private List<ErgastStanding> getErgastStandingsByYear(Integer season) {
         ErgastResponse response = restTemplate
                .getForObject(ERGAST_URL + season + "/driverStandings.json?limit=100", ErgastResponse.class);
        return response.getMrData().getStandingsTable().getStandingsLists().get(0)
                .getDriverStandings();
    }
}
