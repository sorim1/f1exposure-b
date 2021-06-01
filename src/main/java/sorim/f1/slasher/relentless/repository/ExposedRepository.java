package sorim.f1.slasher.relentless.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import sorim.f1.slasher.relentless.entities.Exposed;
import sorim.f1.slasher.relentless.entities.ExposedVote;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
@EnableJpaAuditing
public interface ExposedRepository extends PagingAndSortingRepository<Exposed, String>, CrudRepository<Exposed, String> {

    List<Exposed> findByRaceId(Integer raceId);
    @Modifying
    @Query("update Exposed set counter = counter+1 where raceId = ?1 and driverId = ?2 ")
    Integer incrementExposed(Integer raceId, Integer driverId);
}
