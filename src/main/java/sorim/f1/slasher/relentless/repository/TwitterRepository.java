package sorim.f1.slasher.relentless.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sorim.f1.slasher.relentless.entities.TwitterPost;

import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface TwitterRepository extends PagingAndSortingRepository<TwitterPost, String> {
    List<TwitterPost> findAllByOrderByCreatedAtDesc(Pageable pageable);

    TwitterPost findFirstByCreatedAtAfterOrderByRetweetCountDesc(Date yesterday);
    TwitterPost findFirstByOrderByCreatedAtDesc();


    @Modifying
    void deleteByCreatedAtBefore(Date expiryDate);
}
