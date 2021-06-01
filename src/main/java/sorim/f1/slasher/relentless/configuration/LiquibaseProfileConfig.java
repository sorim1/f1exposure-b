/*
package sorim.f1.slasher.relentless.configuration;

import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.liquibase.DataSourceClosingSpringLiquibase;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

@Configuration
public class LiquibaseProfileConfig {
  private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LiquibaseProfileConfig.class);

  @Bean
  @ConfigurationProperties(prefix = "spring.liquibase.datasource.profile-dps")
  public LiquibaseProperties profileLiquibaseProperties() {
    return new LiquibaseProperties();
  }

  @Bean
  public SpringLiquibase profileDpsLiquibase(@Qualifier("profileDpsDataSource") DataSource profileDpsDataSource, @Qualifier("profileLiquibaseProperties") LiquibaseProperties profileLiquibaseProperties) throws SQLException {
    if (!(profileDpsDataSource instanceof HikariDataSource)) {
      throw new IllegalStateException("Profile dps datasource is not baes on hikari connection pool!");
    }
    return springLiquibaseOwnDatasource((HikariDataSource) profileDpsDataSource, profileLiquibaseProperties);
  }

  public static SpringLiquibase springLiquibaseOwnDatasource(HikariDataSource hikariDataSource, LiquibaseProperties properties) {
    DataSourceBuilder liquibaseDataSourceBuilder = DataSourceBuilder.create();
    liquibaseDataSourceBuilder.driverClassName(hikariDataSource.getDriverClassName());
    liquibaseDataSourceBuilder.url(hikariDataSource.getJdbcUrl());
    liquibaseDataSourceBuilder.username(properties.getUser());
    liquibaseDataSourceBuilder.password(properties.getPassword());
    liquibaseDataSourceBuilder.type(HikariDataSource.class);
    DataSourceClosingSpringLiquibase liquibase = new DataSourceClosingSpringLiquibase();
    setLiquibaseProperties(liquibase, liquibaseDataSourceBuilder.build(), properties);
    return liquibase;
  }

  public static void setLiquibaseProperties(SpringLiquibase liquibase, DataSource dataSource, LiquibaseProperties properties) {
    liquibase.setDataSource(dataSource);
    liquibase.setChangeLog(properties.getChangeLog());
    liquibase.setContexts(properties.getContexts());
    liquibase.setDefaultSchema(properties.getDefaultSchema());
    liquibase.setDropFirst(properties.isDropFirst());
    liquibase.setShouldRun(properties.isEnabled());
    liquibase.setLabels(properties.getLabels());
    liquibase.setChangeLogParameters(properties.getParameters());
    liquibase.setRollbackFile(properties.getRollbackFile());
  }
}
*/
