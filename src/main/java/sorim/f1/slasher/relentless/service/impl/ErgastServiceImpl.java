package sorim.f1.slasher.relentless.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sorim.f1.slasher.relentless.entities.*;
import sorim.f1.slasher.relentless.entities.ergast.RaceData;
import sorim.f1.slasher.relentless.handling.Logger;
import sorim.f1.slasher.relentless.model.AllStandings;
import sorim.f1.slasher.relentless.model.ChartSeries;
import sorim.f1.slasher.relentless.model.FrontendRace;
import sorim.f1.slasher.relentless.model.ergast.ErgastResponse;
import sorim.f1.slasher.relentless.model.livetiming.LapTimeData;
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

    private final ErgastRaceRepository ergastRaceRepository;
    private final DriverRepository driverRepository;
    private final JsonRepository jsonRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private static Map<Integer, Integer> roundDriverCount = new HashMap<>();

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
    public ErgastResponse getDriverStandings() {
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
                .getForObject(ERGAST_URL + season + "/" + round + "/results.json", ErgastResponse.class);
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
        Integer roundCount = null;
        List<DriverStanding> driverStandings = getDriverStandingsByYear(season, roundCount);
        log.info("roundCount: {}", roundCount);
        Map<String, DriverStandingByRound> driverStandingsByRound = new HashMap<>();
        Map<String, ConstructorStandingByRound> constructorStandingByRound = new HashMap<>();
        List<FrontendRace> races = new ArrayList<>();
        generateDriverStandingByRound(season, driverStandingsByRound, constructorStandingByRound, races);
        List<DriverStandingByRound> driverStandingsByRoundList = new ArrayList<>(driverStandingsByRound.values());
        List<ConstructorStandingByRound> constructorStandingByRoundList = new ArrayList<>(constructorStandingByRound.values());
        List<JsonRepositoryModel> driverCharts = generateChartsDriverStandingsByRound(driverStandingsByRoundList);
        List<JsonRepositoryModel> constructorCharts = generateChartsConstructorStandingsByRound(constructorStandingByRoundList);
        AllStandings championship = AllStandings.builder()
                .driverStandings(driverStandings)
                .constructorStandings(getConstructorStandingsByYear(season))
                .driverStandingByRound((List<ChartSeries>) driverCharts.get(0).getJson())
                .driverPointsByRound((List<ChartSeries>) driverCharts.get(1).getJson())
                .driverResultByRound((List<ChartSeries>) driverCharts.get(2).getJson())
                .gridToResultChartWithDnf((List<ChartSeries>) driverCharts.get(3).getJson())
                .gridToResultChartWithoutDnf((List<ChartSeries>) driverCharts.get(4).getJson())
                .constructorStandingByRound((List<ChartSeries>) constructorCharts.get(0).getJson())
                .constructorPointsByRound((List<ChartSeries>) constructorCharts.get(1).getJson())
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
        log.info("jrm1: {}", jrm.getJson().getClass().getName());
        return jrm.getJson();
    }

    private List<JsonRepositoryModel> generateChartsDriverStandingsByRound(List<DriverStandingByRound> standingsBySeason) {
        Map<String, ChartSeries> totalPoints = new TreeMap<>();
        Map<String, ChartSeries> roundPoints = new TreeMap<>();
        Map<String, ChartSeries> roundResults = new TreeMap<>();
        Map<String, ChartSeries> gridToResultChartIncludingDnf = new TreeMap<>();
        Map<String, ChartSeries> gridToResultChartWithoutDnf = new TreeMap<>();
        standingsBySeason.forEach(standing -> {
            if (!totalPoints.containsKey(standing.getCode())) {
                totalPoints.put(standing.getCode(), ChartSeries.builder()
                        .name(standing.getCode())
                        .color(standing.getColor())
                        .series(new ArrayList<>()).build());
                roundPoints.put(standing.getCode(), ChartSeries.builder()
                        .name(standing.getCode())
                        .color(standing.getColor())
                        .series(new ArrayList<>()).build());
                roundResults.put(standing.getCode(), ChartSeries.builder()
                        .name(standing.getCode())
                        .color(standing.getColor())
                        .series(new ArrayList<>()).build());
                gridToResultChartIncludingDnf.put(standing.getCode(), ChartSeries.builder()
                        .name(standing.getCode())
                        .color(standing.getColor())
                        .series(new ArrayList<>())
                        .series2(new ArrayList<>()).build());
                gridToResultChartWithoutDnf.put(standing.getCode(), ChartSeries.builder()
                        .name(standing.getCode())
                        .color(standing.getColor())
                        .series(new ArrayList<>())
                        .series2(new ArrayList<>()).build());
            }
            totalPoints.get(standing.getCode()).add(standing.getId().getRound(), standing.getPoints());
            roundPoints.get(standing.getCode()).add(standing.getId().getRound(), standing.getPointsThisRound());
            if (standing.getResultThisRound() != null) {
                roundResults.get(standing.getCode()).add(standing.getId().getRound(), new BigDecimal(standing.getResultThisRound()));
            }

            if (standing.getResultThisRound() != null) {
                gridToResultChartIncludingDnf.get(standing.getCode()).add2(standing.getGrid(), standing.getResultThisRoundDnf());
                if (standing.getResultThisRound() < this.roundDriverCount.get(standing.getId().getRound())) {
                    gridToResultChartWithoutDnf.get(standing.getCode()).add2(standing.getGrid(), standing.getResultThisRoundDnf());
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
                            driverStandingsByRound.get(ergastStanding.getDriver().getDriverId() + finalRound).setDataFromARound(ergastStanding, finalMaxPosition);
                            constructorStandingByRound.get(ergastStanding.getConstructor().getConstructorId() + finalRound).incrementPointsThisRound(ergastStanding.getPoints());
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
        response.getMrData().getStandingsTable().getStandingsLists().get(0).getConstructorStandings()
                .forEach(ergastStanding -> {
                    constructorStandings.add(new ConstructorStanding(ergastStanding));
                                   });
        return constructorStandings;
    }

    private List<DriverStanding> getDriverStandingsByYear(Integer season, Integer roundCount) {
        List<DriverStanding> driverStandings = new ArrayList<>();
        ErgastResponse response = restTemplate
                .getForObject(ERGAST_URL + season + "/driverStandings.json", ErgastResponse.class);
        roundCount = response.getMrData().getStandingsTable().getStandingsLists().get(0).getRound();
        response.getMrData().getStandingsTable().getStandingsLists().get(0).getDriverStandings()
                .forEach(ergastStanding -> {
                    driverStandings.add(new DriverStanding(ergastStanding));
                     });

        return driverStandings;
    }
}
