package sorim.f1.slasher.relentless.repository;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sorim.f1.slasher.relentless.entities.ExposedVote;
import sorim.f1.slasher.relentless.entities.ExposedVoteTotals;

@Repository
@Transactional
@EnableJpaAuditing
public interface ExposedVoteTotalsRepository extends CrudRepository<ExposedVoteTotals, String> {

}
