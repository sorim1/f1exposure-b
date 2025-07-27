package sorim.f1.slasher.relentless.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.hibernate.type.format.FormatMapper;
import org.hibernate.type.format.jackson.JacksonJsonFormatMapper;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;

@Configuration
public class CustomObjectMapperConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    @Bean
    @Primary
    public FormatMapper formatMapper(ObjectMapper objectMapper) {
        JacksonJsonFormatMapper mapper = new JacksonJsonFormatMapper(objectMapper);
        System.out.println("FormatMapper initialized: " + mapper);
        return mapper;
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(FormatMapper formatMapper) {
        return properties -> properties.put("hibernate.type.json_format_mapper", formatMapper);
    }

   /* @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            DataSource dataSource, JpaProperties jpaProperties, FormatMapper formatMapper) {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setPackagesToScan(
                "sorim.f1.slasher.relentless.model",
                "sorim.f1.slasher.relentless.entities"
        );
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setJpaPropertyMap(jpaProperties.getProperties());

        // Set JSON FormatMapper for LocalDateTime support
        factory.getJpaPropertyMap().put("hibernate.type.json_format_mapper", formatMapper);

        // Set SpringPhysicalNamingStrategy to restore camelCase to snake_case
        factory.getJpaPropertyMap().put("hibernate.physical_naming_strategy",
                SpringPhysicalNamingStrategy.class.getName());

        System.out.println("EntityManagerFactory initialized with FormatMapper: " + formatMapper);
        return factory;
    }*/
}