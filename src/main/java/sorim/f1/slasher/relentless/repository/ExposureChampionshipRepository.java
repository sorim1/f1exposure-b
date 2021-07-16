package sorim.f1.slasher.relentless.repository;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sorim.f1.slasher.relentless.entities.Exposed;
import sorim.f1.slasher.relentless.entities.ExposedVote;
import sorim.f1.slasher.relentless.entities.ExposureChampionship;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
@EnableJpaAuditing
public interface ExposureChampionshipRepository extends CrudRepository<ExposureChampionship, String> {
    List<ExposureChampionship> findAllByIdSeasonOrderByIdRound(Integer season);
}
