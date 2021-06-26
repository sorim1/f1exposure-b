package sorim.f1.slasher.relentless.repository;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sorim.f1.slasher.relentless.entities.F1Comment;
import sorim.f1.slasher.relentless.entities.SportSurgeEvent;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
@EnableJpaAuditing
public interface F1CommentRepository extends CrudRepository<F1Comment, String> {
    List<F1Comment> findFirst30ByPageOrderByTimestampDesc(Integer page);


}
