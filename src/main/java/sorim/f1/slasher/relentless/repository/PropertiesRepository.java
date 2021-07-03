package sorim.f1.slasher.relentless.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sorim.f1.slasher.relentless.entities.AppProperty;

import javax.transaction.Transactional;

@Repository
@Transactional
@EnableJpaAuditing
public interface PropertiesRepository extends CrudRepository<AppProperty, String> {
    AppProperty findDistinctFirstByName(String name);

    @Modifying
    @Query("update AppProperty u set u.value = :newValue where u.name = :name")
    void updateProperty(@Param(value = "name") String name, @Param(value = "newValue") String newValue);
}
