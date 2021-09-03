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

    public String formula1RacingUrl;
    private String tempo;
    private Integer currentYear;
    private Boolean twitterDebug;
    private String twitterKey;
    private String twitterSecret;
    private String twitterAccessToken;
    private String twitterAccessTokenSecret;
    private String instagramUsername;
    private String instagramPassword;
    private String[] clients;
    private String[] admins;
    private String calendarUrl;
    private String sportSurgeRoot;
    private String sportSurgeStreams;
    private Integer howManySeasonsBack;

    @PostConstruct
    public void setProperty() {
        System.setProperty("net.fortuna.ical4j.timezone.cache.impl", MapTimeZoneCache.class.getName());
    }
}
