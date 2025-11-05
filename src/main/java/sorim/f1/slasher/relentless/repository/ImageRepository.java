package sorim.f1.slasher.relentless.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sorim.f1.slasher.relentless.entities.ImageRow;


@Repository
@Transactional
public interface ImageRepository extends PagingAndSortingRepository<ImageRow, String> {

    ImageRow findFirstByCode(String code);

    @Modifying
    @Query("delete from ImageRow i where i.code not like 'M%'")
    Integer deleteEverythingExceptM();

    @Modifying
    @Query("delete from ImageRow i where i.code like 'INSTA_%'")
    Integer deleteEverythingInsta();

}
