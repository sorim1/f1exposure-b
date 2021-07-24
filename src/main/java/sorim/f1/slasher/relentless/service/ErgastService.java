package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.entities.ergast.Race;
import sorim.f1.slasher.relentless.model.FrontendRace;
import sorim.f1.slasher.relentless.model.ergast.ErgastResponse;

import java.util.List;

public interface ErgastService {

    List<Race> fetchCurrentSeason();

    List<Race> fetchSeason(String year);

    void saveRace(Race race);

    void saveRaces(List<Race> races);

    Race getLatestAnalyzedRace();
    Race getLatestNonAnalyzedRace(Integer currentYear);


    List<Race> findByCircuitIdOrderBySeasonDesc(String circuitId);

    ErgastResponse getDriverStandings();

    ErgastResponse getConstructorStandings();

    ErgastResponse getDriverStandingsByRound(Integer season, Integer round);

    ErgastResponse getConstructorStandingsByRound(Integer season, Integer round);

    ErgastResponse getResultsByRound(Integer season, Integer round);

    List<FrontendRace> getRacesSoFar(String season, Integer round);
    List<FrontendRace> getRacesOfSeason(String season);
}
