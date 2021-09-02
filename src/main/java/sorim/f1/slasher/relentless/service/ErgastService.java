package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.entities.ergast.RaceData;
import sorim.f1.slasher.relentless.model.FrontendRace;
import sorim.f1.slasher.relentless.model.ergast.ErgastResponse;

import java.util.List;
import java.util.Map;

public interface ErgastService {

    List<RaceData> fetchCurrentSeason();

    List<RaceData> fetchSeason(String year);

    void saveRace(RaceData raceData);

    void saveRaces(List<RaceData> raceData);

    RaceData getLatestAnalyzedRace();
    RaceData getLatestNonAnalyzedRace(Integer currentYear);


    List<RaceData> findByCircuitIdOrderBySeasonDesc(String circuitId);

    ErgastResponse getDriverStandings();

    ErgastResponse getConstructorStandings();

    ErgastResponse getDriverStandingsByRound(Integer season, Integer round);

    ErgastResponse getConstructorStandingsByRound(Integer season, Integer round);

    ErgastResponse getResultsByRound(Integer season, Integer round);

    List<FrontendRace> getRacesSoFar(String season, Integer round);
    List<FrontendRace> getRacesOfSeason(String season);

    Map<String, String> connectDriverCodesWithErgastCodes();

    ErgastResponse getRaceLaps(Integer season, Integer round);
}
