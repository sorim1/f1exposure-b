package sorim.f1.slasher.relentless.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sorim.f1.slasher.relentless.entities.ExposureChampionshipStanding;

import java.util.List;

@Repository
@Transactional
@EnableJpaAuditing
public interface ExposureChampionshipStandingsRepository extends CrudRepository<ExposureChampionshipStanding, String> {

    List<ExposureChampionshipStanding> findAllByIdSeasonOrderByExposureDesc(Integer season);

    @Modifying
    @Query("update ExposureChampionshipStanding e set e.fullName = (select c.fullName from ExposureDriver c where c.code=e.id.driver) where e.fullName is null")
    Integer updateChampionshipNames();

}
