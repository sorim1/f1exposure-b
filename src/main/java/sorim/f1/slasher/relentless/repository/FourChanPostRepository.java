package sorim.f1.slasher.relentless.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sorim.f1.slasher.relentless.entities.FourChanPostEntity;

import java.util.List;

@Repository
@Transactional
@EnableJpaAuditing
public interface FourChanPostRepository extends CrudRepository<FourChanPostEntity, String> {
    List<FourChanPostEntity> findAllByOrderByIdDesc(Pageable pageable);
    Integer deleteById(Integer id);
}
