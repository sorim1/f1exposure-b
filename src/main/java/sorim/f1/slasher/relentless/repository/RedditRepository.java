package sorim.f1.slasher.relentless.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sorim.f1.slasher.relentless.entities.MyRedditPost;

import java.util.List;

@Repository
@Transactional
public interface RedditRepository extends PagingAndSortingRepository<MyRedditPost, String> {
    List<MyRedditPost> findAllByOrderByCreatedDesc(Pageable pageable);
}
