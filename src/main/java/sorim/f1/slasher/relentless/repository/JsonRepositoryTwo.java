package sorim.f1.slasher.relentless.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sorim.f1.slasher.relentless.entities.JsonRepositoryTwoModel;


@Repository
@Transactional
public interface JsonRepositoryTwo extends CrudRepository<JsonRepositoryTwoModel, String> {
    JsonRepositoryTwoModel findAllById(String id);

}
