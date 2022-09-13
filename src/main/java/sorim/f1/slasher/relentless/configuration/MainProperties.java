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
    private String url;
    private Integer currentSeasonPast =2024;
    private Integer currentSeasonFuture =2025;
    private Boolean twitterDebug;
    private String twitterKey;
    private String twitterSecret;
    private String twitterAccessToken;
    private String twitterAccessTokenSecret;
    private String instagramWorkerUsername;
    //private String instagramWorkerPassword;
    private String instagramOfficialUsername;
    private String instagramOfficialPassword;
    private String[] clients;
    private String[] admins;
    private String calendarUrl;
    private String sportSurgeRoot;

    private Integer howManySeasonsBack;
    private String sportSurgeStreams;
    private String replaysUrl;
    private String saveRadioLocation;
    private final PropertiesRepository propertiesRepository;


    @PostConstruct
    public void setProperty() {
        System.setProperty("net.fortuna.ical4j.timezone.cache.impl", MapTimeZoneCache.class.getName());
        AppProperty ap = propertiesRepository.findDistinctFirstByName("CURRENT_SEASON_PAST");
        if(ap==null){
            ap = AppProperty.builder().name("CURRENT_SEASON_PAST").value("2021").build();
            propertiesRepository.save(ap);
            this.currentSeasonPast = 2022;
        } else{
            this.currentSeasonPast = Integer.valueOf(ap.getValue());
        }

        this.currentSeasonFuture = Calendar.getInstance().get(Calendar.YEAR);
            ap = AppProperty.builder().name("CURRENT_SEASON_FUTURE").value(String.valueOf(this.currentSeasonFuture)).build();
            propertiesRepository.save(ap);
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

    public void saveProperty(String name, String value) {
        AppProperty ap = AppProperty.builder().name(name)
                .value(value).build();
        propertiesRepository.save(ap);
    }
}
