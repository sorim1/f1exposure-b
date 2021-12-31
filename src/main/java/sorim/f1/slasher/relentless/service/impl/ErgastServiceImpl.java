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
import sorim.f1.slasher.relentless.model.ergast.*;
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
                .getForObject(ERGAST_URL + "current/driverStandings.json?limit=100", ErgastResponse.class);
    }

    @Override
    public ErgastResponse getConstructorStandings() {
        return restTemplate
                .getForObject(ERGAST_URL + "current/constructorStandings.json?limit=100", ErgastResponse.class);
    }

    @Override
    public ErgastResponse getDriverStandingsByRound(Integer season, Integer round) {
        return restTemplate
                .getForObject(ERGAST_URL + season + "/" + round + "/driverStandings.json?limit=100", ErgastResponse.class);
    }

    @Override
    public ErgastResponse getConstructorStandingsByRound(Integer season, Integer round) {
        return restTemplate
                .getForObject(ERGAST_URL + season + "/" + round + "/constructorStandings.json?limit=100", ErgastResponse.class);
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

    private List<ErgastConstructor> getAllErgastConstructors() {
        ErgastResponse response = restTemplate
                .getForObject(ERGAST_URL + "constructors.json?limit=500", ErgastResponse.class);
        return response.getMrData().getConstructorTable().getConstructors();
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
    public JsonRepositoryModel fetchHistoricSeason(Integer season) {
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
        String name1 = season +"_Formula_One_season";
        String name2 = season +"_Formula_One_World_Championship";
        List<String> list = generateWikiProperties(name1, name2);
        if(!list.isEmpty()){
            championship.setWikiSummary(list.get(0));
        }
        if(list.size()>1){
            championship.setWikiImage(list.get(1));
        }

        return JsonRepositoryModel.builder().id("CHAMPIONSHIP_" + season)
                .json(championship).build();
    }

    @Override
    public Boolean fetchHistoricSeasonFull() throws InterruptedException {
        List<JsonRepositoryModel> saveData = new ArrayList<>();
        for(int i = 1950; i<=properties.getCurrentSeasonPast(); i++){
            log.info("fetch year: {}", i );
            JsonRepositoryModel season = fetchHistoricSeason(i);
            saveData.add(season);
            Thread.sleep(1000);
        }
        jsonRepository.saveAll(saveData);
        return true;
    }

    @Override
    public Boolean fetchStatistics() {
        int firstSeason = 1950;
        List<ErgastDriver> allDrivers = generateAllErgastDrivers();
        List<ErgastConstructor> allConstructors = generateAllErgastConstructors();

        Map<String, DriverStatistics> driversMap = new HashMap<>();
        allDrivers.forEach(ergastDriver -> driversMap.put(ergastDriver.getDriverId(), new DriverStatistics(ergastDriver)));
        Map<String, ConstructorStatistics> constructorsMap = new HashMap<>();
        allConstructors.forEach(ergastConstructor -> constructorsMap.put(ergastConstructor.getConstructorId(), new ConstructorStatistics(ergastConstructor)));

        for(int season = firstSeason; season<=properties.getCurrentSeasonPast(); season++){
           List<ErgastStanding> list = getErgastStandingsByYear(season);
            int finalSeason = season;
            list.forEach(es->{
                driversMap.get(es.getDriver().getDriverId()).pushSeasonStanding(finalSeason, es);
            });
            constructorsMap.get( list.get(0).getConstructors().get(list.get(0).getConstructors().size()-1).getConstructorId()).addWdc();
            List<ErgastStanding> list2 = getErgastConstructorStandingsByYear(season);
            list2.forEach(es->{
                constructorsMap.get(es.getConstructor().getConstructorId()).pushSeasonStanding(finalSeason, es);
            });

            log.info("seasonA over: {}", season);
        }

        ErgastResponse response = getCurrentDriverStandings();
        response.getMrData().getStandingsTable().getStandingsLists().get(0)
                .getDriverStandings().forEach(es->{
                    driversMap.get(es.getDriver().getDriverId()).setCurrentConstructor(es.getConstructors().get(0));
                });
        Map<String, CircuitStatistics> circuitsMap = generateAllCircuits();

        List<RaceData> races = new ArrayList<>();
        for(int season = firstSeason; season<=properties.getCurrentSeasonPast(); season++){
            updateDriverWithRaceByRaceData(season, driversMap, constructorsMap, circuitsMap, races);
            try {
                Thread.sleep(1000);
                log.info("seasonB over: {}", season);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        saveAll(driversMap, constructorsMap, circuitsMap, races);
        return true;
    }

    private void updateDriverWithRaceByRaceData(int season, Map<String, DriverStatistics> driversMap, Map<String, ConstructorStatistics> constructorsMap,
                                                Map<String, CircuitStatistics> circuitsMap, List<RaceData> races) {
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
                response = getConstructorStandingsByRound(season, round);
                if (response.getMrData().getTotal() > 0) {
                    response.getMrData().getStandingsTable().getStandingsLists().get(0).getConstructorStandings()
                            .forEach(es -> {
                                constructorsMap.get(es.getConstructor().getConstructorId()).addPointThroughSeason(season, finalRound, es);
                            });
                }

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
                Integer finalRound1 = round;
                response.getMrData().getRaceTable().getRaces().get(0).getResults()
                        .forEach(es -> {
                            driversMap.get(es.getDriver().getDriverId()).incrementRaceCounts(es);
                            constructorsMap.get(es.getConstructor().getConstructorId()).incrementRaceCounts(es, season+"-"+ finalRound1);
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
    }

    private void saveAll(Map<String, DriverStatistics> driversMap, Map<String, ConstructorStatistics> constructorsMap, Map<String, CircuitStatistics> circuitsMap, List<RaceData> races) {
        List<JsonRepositoryModel> saveList = new ArrayList<>();
        driversMap.forEach((k,v)->{
            String driverId = v.getUrl().substring(v.getUrl().lastIndexOf("/")+1) ;
            String driverId2 = v.getGivenName() + "_" + v.getFamilyName() ;
            List<String> list = generateWikiProperties(driverId, driverId2);
            if(list.size()>0){
                v.setWikiSummary(list.get(0));
            }
            if(list.size()>1){
                v.setWikiImage(list.get(1));
            }
            JsonRepositoryModel driverJrm = JsonRepositoryModel.builder().id("DRIVER_"+k)
                    .json(v).build();
            saveList.add(driverJrm);
        });

        constructorsMap.forEach((k,v)->{
            String name1 =   v.getErgastConstructor().getUrl().substring( v.getErgastConstructor().getUrl().lastIndexOf("/")+1) ;
            String name2 =  v.getErgastConstructor().getName().replace(" ", "_") ;
            List<String> list = generateWikiProperties(name1, name2);
            if(!list.isEmpty()){
                v.setWikiSummary(list.get(0));
            }
            if(list.size()>1){
                v.setWikiImage(list.get(1));
            }
            JsonRepositoryModel driverJrm = JsonRepositoryModel.builder().id("CONSTRUCTOR_"+k)
                    .json(v).build();
            saveList.add(driverJrm);
        });

        circuitsMap.forEach((id,circuit) ->{
            String name1 = circuit.getErgastCircuit().getUrl().substring(circuit.getErgastCircuit().getUrl().lastIndexOf("/")+1) ;
            String name2 = circuit.getErgastCircuit().getCircuitName().replace(" ", "_") ;
            List<String> list = generateWikiProperties(name1, name2);
            if(!list.isEmpty()){
                circuit.setWikiSummary(list.get(0));
            }
            if(list.size()>1){
                circuit.setWikiImage(list.get(1));
            }
            JsonRepositoryModel entry = JsonRepositoryModel.builder().id("CIRCUIT_"+id)
                    .json(circuit).build();
            saveList.add(entry);
        });
        races.forEach((race) ->{
            String name1 = race.getUrl().substring(race.getUrl().lastIndexOf("/")+1) ;
            String name2 = race.getRaceName().replace(" ", "_") ;
            List<String> list = generateWikiProperties(name1, name2);
            if(!list.isEmpty()){
                race.setWikiSummary(list.get(0));
            }
            if(list.size()>1){
                race.setWikiImage(list.get(1));
            }
            JsonRepositoryModel entry = JsonRepositoryModel.builder().id("RACE_"+race.getSeason()+"_"+race.getRound())
                    .json(race).build();
            saveList.add(entry);
        });
        jsonRepository.saveAll(saveList);
    }

    private List<String> generateWikiProperties(String name, String fallbackName) {
            Map<String, Object> mapping;
        String name1 = name.replace("%28", "(").replace("%29", ")");
        List<String> responseList = new ArrayList<>();
        try {
                String response =  restTemplate
                        .getForObject(WIKIPEDIA_URL +name1, String.class);
                TypeReference<HashMap<String, Object>> typeRef = new TypeReference<>() {
                };
                mapping = mapper.readValue(response, typeRef);
            } catch (Exception e) {
                try {
                    String response =  restTemplate
                            .getForObject(WIKIPEDIA_URL +fallbackName, String.class);
                    TypeReference<HashMap<String, Object>> typeRef = new TypeReference<>() {
                    };
                    mapping = mapper.readValue(response, typeRef);
                } catch (Exception e3) {
                    log.error("ERROR3: {} - {}", name1, fallbackName);
                    e3.printStackTrace();
                    return responseList;
                }
            }
        String summary = (String) mapping.get("extract");
        responseList.add(summary);
        LinkedHashMap<String, Object> query = (LinkedHashMap<String, Object>) mapping.get("thumbnail");
        if(query!=null) {
            String thumbnail = (String) query.get("source");
            responseList.add(thumbnail);
        }
            return responseList;
        }

    private Map<String, CircuitStatistics> generateAllCircuits() {
        Map<String, CircuitStatistics> circuitsMap = new HashMap<>();
        List<Circuit> circuits = getAllErgastCircuits();
        JsonRepositoryModel saveData = JsonRepositoryModel.builder().id("ALL_CIRCUITS")
                .json(circuits).build();
        jsonRepository.save(saveData);
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
    public Object getErgastConstructors() {
        JsonRepositoryModel jrm = jsonRepository.findAllById("ALL_ERGAST_CONSTRUCTORS");
        return jrm.getJson();
    }

    @Override
    public Object getHistoricSeason(Integer season) {
        JsonRepositoryModel jrm = jsonRepository.findAllById("CHAMPIONSHIP_"+season);
        return jrm.getJson();
    }

    @Override
    public Object getDriverStatistics(String driverId) {
        JsonRepositoryModel jrm = jsonRepository.findAllById("DRIVER_"+driverId);
        return jrm.getJson();
    }

    @Override
    public Object getConstructorStatistics(String driverId) {
        JsonRepositoryModel jrm = jsonRepository.findAllById("CONSTRUCTOR_"+driverId);
        return jrm.getJson();
    }

    @Override
    public List<ErgastDriver> generateAllErgastDrivers() {
        List<ErgastDriver> allDrivers = getAllErgastDrivers();

        allDrivers.sort((o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o2.getDateOfBirth(), o1.getDateOfBirth()));
        JsonRepositoryModel saveData = JsonRepositoryModel.builder().id("ALL_ERGAST_DRIVERS")
                .json(allDrivers).build();
        jsonRepository.save(saveData);
        return allDrivers;
    }

    private List<ErgastConstructor> generateAllErgastConstructors() {
        List<ErgastConstructor> all = getAllErgastConstructors();
        all.sort((o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.getName(), o2.getName()));
        JsonRepositoryModel saveData = JsonRepositoryModel.builder().id("ALL_ERGAST_CONSTRUCTORS")
                .json(all).build();
        jsonRepository.save(saveData);
        return all;
    }

    @Override
    public Object getCircuitStatistics(String circuitId) {
        JsonRepositoryModel jrm = jsonRepository.findAllById("CIRCUIT_"+circuitId);
        return jrm.getJson();
    }

    @Override
    public Object getErgastRace(Integer season, Integer round) {
        JsonRepositoryModel jrm = jsonRepository.findAllById("RACE_"+season+"_"+round);
        return jrm.getJson();
    }

    @Override
    public Object getAllCircuits() {
        JsonRepositoryModel jrm = jsonRepository.findAllById("ALL_CIRCUITS");
        return jrm.getJson();
    }

    private List<JsonRepositoryModel> generateChartsDriverStandingsByRound(List<DriverStandingByRound> standingsBySeason) {
        Map<String, ChartSeries> totalPoints = new TreeMap<>();
        Map<String, ChartSeries> roundPoints = new TreeMap<>();
        Map<String, ChartSeries> roundResults = new TreeMap<>();
        Map<String, ChartSeries> gridToResultChartIncludingDnf = new TreeMap<>();
        Map<String, ChartSeries> gridToResultChartWithoutDnf = new TreeMap<>();
        standingsBySeason.forEach(standing -> {
            if (!totalPoints.containsKey(standing.getId().getId())) {
                String driverName;
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
                .getForObject(ERGAST_URL + season + "/constructorStandings.json?limit=100", ErgastResponse.class);
        if(response.getMrData().getStandingsTable().getStandingsLists().size()>0) {
            response.getMrData().getStandingsTable().getStandingsLists().get(0).getConstructorStandings()
                    .forEach(ergastStanding -> constructorStandings.add(new ConstructorStanding(ergastStanding)));
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

    private List<ErgastStanding> getErgastConstructorStandingsByYear(Integer season) {
        ErgastResponse response =  restTemplate
                .getForObject(ERGAST_URL + season + "/constructorStandings.json?limit=100", ErgastResponse.class);
        if(!response.getMrData().getStandingsTable().getStandingsLists().isEmpty()) {
            return response.getMrData().getStandingsTable().getStandingsLists().get(0)
                    .getConstructorStandings();
        }
        return new ArrayList<>();
    }
}
