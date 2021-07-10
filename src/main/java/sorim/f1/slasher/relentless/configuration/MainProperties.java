package sorim.f1.slasher.relentless.configuration;

import lombok.Data;
import net.fortuna.ical4j.util.MapTimeZoneCache;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Data
@Configuration
@ConfigurationProperties(prefix = "f1.exposure.properties")
public class MainProperties {

    private String tempo;
    private Integer currentYear;
    private Boolean twitterDebug;
    private String twitterKey;
    private String twitterSecret;
    private String twitterAccessToken;
    private String twitterAccessTokenSecret;
    private String instagramUsername;
    private String instagramPassword;

    @PostConstruct
    public void setProperty() {
        System.setProperty("net.fortuna.ical4j.timezone.cache.impl", MapTimeZoneCache.class.getName());
    }
}
