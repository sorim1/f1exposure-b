package sorim.f1.slasher.relentless.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sorim.f1.slasher.relentless.entities.F1Comment;
import sorim.f1.slasher.relentless.entities.SportSurgeEvent;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
@EnableJpaAuditing
public interface F1CommentRepository extends CrudRepository<F1Comment, String> {
    List<F1Comment> findFirst30ByPageAndStatusOrderByTimestampDesc(Integer page, Integer status);

    F1Comment findF1CommentByIdAndStatus(Integer id, Integer status);

    @Modifying
    @Query("update F1Comment u set u.status = :newStatus where u.id = :id")
    Integer updateStatus(@Param(value = "id") Integer name, @Param(value = "newStatus") Integer newStatus);



}
