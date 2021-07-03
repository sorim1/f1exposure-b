package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.entities.DriverStanding;
import sorim.f1.slasher.relentless.entities.ergast.Race;
import sorim.f1.slasher.relentless.model.ergast.ErgastResponse;

import java.util.List;

public interface ErgastService {

    List<Race> fetchCurrentSeason();

    List<Race> fetchSeason(String year);

    List<Race> getAllRaces();

    void saveRaces(List<Race> races);

    Race fetchLatestRace();

    List<Race> findByCircuitId(String circuitId);

    ErgastResponse getDriverStandings();

    ErgastResponse getConstructorStandings();
}
