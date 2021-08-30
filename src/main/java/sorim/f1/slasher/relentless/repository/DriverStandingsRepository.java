package sorim.f1.slasher.relentless.repository;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sorim.f1.slasher.relentless.entities.DriverStanding;

import java.util.List;

@Repository
@Transactional
@EnableJpaAuditing
public interface DriverStandingsRepository extends CrudRepository<DriverStanding, String> {
    List<DriverStanding> findAllByOrderByPositionAsc();
}
