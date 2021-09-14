package sorim.f1.slasher.relentless.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sorim.f1.slasher.relentless.entities.RedditPostNew;

import java.util.List;

@Repository
@Transactional
@EnableJpaAuditing
public interface RedditNewRepository extends PagingAndSortingRepository<RedditPostNew, String> {
    List<RedditPostNew> findAllByOrderByCreatedDesc(Pageable pageable);
}
