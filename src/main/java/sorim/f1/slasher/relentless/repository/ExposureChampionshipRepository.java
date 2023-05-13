package sorim.f1.slasher.relentless.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sorim.f1.slasher.relentless.entities.ExposureChampionship;

import java.util.List;

@Repository
@Transactional
@EnableJpaAuditing
public interface ExposureChampionshipRepository extends CrudRepository<ExposureChampionship, String> {
    List<ExposureChampionship> findAllByIdSeasonAndStatusOrderByIdRound(Integer season, Integer status);
    List<ExposureChampionship> findAllByIdSeasonAndIdRoundOrderByVotesDesc(Integer season, Integer round);
    ExposureChampionship findFirstByIdSeasonAndIdRoundOrderByVotesDesc(Integer season, Integer round);
    Integer deleteByIdSeasonAndIdRoundOrderByVotesDesc(Integer season, Integer round);

    @Modifying
    @Query("update ExposureChampionship e set e.status = 3")
    Integer closeAllPolls();
}
