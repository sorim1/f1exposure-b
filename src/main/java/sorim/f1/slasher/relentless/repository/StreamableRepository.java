package sorim.f1.slasher.relentless.repository;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sorim.f1.slasher.relentless.entities.Streamable;

import java.util.List;

@Repository
@Transactional
@EnableJpaAuditing
public interface StreamableRepository extends CrudRepository<Streamable, String> {
    List<Streamable> findAllByOrderByIdDesc();
}
