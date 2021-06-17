package sorim.f1.slasher.relentless.repository;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sorim.f1.slasher.relentless.entities.F1Calendar;
import sorim.f1.slasher.relentless.entities.ergast.Race;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Repository
@Transactional
@EnableJpaAuditing
public interface ErgastRaceRepository extends CrudRepository<Race, String> {
}
