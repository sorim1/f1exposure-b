package sorim.f1.slasher.relentless.configuration;

import lombok.Data;
import net.fortuna.ical4j.util.MapTimeZoneCache;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import sorim.f1.slasher.relentless.entities.AppProperty;
import sorim.f1.slasher.relentless.repository.PropertiesRepository;

import java.time.LocalDate;
import jakarta.annotation.PostConstruct;
import java.util.Calendar;

@Data
@Configuration
@ConfigurationProperties(prefix = "f1.exposure.properties")
public class MainProperties {

    private final PropertiesRepository propertiesRepository;
    public String formula1RacingUrl;
    private String url;
    private Integer currentSeasonPast;
    private Integer currentSeasonFuture;
    private Boolean twitterDebug;
    private String twitterKey;
    private String twitterSecret;
    private String twitterAccessToken;
    private String twitterAccessTokenSecret;
    private String twitterClientId;
    private String twitterClientSecret;
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

    @PostConstruct
    public void setProperty() {
        System.setProperty("net.fortuna.ical4j.timezone.cache.impl", MapTimeZoneCache.class.getName());
        AppProperty ap = propertiesRepository.findDistinctFirstByName("CURRENT_SEASON_PAST");
        if (ap != null) {
            this.currentSeasonPast = Integer.valueOf(ap.getValue());
        } else {
            Integer currentYear = LocalDate.now().getYear();
            saveProperty("CURRENT_SEASON_PAST", String.valueOf(currentYear));
            this.currentSeasonPast = currentYear;
        }
        ap = propertiesRepository.findDistinctFirstByName("CURRENT_SEASON_FUTURE");
        if (ap != null) {
            this.currentSeasonFuture = Integer.valueOf(ap.getValue());
            checkCurrentSeasonFuture();
        } else {
            Integer currentYear = LocalDate.now().getYear();
            saveProperty("CURRENT_SEASON_FUTURE", String.valueOf(currentYear));
            this.currentSeasonFuture = currentYear;
        }
        System.out.println(this.currentSeasonPast + " : " + this.currentSeasonFuture);
    }

    public String updateCurrentSeasonPast(Integer newValue) {
        String response = this.currentSeasonPast + " -> ";
        AppProperty ap = AppProperty.builder().name("CURRENT_SEASON_PAST").value(String.valueOf(newValue)).build();
        propertiesRepository.save(ap);
        this.currentSeasonPast = newValue;
        response += this.currentSeasonPast;
        checkCurrentSeasonFuture();
        return response;
    }

    public void checkCurrentSeasonFuture() {
        Integer currentYear = LocalDate.now().getYear();
        if(currentYear!=this.currentSeasonFuture){
            saveProperty("CURRENT_SEASON_FUTURE", String.valueOf(currentYear));
            this.currentSeasonFuture = currentYear;
        }
    }

    public void saveProperty(String name, String value) {
        AppProperty ap = AppProperty.builder().name(name)
                .value(value).build();
        propertiesRepository.save(ap);
    }
}
