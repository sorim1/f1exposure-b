package sorim.f1.slasher.relentless.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sorim.f1.slasher.relentless.entities.ConstructorStanding;
import sorim.f1.slasher.relentless.entities.ConstructorStandingByRound;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
@EnableJpaAuditing
public interface ConstructorStandingsByRoundRepository extends CrudRepository<ConstructorStandingByRound, String> {
    @NotNull
    List<ConstructorStandingByRound> findAll();

    List<ConstructorStandingByRound> findAllByIdSeasonOrderByIdRoundAscNameAsc(Integer season);
}
