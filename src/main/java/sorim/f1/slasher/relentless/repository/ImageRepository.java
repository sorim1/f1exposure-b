package sorim.f1.slasher.relentless.repository;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sorim.f1.slasher.relentless.entities.ImageRow;


@Repository
@Transactional
@EnableJpaAuditing
public interface ImageRepository extends PagingAndSortingRepository<ImageRow, String> {

    ImageRow findFirstByCode(String Code);
}
