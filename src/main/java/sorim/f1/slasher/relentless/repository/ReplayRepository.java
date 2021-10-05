package sorim.f1.slasher.relentless.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sorim.f1.slasher.relentless.entities.F1Comment;
import sorim.f1.slasher.relentless.entities.Replay;
import sorim.f1.slasher.relentless.entities.SportSurgeStream;
import sorim.f1.slasher.relentless.entities.TwitterPost;

import java.util.List;

@Repository
@Transactional
@EnableJpaAuditing
public interface ReplayRepository extends CrudRepository<Replay, String> {
    List<Replay> findAllByOrderByIdDesc(Pageable pageable);
    Replay findFirstByOrderByIdDesc();
}
