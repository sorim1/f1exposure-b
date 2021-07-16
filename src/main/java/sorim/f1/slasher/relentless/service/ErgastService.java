package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.entities.DriverStanding;
import sorim.f1.slasher.relentless.entities.ergast.Race;
import sorim.f1.slasher.relentless.model.ergast.ErgastResponse;

import java.util.List;

public interface ErgastService {

    List<Race> fetchCurrentSeason();

    List<Race> fetchSeason(String year);

    void saveRace(Race race);

    void saveRaces(List<Race> races);

    Race getLatestAnalyzedRace();
    Race getLatestNonAnalyzedRace();


    List<Race> findByCircuitIdOrderBySeasonDesc(String circuitId);

    ErgastResponse getDriverStandings();

    ErgastResponse getConstructorStandings();

    ErgastResponse getDriverStandingsByRound(Integer season, Integer round);

    ErgastResponse getConstructorStandingsByRound(Integer season, Integer round);

    ErgastResponse getResultsByRound(Integer season, Integer round);
}
