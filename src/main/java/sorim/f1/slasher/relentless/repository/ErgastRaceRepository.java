package sorim.f1.slasher.relentless.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sorim.f1.slasher.relentless.entities.ergast.Race;
import sorim.f1.slasher.relentless.model.ExposureRace;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
@EnableJpaAuditing
public interface ErgastRaceRepository extends CrudRepository<Race, String> {

    @NotNull
    List<Race> findAll();

    Race findFirstByRaceAnalysisNotNullOrderByDateDesc();

    Race findFirstByRaceAnalysisIsNullAndSeasonOrderByDateAsc(String year);

    List<Race> findByCircuitIdOrderBySeasonDesc(String circuitId);

    List<ExposureRace> findAllBySeasonAndRoundLessThanEqualOrderByRoundAsc(String season, Integer round);
}
