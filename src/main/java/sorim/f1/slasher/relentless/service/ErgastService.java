package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.entities.ergast.Race;

import java.util.List;

public interface ErgastService {

    List<Race> fetchCurrentSeason();

    List<Race> getAllRaces();

    void saveRaces(List<Race> races);

    Race fetchSingleRace();
}
