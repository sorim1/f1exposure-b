package sorim.f1.slasher.relentless.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sorim.f1.slasher.relentless.entities.Replay;

import java.util.List;

@Repository
@Transactional
public interface ReplayRepository extends CrudRepository<Replay, String> {
    List<Replay> findAllByOrderByIdDesc(Pageable pageable);

    List<Replay> findAllByStatusGreaterThanEqualOrderByIdDesc(Integer status);

    void deleteById(Integer id);

    Replay findFirstByOrderByIdDesc();
}
