package sorim.f1.slasher.relentless.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sorim.f1.slasher.relentless.entities.Exposed;
import sorim.f1.slasher.relentless.entities.ExposedVoteTotals;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
@EnableJpaAuditing
public interface ExposedRepository extends CrudRepository<Exposed, String> {

    List<Exposed> findBySeasonAndRoundOrderByCounterDesc(Integer season, Integer round);

    @Modifying
    @Query("update Exposed set counter = counter+1 where season = ?1 and round = ?2 and driver.code = ?3 ")
    Integer incrementExposed(Integer season, Integer round, String driverCode);

    @Modifying
    @Query(value = "insert into Exposed (season, round, driver_code, counter) VALUES (:season, :round,:driverCode, :counter)", nativeQuery = true)
    Integer saveExposureData(@Param("season") Integer season, @Param("round") Integer round, @Param("driverCode")String driverCode, @Param("counter")int counter);

    @Modifying
    @Query("update ExposedVoteTotals set voters = voters+1 , votes = votes + ?3 where id.season = ?1 and id.round = ?2")
    Integer incrementTotal(Integer season, Integer round, int votes);

    @Modifying
    @Query(value = "insert into EXPOSED_VOTE_TOTALS(season, round, voters, votes) VALUES (:season, :round, 1, :votes)", nativeQuery = true)
    void insertExposureTotal(Integer season, Integer round, Integer votes);

    @Query("select t from ExposedVoteTotals t where t.id.season = ?1 and t.id.round = ?2")
    ExposedVoteTotals findExposedTotalBySeasonAndRound(Integer season, Integer round);
}
