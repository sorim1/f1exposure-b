package sorim.f1.slasher.relentless.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sorim.f1.slasher.relentless.entities.FourChanImageRow;

import java.util.List;


@Repository
@Transactional
@EnableJpaAuditing
public interface FourChanImageRepository extends PagingAndSortingRepository<FourChanImageRow, String> {

    FourChanImageRow findFirstById(Integer id);
    List<FourChanImageRow> findAllByIdIn(List<Integer> ids);
    @Modifying
    @Query("update FourChanImageRow u set u.status = :newStatus where u.id = :id")
    Integer updateStatusById(@Param(value = "id") Integer id, @Param(value = "newStatus") Integer newStatus);


    @Query("SELECT COUNT(u) FROM FourChanImageRow u")
    long countRows();
    @Modifying
    Integer deleteByIdGreaterThanAndIdLessThan(Integer id1, Integer id2);

    @Modifying
    Integer deleteByStatusIn(List<Integer> ids);

    @Modifying
    @Query("delete from FourChanImageRow u where u.status = :newStatus")
    void deleteAllByStatus(Integer status);

    Integer deleteById(Integer id);
}
