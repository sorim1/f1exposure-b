package sorim.f1.slasher.relentless.repository;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sorim.f1.slasher.relentless.entities.ExposureChampionshipStanding;
import sorim.f1.slasher.relentless.model.enums.ExposureModeEnum;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
@EnableJpaAuditing
public interface ExposureChampionshipStandingsRepository extends CrudRepository<ExposureChampionshipStanding, String> {

    List<ExposureChampionshipStanding> findAllByIdSeasonAndIdModeOrderByExposureDesc(Integer season, ExposureModeEnum mode);
}
