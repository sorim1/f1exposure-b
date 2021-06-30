package sorim.f1.slasher.relentless.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sorim.f1.slasher.relentless.entities.Exposed;
import sorim.f1.slasher.relentless.entities.InstagramPost;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
@EnableJpaAuditing
public interface InstagramRepository extends CrudRepository<InstagramPost, String> {

    //@Query("SELECT TOP 500 a FROM InstagramPost a ORDER BY a.deviceTimestamp DESC")
    List<InstagramPost> findFirst50ByOrderByDeviceTimestampDesc();
    List<InstagramPost> findFirst20ByOrderByDeviceTimestampDesc();
}
