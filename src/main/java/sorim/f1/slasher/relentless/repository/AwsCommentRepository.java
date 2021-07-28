package sorim.f1.slasher.relentless.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sorim.f1.slasher.relentless.entities.AwsComment;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
@EnableJpaAuditing
public interface AwsCommentRepository extends CrudRepository<AwsComment, String> {
    List<AwsComment> findAllByContentCodeAndStatusOrderByTimestampCreatedDesc(String code, Integer status);

    @Modifying
    @Query("update AwsComment u set u.status = :newStatus where u.id = :id")
    Integer updateStatus(@Param(value = "id") Integer id, @Param(value = "newStatus") Integer newStatus);
}
