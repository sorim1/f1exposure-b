package sorim.f1.slasher.relentless.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "f1.exposure.properties")
public class MainProperties {

    private String tempo;

}
