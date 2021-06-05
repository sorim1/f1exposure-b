package sorim.f1.slasher.relentless.repository;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import sorim.f1.slasher.relentless.entities.DriverStanding;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
@EnableJpaAuditing
public interface DriverStandingsRepository extends CrudRepository<DriverStanding, String> {
    List<DriverStanding> findAll();
}
