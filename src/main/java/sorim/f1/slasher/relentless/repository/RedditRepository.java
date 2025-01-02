package sorim.f1.slasher.relentless.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sorim.f1.slasher.relentless.entities.MyRedditPost;

import java.util.List;

@Repository
@Transactional
@EnableJpaAuditing
public interface RedditRepository extends JpaRepository<MyRedditPost, String> {
    List<MyRedditPost> findAllByOrderByCreatedDesc(Pageable pageable);
}
