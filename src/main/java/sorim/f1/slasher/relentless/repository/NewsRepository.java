package sorim.f1.slasher.relentless.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sorim.f1.slasher.relentless.entities.NewsContent;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface NewsRepository extends CrudRepository<NewsContent, String> {

    List<NewsContent> findAll();

    List<NewsContent> findAllByStatusLessThanEqualOrderByTimestampActivityDesc(Integer status, Pageable pageable);

    List<NewsContent> findAllByTimestampActivityBeforeOrderByTimestampActivityDesc(Date timestampActivity, Pageable pageable);

    @Query("SELECT n FROM NewsContent n WHERE " +
            "LOWER(n.title) LIKE LOWER(CONCAT('%', :substring, '%')) " +
            "AND n.timestampCreated > :timestampCreated")
    List<NewsContent> findAllByTitleContainingIgnoreCase(@Param("substring") String substring, @Param("timestampCreated") Date timestampCreated);


    NewsContent findByCodeAndStatusLessThanEqual(String code, Integer status);

    NewsContent findByCode(String code);

    Integer deleteByCode(String code);

    NewsContent findFirstByStatusLessThanEqualOrderByTimestampActivityDesc(Integer status);

    Integer deleteAllByUsername(String username);

    @Modifying
    void deleteByTimestampCreatedBefore(Date timestampCreated);

    @Modifying
    @Query("update NewsContent set commentCount = commentCount+1 , timestampActivity = ?2 where code = ?1")
    void updateActivityAndCommentCount(String code, Date date);
}
