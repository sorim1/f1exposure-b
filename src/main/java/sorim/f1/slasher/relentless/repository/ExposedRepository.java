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

    List<Exposed> findByRaceIdOrderByCounterDesc(Integer raceId);

    @Modifying
    @Query("update Exposed set counter = counter+1 where raceId = ?1 and driver.code = ?2 ")
    Integer incrementExposed(Integer raceId, String driverCode);

    @Modifying
    @Query(value = "insert into Exposed (race_id, driver_code, counter) VALUES (:raceId,:driverCode, 1)", nativeQuery = true)
    Integer saveExposureData(@Param("raceId") Integer raceId, @Param("driverCode")String driverCode);

}
