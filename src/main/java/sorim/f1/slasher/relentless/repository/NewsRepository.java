package sorim.f1.slasher.relentless.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sorim.f1.slasher.relentless.entities.NewsContent;

import java.util.Date;
import java.util.List;

@Repository
@Transactional
@EnableJpaAuditing
public interface NewsRepository extends CrudRepository<NewsContent, String> {

    List<NewsContent> findAll();
    List<NewsContent> findAllByStatusLessThanEqualOrderByTimestampActivityDesc(Integer status, Pageable pageable);
    NewsContent findByCodeAndStatusLessThanEqual(String code, Integer status);
    NewsContent findByCode(String code);
    NewsContent findFirstByStatusLessThanEqualOrderByTimestampActivityDesc(Integer status);

    Integer deleteAllByUsername(String username);

    @Modifying
    @Query("update NewsContent set commentCount = commentCount+1 , timestampActivity = ?2 where code = ?1")
    void updateActivityAndCommentCount(String code, Date date);
}
