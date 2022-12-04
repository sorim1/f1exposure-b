package sorim.f1.slasher.relentless.repository;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sorim.f1.slasher.relentless.entities.ArtImageRow;

import java.util.List;


@Repository
@Transactional
@EnableJpaAuditing
public interface ArtImageRepository extends PagingAndSortingRepository<ArtImageRow, String> {

    ArtImageRow findFirstByCode(String Code);

    Integer deleteByCode(String code);

    List<ArtImageRow> findAllByOrderBySeasonDescRoundDesc();
}
