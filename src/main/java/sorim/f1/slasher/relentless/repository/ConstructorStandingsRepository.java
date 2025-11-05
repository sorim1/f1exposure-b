package sorim.f1.slasher.relentless.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sorim.f1.slasher.relentless.entities.ConstructorStanding;

import java.util.List;

@Repository
@Transactional
public interface ConstructorStandingsRepository extends CrudRepository<ConstructorStanding, String> {
    List<ConstructorStanding> findAllByOrderByPositionAsc();
}
