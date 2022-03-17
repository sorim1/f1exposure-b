package sorim.f1.slasher.relentless.configuration;

import lombok.Data;
import net.fortuna.ical4j.util.MapTimeZoneCache;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import sorim.f1.slasher.relentless.entities.AppProperty;
import sorim.f1.slasher.relentless.repository.PropertiesRepository;

import javax.annotation.PostConstruct;
import java.util.Calendar;

@Data
@Configuration
@ConfigurationProperties(prefix = "f1.exposure.properties")
public class MainProperties {

    public String formula1RacingUrl;
    private String tempo;
    private Integer currentSeasonPast =2024;
    private Integer currentSeasonFuture =2025;
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

    private Integer howManySeasonsBack;
    private String sportSurgeStreams;
    private String replaysUrl;
    private final PropertiesRepository propertiesRepository;


    @PostConstruct
    public void setProperty() {
        System.setProperty("net.fortuna.ical4j.timezone.cache.impl", MapTimeZoneCache.class.getName());
        AppProperty ap = propertiesRepository.findDistinctFirstByName("CURRENT_SEASON_PAST");
        if(ap==null){
            ap = AppProperty.builder().name("CURRENT_SEASON_PAST").value("2021").build();
            propertiesRepository.save(ap);
            this.currentSeasonPast = 2021;
        } else{
            this.currentSeasonPast = Integer.valueOf(ap.getValue());
        }

        this.currentSeasonFuture = Calendar.getInstance().get(Calendar.YEAR);
            ap = AppProperty.builder().name("CURRENT_SEASON_FUTURE").value(String.valueOf(this.currentSeasonFuture)).build();
            propertiesRepository.save(ap);

        System.out.println("currentSeasonPast: " + this.currentSeasonPast);
    }

    public String updateCurrentSeasonPast(Integer newValue) {
        String response = this.currentSeasonPast + " -> ";
        AppProperty ap = AppProperty.builder().name("CURRENT_SEASON_PAST").value(String.valueOf(newValue)).build();
        propertiesRepository.save(ap);
        this.currentSeasonPast = newValue;
        response+=this.currentSeasonPast;
        checkCurrentSeasonFuture();
        return response;
    }

    public void checkCurrentSeasonFuture() {
        this.currentSeasonFuture = Calendar.getInstance().get(Calendar.YEAR);
    }
}
