package sorim.f1.slasher.relentless.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sorim.f1.slasher.relentless.entities.Log;

import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface LogRepository extends CrudRepository<Log, String> {

    List<Log> findAllByCreatedBeforeOrderByIdDesc(Date created);

    List<Log> findAllByCreatedAfterOrderByIdDesc(Date created);
}
