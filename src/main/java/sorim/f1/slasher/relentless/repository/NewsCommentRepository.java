package sorim.f1.slasher.relentless.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sorim.f1.slasher.relentless.entities.NewsComment;

import java.util.List;

@Repository
@Transactional
@EnableJpaAuditing
public interface NewsCommentRepository extends CrudRepository<NewsComment, String> {

    List<NewsComment> findAll();
    List<NewsComment> findAllByContentCodeAndStatusLessThanOrderByTimestampCreatedDesc(String code, Integer status);

    NewsComment findNewsCommentById(Integer id);

    @Modifying
    @Query("update NewsComment u set u.status = :newStatus where u.id = :id")
    Integer updateStatus(@Param(value = "id") Integer id, @Param(value = "newStatus") Integer newStatus);
}
