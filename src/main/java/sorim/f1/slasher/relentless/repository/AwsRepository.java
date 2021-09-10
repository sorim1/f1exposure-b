package sorim.f1.slasher.relentless.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sorim.f1.slasher.relentless.entities.AwsContent;

import java.util.Date;
import java.util.List;

@Repository
@Transactional
@EnableJpaAuditing
public interface AwsRepository extends CrudRepository<AwsContent, String> {
    List<AwsContent> findAllByStatusOrderByTimestampActivityDesc(Integer status, Pageable pageable);
    AwsContent findByCodeAndStatus(String code, Integer status);

    @Modifying
    @Query("update AwsContent set commentCount = commentCount+1 , timestampActivity = ?2 where code = ?1")
    void updateActivityAndCommentCount(String code, Date date);
}
