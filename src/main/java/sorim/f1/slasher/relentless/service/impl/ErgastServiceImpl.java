package sorim.f1.slasher.relentless.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sorim.f1.slasher.relentless.entities.ExposureDriver;
import sorim.f1.slasher.relentless.entities.ergast.RaceData;
import sorim.f1.slasher.relentless.model.FrontendRace;
import sorim.f1.slasher.relentless.model.ergast.ErgastResponse;
import sorim.f1.slasher.relentless.repository.DriverRepository;
import sorim.f1.slasher.relentless.repository.ErgastRaceRepository;
import sorim.f1.slasher.relentless.service.ErgastService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final RestTemplate restTemplate = new RestTemplate();


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
        RaceData response =ergastRaceRepository.findFirstByRaceAnalysisNullAndSeasonOrderByDateAsc(currentYear.toString());
        if(response==null){
            Integer nextYear = currentYear + 1;
            response =ergastRaceRepository.findFirstByRaceAnalysisIsNullAndSeasonOrderByDateAsc(nextYear.toString());
        }
        return response;
    }

    @Override
    public RaceData getLatestNonAnalyzedRace(Integer currentYear) {
        RaceData response = ergastRaceRepository.findFirstByRaceAnalysisIsNotNullAndLiveTimingRaceIsNullAndSeasonOrderByDateAsc(String.valueOf(currentYear));
        if(response!=null){
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
    public void deleteRaces(String season) {
        ergastRaceRepository.deleteAllBySeason(season);
    }
}
