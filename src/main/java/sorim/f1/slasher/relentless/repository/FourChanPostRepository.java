package sorim.f1.slasher.relentless.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sorim.f1.slasher.relentless.entities.FourChanPostEntity;

import java.util.List;

@Repository
@Transactional
@EnableJpaAuditing
public interface FourChanPostRepository extends CrudRepository<FourChanPostEntity, String> {
    List<FourChanPostEntity> findAllByOrderByIdDesc(Pageable pageable);

    List<FourChanPostEntity> findAllByStatusOrderByIdAsc(Integer thread, Pageable pageable);
    FourChanPostEntity findFirstByStatusInOrderByIdAsc(List<Integer> statusList);
    FourChanPostEntity findFirstByIdGreaterThanAndStatusInOrderByIdAsc(Integer id, List<Integer> statusList);
    FourChanPostEntity findFirstByIdLessThanAndStatusInOrderByIdAsc(Integer id, List<Integer> statusList);
    @Query("SELECT COUNT(u) FROM FourChanPostEntity u WHERE u.status= :status")
    long countRowsByStatus(Integer status);
    Integer deleteById(Integer id);
}
