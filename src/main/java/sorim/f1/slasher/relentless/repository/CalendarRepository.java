package sorim.f1.slasher.relentless.repository;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sorim.f1.slasher.relentless.entities.F1Calendar;

import java.time.LocalDateTime;

@Repository
@Transactional
@EnableJpaAuditing
public interface CalendarRepository extends CrudRepository<F1Calendar, String> {

    F1Calendar findFirstByRaceAfterOrderByRace(LocalDateTime date);
    F1Calendar findFirstByRaceBeforeOrderByRaceDesc(LocalDateTime date);

}
