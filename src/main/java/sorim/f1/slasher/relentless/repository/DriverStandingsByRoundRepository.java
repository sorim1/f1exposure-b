package sorim.f1.slasher.relentless.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sorim.f1.slasher.relentless.entities.DriverStandingByRound;

import java.util.List;

@Repository
@Transactional
@EnableJpaAuditing
public interface DriverStandingsByRoundRepository extends CrudRepository<DriverStandingByRound, String> {
    @NotNull
    List<DriverStandingByRound> findAll();

    List<DriverStandingByRound> findAllByIdSeasonOrderByIdRoundAscNameAsc(Integer season);

    DriverStandingByRound findFirstByCodeOrderByIdSeasonDesc(String code);
}
