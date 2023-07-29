package sorim.f1.slasher.relentless.repository;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sorim.f1.slasher.relentless.entities.Ban;

@Repository
@Transactional
@EnableJpaAuditing
public interface BanlistRepository extends CrudRepository<Ban, String> {
    boolean existsBanByIp(String ip);

    boolean existsBanByIpAndStatus(String ip, Integer status);
}
