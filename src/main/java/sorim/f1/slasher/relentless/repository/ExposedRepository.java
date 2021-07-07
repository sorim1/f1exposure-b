package sorim.f1.slasher.relentless.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sorim.f1.slasher.relentless.entities.Exposed;

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

}
