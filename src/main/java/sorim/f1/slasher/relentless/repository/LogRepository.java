package sorim.f1.slasher.relentless.repository;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sorim.f1.slasher.relentless.entities.Log;

import javax.transaction.Transactional;

@Repository
@Transactional
@EnableJpaAuditing
public interface LogRepository extends CrudRepository<Log, String> {


}
