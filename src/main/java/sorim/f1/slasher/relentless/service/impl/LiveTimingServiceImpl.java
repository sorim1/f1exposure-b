package sorim.f1.slasher.relentless.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.util.MapTimeZoneCache;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sorim.f1.slasher.relentless.entities.*;
import sorim.f1.slasher.relentless.entities.ergast.Race;
import sorim.f1.slasher.relentless.model.LiveTimingData;
import sorim.f1.slasher.relentless.model.SportSurge;
import sorim.f1.slasher.relentless.model.ergast.ErgastResponse;
import sorim.f1.slasher.relentless.repository.*;
import sorim.f1.slasher.relentless.service.AdminService;
import sorim.f1.slasher.relentless.service.ErgastService;
import sorim.f1.slasher.relentless.service.LiveTimingService;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LiveTimingServiceImpl implements LiveTimingService {

    private final ErgastService service;

    String urlExample = "https://livetiming.formula1.com/static/2019/2019-03-17_Australian_Grand_Prix/2019-03-17_Race/SPFeed.json";
    String LIVETIMING_URL = "https://livetiming.formula1.com/static/";
   // 2019/2019-03-17_Australian_Grand_Prix/2019-03-17_Race/SPFeed.json
   String liveTimingUrl = "https://livetiming.formula1.com/static/{year}/{grandPrix}/{race}/SPFeed.json";

    private final CalendarRepository calendarRepository;

    @Override
    public void getLatestRaceData() {
        getDataUrl();
    }

    @Override
    public void getAllRaceDataFromErgastTable() {
        RestTemplate restTemplate = new RestTemplate();
        List<Race> races = service.fetchCurrentSeason();
        races.forEach(race -> {
            String grandPrixName = race.getRaceName().replaceAll(" ", "_");
            String grandPrix = race.getDate() + "_" + grandPrixName;
            String raceName = race.getDate() + "_Race";
            try {
                String response = restTemplate
                        .getForObject(liveTimingUrl, String.class, race.getSeason(), grandPrix, raceName);
                race.setLiveTiming(response.substring(1));
            }catch(Exception e){
                race.setLiveTiming("ERROR OCCURED: " + e.getMessage());
                log.error("error1:", e);
            }
        });
        service.saveRaces(races);
    }

    @Override
    public LiveTimingData processSingleRace() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        Race race = service.fetchSingleRace();
        ObjectMapper mapper = new ObjectMapper();
        LiveTimingData response = mapper.readValue(race.getLiveTiming(), LiveTimingData.class);
        return response;
    }

    private void getDataUrl() {
        ZonedDateTime gmtZoned = ZonedDateTime.now(ZoneId.of("Europe/London"));
        LocalDateTime gmtDateTime = gmtZoned.toLocalDateTime();
        F1Calendar f1calendar = calendarRepository.findFirstByRaceAfterOrderByRace(gmtDateTime);
    }
}
