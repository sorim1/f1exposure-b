/*
package sorim.f1.slasher.relentless.configuration;


import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateJacksonConfiguration {
    private final EntityManagerFactory entityManagerFactory;

    public HibernateConfig(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @PostConstruct
    public void configureHibernate() {
        CustomObjectMapperConfig.configure(entityManagerFactory);
    }
}*/
