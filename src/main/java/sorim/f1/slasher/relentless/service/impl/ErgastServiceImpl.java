package sorim.f1.slasher.relentless.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sorim.f1.slasher.relentless.entities.ergast.Race;
import sorim.f1.slasher.relentless.model.FrontendRace;
import sorim.f1.slasher.relentless.model.ergast.ErgastResponse;
import sorim.f1.slasher.relentless.repository.ErgastRaceRepository;
import sorim.f1.slasher.relentless.service.ErgastService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ErgastServiceImpl implements ErgastService {
    private final static String CURRENT_SEASON = "http://ergast.com/api/f1/current.json";
    private final static String GET_SEASON = "https://ergast.com/api/f1/{year}.json";
    private final static String ERGAST_URL = "https://ergast.com/api/f1/";

    private final ErgastRaceRepository ergastRaceRepository;
    private RestTemplate restTemplate = new RestTemplate();


    @Override
    public List<Race> fetchSeason(String year) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("year", year);

        ErgastResponse ergastResponse = restTemplate
                .getForObject(GET_SEASON, ErgastResponse.class, uriVariables);
        return ergastResponse.getMrData().getRaceTable().getRaces();
    }

    @Override
    public void saveRace(Race race) {
        ergastRaceRepository.save(race);
    }

    @Override
    public List<Race> fetchCurrentSeason() {
        RestTemplate restTemplate = new RestTemplate();
        ErgastResponse ergastResponse = restTemplate
                .getForObject(CURRENT_SEASON, ErgastResponse.class);
        return ergastResponse.getMrData().getRaceTable().getRaces();
    }

    @Override
    public void saveRaces(List<Race> races) {
        ergastRaceRepository.saveAll(races);
    }

    @Override
    public Race getLatestAnalyzedRace() {
        return ergastRaceRepository.findFirstByRaceAnalysisNotNullOrderByDateDesc();
    }

    @Override
    public Race getLatestNonAnalyzedRace(Integer currentYear) {
        return ergastRaceRepository.findFirstByRaceAnalysisIsNullAndSeasonOrderByDateAsc(String.valueOf(currentYear));
    }

    @Override
    public List<Race> findByCircuitIdOrderBySeasonDesc(String circuitId) {
        return ergastRaceRepository.findByCircuitIdOrderBySeasonDesc(circuitId);
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
                .getForObject(ERGAST_URL + season+ "/" + round+ "/driverStandings.json", ErgastResponse.class);
    }

    @Override
    public ErgastResponse getConstructorStandingsByRound(Integer season, Integer round) {
        return restTemplate
                .getForObject(ERGAST_URL + season+ "/" + round+ "/constructorStandings.json", ErgastResponse.class);
    }

    @Override
    public ErgastResponse getResultsByRound(Integer season, Integer round) {
        return restTemplate
                .getForObject(ERGAST_URL + season+ "/" + round+ "/results.json", ErgastResponse.class);
    }

    @Override
    public List<FrontendRace> getRacesSoFar(String season, Integer round) {
        return ergastRaceRepository.findAllBySeasonAndRoundLessThanEqualOrderByRoundAsc(season, round);
    }

    @Override
    public List<FrontendRace> getRacesOfSeason(String season) {
        return ergastRaceRepository.findAllBySeasonOrderByRoundAsc(season);
    }
}
