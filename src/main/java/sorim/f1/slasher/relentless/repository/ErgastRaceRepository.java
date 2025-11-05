package sorim.f1.slasher.relentless.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sorim.f1.slasher.relentless.entities.ergast.RaceData;
import sorim.f1.slasher.relentless.model.FrontendRace;

import java.util.List;

@Repository
@Transactional
public interface ErgastRaceRepository extends CrudRepository<RaceData, String> {

    @NotNull
    List<RaceData> findAll();

    RaceData findFirstByRaceAnalysisNotNullOrderByDateDesc();

    RaceData findFirstByRaceAnalysisNullAndSeasonOrderByDateAsc(String year);

    RaceData findFirstByRaceAnalysisIsNullAndSeasonOrderByDateAsc(String year);

    RaceData findFirstByRaceAnalysisIsNotNullAndLiveTimingRaceIsNullAndSeasonOrderByDateAsc(String year);

    RaceData findFirstByCircuitIdOrderBySeasonDesc(String circuitId);

    List<FrontendRace> findAllBySeasonAndRoundLessThanEqualOrderByRoundAsc(String season, Integer round);

    List<FrontendRace> findAllBySeasonOrderByRoundAsc(String season);

    List<RaceData> findAllBySeason(String Season);

    RaceData findAllBySeasonAndRound(String Season, Integer round);

    RaceData findAllById(Integer id);

    void deleteAllBySeason(String season);
}
