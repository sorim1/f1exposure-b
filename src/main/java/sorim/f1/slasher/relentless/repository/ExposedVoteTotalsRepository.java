package sorim.f1.slasher.relentless.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sorim.f1.slasher.relentless.entities.ExposedVoteTotals;

import java.util.List;

@Repository
@Transactional
@EnableJpaAuditing
public interface ExposedVoteTotalsRepository extends CrudRepository<ExposedVoteTotals, String> {

    @Query("select t from ExposedVoteTotals t where t.id.season = ?1 and t.id.round = ?2")
    ExposedVoteTotals findExposedTotalBySeasonAndRound(Integer season, Integer round);

    @Query("select t.voters from ExposedVoteTotals t where t.id.season = ?1 order by t.id.round asc")
    List<Integer> getVoterCountOfSeason(Integer season);

}
