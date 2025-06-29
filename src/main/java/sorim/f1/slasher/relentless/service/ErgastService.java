package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.entities.Driver;
import sorim.f1.slasher.relentless.entities.JsonRepositoryModel;
import sorim.f1.slasher.relentless.entities.ergast.RaceData;
import sorim.f1.slasher.relentless.model.DriverComparator;
import sorim.f1.slasher.relentless.model.DriverCompared;
import sorim.f1.slasher.relentless.model.FrontendRace;
import sorim.f1.slasher.relentless.model.ergast.ErgastDriver;
import sorim.f1.slasher.relentless.model.ergast.ErgastResponse;

import java.util.List;
import java.util.Map;

public interface ErgastService {

    List<RaceData> fetchCurrentSeason();

    List<RaceData> fetchSeason(String year);

    void saveRace(RaceData raceData);

    void saveRaces(List<RaceData> raceData);

    RaceData getLatestAnalyzedRace();

    RaceData getUpcomingRace(Integer currentYear);

    RaceData getLatestNonAnalyzedRace(Integer currentYear);

    RaceData findRaceBySeasonAndRound(String season, Integer round);

    RaceData findById(Integer id);

    RaceData findByCircuitIdOrderBySeasonDesc(String circuitId);

    List<RaceData> findRacesBySeason(String season);

    ErgastResponse getCurrentDriverStandings();

    ErgastResponse getConstructorStandings();

    ErgastResponse getDriverStandingsByRound(Integer season, Integer round);

    ErgastResponse getConstructorStandingsByRound(Integer season, Integer round);

    ErgastResponse getResultsByRound(Integer season, Integer round);

    ErgastResponse getSprintResultsByRound(Integer season, Integer round);

    List<FrontendRace> getRacesSoFar(String season, Integer round);

    List<FrontendRace> getRacesOfSeason(String season);

    Map<String, String> connectDriverCodesWithErgastCodes();

    ErgastResponse getRaceLaps(Integer season, Integer round);

    ErgastResponse getRacePitStops(Integer season, Integer round);

    void deleteRaces(String season);

    JsonRepositoryModel fetchHistoricSeason(Integer season);

    Boolean fetchHistoricSeasonFull() throws InterruptedException;

    Boolean fetchStatistics(Boolean partial);

    Boolean fetchStatisticsFullFromPartial(Boolean forceFetch);

    Object getErgastDrivers();

    Object getHistoricSeason(Integer season);

    Object getDriverStatistics(String driverId);

    List<ErgastDriver> generateAllErgastDrivers();

    Object getCircuitStatistics(String circuitId);

    Object getErgastRace(Integer season, Integer round);

    Object getAllCircuits();

    Object getErgastConstructors();

    Object getConstructorStatistics(String constructorId);

    DriverComparator compareDrivers(DriverComparator body);

    List<DriverCompared> getCompareDriversDropdown(Integer season);

    List<Driver>  fetchCurrentDrivers();
}
