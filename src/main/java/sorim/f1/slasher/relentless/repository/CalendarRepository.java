package sorim.f1.slasher.relentless.repository;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import sorim.f1.slasher.relentless.entities.F1Calendar;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
@EnableJpaAuditing
public interface CalendarRepository extends CrudRepository<F1Calendar, String> {

    F1Calendar findById(Integer id);

    F1Calendar findFirstByRaceAfterOrderByRace(LocalDateTime date);
    F1Calendar findFirstByRaceBeforeOrderByRaceDesc(LocalDateTime date);
}
