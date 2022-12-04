package sorim.f1.slasher.relentless.repository;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sorim.f1.slasher.relentless.entities.Marketing;

@Repository
@Transactional
@EnableJpaAuditing
public interface MarketingRepository extends CrudRepository<Marketing, String> {
    Marketing findById(Integer id);

    void deleteById(Integer id);
}
