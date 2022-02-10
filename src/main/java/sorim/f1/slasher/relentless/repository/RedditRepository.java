package sorim.f1.slasher.relentless.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sorim.f1.slasher.relentless.entities.RedditPost;

import java.util.List;

@Repository
@Transactional
@EnableJpaAuditing
public interface RedditRepository extends PagingAndSortingRepository<RedditPost, String> {
    List<RedditPost> findAllByOrderByCreatedDesc(Pageable pageable);
}
