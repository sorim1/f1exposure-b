package sorim.f1.slasher.relentless.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sorim.f1.slasher.relentless.entities.ergast.Race;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
@EnableJpaAuditing
public interface ErgastRaceRepository extends CrudRepository<Race, String> {

    @NotNull
    List<Race> findAll();

    Race findFirstByRaceAnalysisNotNullOrderByDateDesc();

    Race findFirstByRaceAnalysisIsNullOrderByDateAsc();


    List<Race> findByCircuitIdOrderByIdDesc(String circuitId);
}
