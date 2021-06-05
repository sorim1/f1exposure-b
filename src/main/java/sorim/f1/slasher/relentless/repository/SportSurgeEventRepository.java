package sorim.f1.slasher.relentless.repository;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sorim.f1.slasher.relentless.entities.Driver;
import sorim.f1.slasher.relentless.entities.SportSurgeEvent;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
@EnableJpaAuditing
public interface SportSurgeEventRepository extends CrudRepository<SportSurgeEvent, String> {
    List<SportSurgeEvent> findAllOrderByIdDesc();

}
