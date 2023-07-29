package sorim.f1.slasher.relentless.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sorim.f1.slasher.relentless.entities.InstagramPost;

import java.util.List;

@Repository
@Transactional
@EnableJpaAuditing
public interface InstagramRepository extends PagingAndSortingRepository<InstagramPost, String> {

    //@Query("SELECT TOP 500 a FROM InstagramPost a ORDER BY a.deviceTimestamp DESC")
    InstagramPost findTopByOrderByTakenAtDesc();

    List<InstagramPost> findAllByOrderByTakenAtDesc(Pageable pageable);

    boolean existsByCode(String code);
}
