package sorim.f1.slasher.relentless.repository;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import sorim.f1.slasher.relentless.entities.ExposedVote;

import javax.transaction.Transactional;

@Repository
@Transactional
@EnableJpaAuditing
public interface ExposedVoteRepository extends PagingAndSortingRepository<ExposedVote, String>, CrudRepository<ExposedVote, String> {

    boolean existsExposedVoteByIpAddress(String ipAddress);
}
