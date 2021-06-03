package sorim.f1.slasher.relentless.repository;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import sorim.f1.slasher.relentless.entities.Constructor;
import sorim.f1.slasher.relentless.entities.Driver;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
@EnableJpaAuditing
public interface ConstructorRepository extends PagingAndSortingRepository<Constructor, String>, CrudRepository<Constructor, String> {

    List<Constructor> findAll();

}
