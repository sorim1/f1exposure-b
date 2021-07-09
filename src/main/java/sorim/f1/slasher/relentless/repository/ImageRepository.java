package sorim.f1.slasher.relentless.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import sorim.f1.slasher.relentless.entities.ImageRow;
import sorim.f1.slasher.relentless.entities.InstagramPost;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
@EnableJpaAuditing
public interface ImageRepository extends PagingAndSortingRepository<ImageRow, String> {

    ImageRow findFirstByCode(String Code);
}
