package sorim.f1.slasher.relentless.service.impl;

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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sorim.f1.slasher.relentless.configuration.MainProperties;
import sorim.f1.slasher.relentless.entities.*;
import sorim.f1.slasher.relentless.handling.Logger;
import sorim.f1.slasher.relentless.model.CalendarData;
import sorim.f1.slasher.relentless.model.SportSurge;
import sorim.f1.slasher.relentless.model.ergast.ErgastResponse;
import sorim.f1.slasher.relentless.repository.*;
import sorim.f1.slasher.relentless.service.AdminService;
import sorim.f1.slasher.relentless.service.ClientService;
import sorim.f1.slasher.relentless.service.ErgastService;
import sorim.f1.slasher.relentless.service.ExposureService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminServiceImpl implements AdminService {

    private static Integer CURRENT_ROUND;

    private final CalendarRepository calendarRepository;
    private final DriverStandingsRepository driverStandingsRepository;
    private final ConstructorStandingsRepository constructorStandingsRepository;
    private final DriverStandingsByRoundRepository driverStandingsByRoundRepository;
    private final ConstructorStandingsByRoundRepository constructorStandingsByRoundRepository;
    private final DriverRepository driverRepository;
    private final SportSurgeStreamRepository sportSurgeStreamRepository;
    private final SportSurgeEventRepository sportSurgeEventRepository;
    private final PropertiesRepository propertiesRepository;
    private final AwsRepository awsRepository;
    private final AwsCommentRepository awsCommentRepository;
    private final F1CommentRepository f1CommentRepository;

    private final ErgastService ergastService;
    private final MainProperties properties;
    private final ClientService clientService;
    private final ExposureService exposureService;
    private RestTemplate restTemplate = new RestTemplate();

    @PostConstruct
    public void onInit() {
        CURRENT_ROUND = Integer.valueOf(propertiesRepository.findDistinctFirstByName("round").getValue());
            }

    @Override
    public void initialize() throws Exception {
        refreshCalendarOfCurrentSeason();
        initializeStandings();
        fetchSportSurgeLinks();
    }

    @Override
    public void refreshCalendarOfCurrentSeason() throws Exception {
        URL url = new URL(properties.getCalendarUrl());
        Reader r = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8);
        CalendarBuilder builder = new CalendarBuilder();
        Calendar calendar = builder.build(r);
        int raceId = 0;
        int currentRaceId;
        List<F1Calendar> f1calendarList = new ArrayList<>();
        F1Calendar f1Calendar = null;
        for (CalendarComponent component : calendar.getComponents().getAll()) {
            if (component.getName().equals("VEVENT")) {
                PropertyList properties = component.getProperties();
                String[] idAndRound = properties.get("UID").get(0).getValue().split("@");
                currentRaceId = Integer.parseInt(idAndRound[1]);
                if (currentRaceId == raceId) {
                    f1Calendar.setDateFromRoundDescription(idAndRound[0], properties.get("DTSTART").get(0).getValue());
                } else {
                    if (f1Calendar != null) {
                        f1calendarList.add(f1Calendar);
                    }
                    raceId = currentRaceId;
                    f1Calendar = new F1Calendar(properties);
                }
            }
        }
        calendarRepository.saveAll(f1calendarList);

    }

    @Override
    public void validateCalendarForNextRace() throws Exception {
        ZonedDateTime gmtZoned = ZonedDateTime.now(ZoneId.of("Europe/London"));
        LocalDateTime gmtDateTime = gmtZoned.toLocalDateTime();
        F1Calendar f1calendar = calendarRepository.findFirstByRaceAfterOrderByRace(gmtDateTime);
        String rawHtml = restTemplate
                .getForObject(properties.getFormula1RacingUrl()+properties.getCurrentYear()+".html", String.class);
            //todo TBC
        Document doc = Jsoup.parse(rawHtml, null);
        Elements clock = doc.getAllElements();
        log.info("clock.wholeText()");
        //Elements tRows1 = clock.getElementsByClass("f1-color--white countdown-text");
        clock.forEach(row -> {
            log.info("row.wholeText()1");
            log.info(row.wholeText());
            row.getAllElements().forEach(row2 -> {
                log.info("row2.wholeText()1");
                log.info(row2.wholeText());
            });
        });
    }

    @Override
    public Boolean initializeStandings() {
        Logger.log("initializeStandings");
        Integer currentRound = refreshDriverStandingsFromErgast();
        if(currentRound== null){
            Logger.log("initializeStandings - no changes detected");
            return false;
        }
        initializeConstructorStandings();
        Logger.log("initializeStandings - changes detected");
        return true;
    }

    @Override
    public Boolean initializeFullStandingsThroughRounds() {
        Logger.log("initializeFullStandingsThroughRounds");
        boolean iterate;
        Integer round = 1;
        Map<String, DriverStandingByRound> driverStandingsByRound = new HashMap<>();
        do{

            ErgastResponse response = ergastService.getDriverStandingsByRound(properties.getCurrentYear(), round);
            if(response.getMrData().getTotal()>0){
                Integer finalRound = round;
                response.getMrData().getStandingsTable().getStandingsLists().get(0).getDriverStandings()
                        .forEach(ergastStanding -> {
                            driverStandingsByRound.put(ergastStanding.getDriver().getDriverId()+finalRound,new DriverStandingByRound(ergastStanding, properties.getCurrentYear(), finalRound) );
                                });
                round++;
                iterate = true;
            } else {
                iterate= false;
            }
        } while(iterate);
        round = 1;

        Map<String, ConstructorStandingByRound> constructorStandingByRound = new HashMap<>();
        do{
            ErgastResponse response = ergastService.getConstructorStandingsByRound(properties.getCurrentYear(), round);
            if(response.getMrData().getTotal()>0){
                Integer finalRound = round;
                response.getMrData().getStandingsTable().getStandingsLists().get(0).getConstructorStandings()
                        .forEach(ergastStanding -> {
                       constructorStandingByRound.put(ergastStanding.getConstructor().getConstructorId()+finalRound, new ConstructorStandingByRound(ergastStanding, properties.getCurrentYear(), finalRound) );

                        });
                round++;
                iterate = true;
            } else {
                iterate= false;
            }
        } while(iterate);
        enrichStandingsWithRoundPoints(driverStandingsByRound, constructorStandingByRound);
        driverStandingsByRoundRepository.deleteAll();
        driverStandingsByRoundRepository.saveAll(driverStandingsByRound.values());
        constructorStandingsByRoundRepository.deleteAll();
        constructorStandingsByRoundRepository.saveAll(constructorStandingByRound.values());
        return true;
    }

    private void enrichStandingsWithRoundPoints(Map<String, DriverStandingByRound> driverStandingsByRound, Map<String, ConstructorStandingByRound> constructorStandingByRound) {
        boolean iterate;
        Integer round = 1;
        do{
            ErgastResponse response = ergastService.getResultsByRound(properties.getCurrentYear(), round);
            if(response.getMrData().getTotal()>0){
                Integer finalRound = round;
                response.getMrData().getRaceTable().getRaces().get(0).getResults()
                        .forEach(ergastStanding -> {
                            driverStandingsByRound.get(ergastStanding.getDriver().getDriverId()+ finalRound).setPointsThisRound(ergastStanding.getPoints());
                            driverStandingsByRound.get(ergastStanding.getDriver().getDriverId()+ finalRound).setResultThisRound(ergastStanding.getPosition());
                            constructorStandingByRound.get(ergastStanding.getConstructor().getConstructorId()+ finalRound).incrementPointsThisRound(ergastStanding.getPoints());
                        });
                round++;
                iterate = true;
            } else {
                iterate= false;
            }
        } while(iterate);
    }

    private Integer refreshDriverStandingsFromErgast() {
        List<DriverStanding> driverStandings = new ArrayList<>();
        Map<String,DriverStandingByRound> driverStandingsByRound = new HashMap<>();
         ErgastResponse response = ergastService.getDriverStandings();
         if(response.getMrData().getStandingsTable().getStandingsLists().get(0).getRound()!=CURRENT_ROUND){
             CURRENT_ROUND = response.getMrData().getStandingsTable().getStandingsLists().get(0).getRound();
             propertiesRepository.updateProperty("round", CURRENT_ROUND.toString());

             response.getMrData().getStandingsTable().getStandingsLists().get(0).getDriverStandings()
                     .forEach(ergastStanding -> {
                         driverStandings.add(new DriverStanding(ergastStanding));
                         driverStandingsByRound.put(ergastStanding.getDriver().getDriverId(), new DriverStandingByRound(ergastStanding, properties.getCurrentYear(),CURRENT_ROUND));
                     });
             response = ergastService.getResultsByRound(properties.getCurrentYear(),CURRENT_ROUND);
             response.getMrData().getRaceTable().getRaces().get(0).getResults()
                     .forEach(ergastStanding -> {
                         driverStandingsByRound.get(ergastStanding.getDriver().getDriverId()).incrementPointsThisRound(ergastStanding.getPoints());
                                });
             driverStandingsRepository.deleteAll();
             driverStandingsRepository.saveAll(driverStandings);
             driverStandingsByRoundRepository.saveAll(driverStandingsByRound.values());
             updateExposureDriverList(driverStandings);
             return CURRENT_ROUND;
         } else {
             return null;
         }
    }


    private void updateExposureDriverList(List<DriverStanding> driverStandings) {
        //poveznica između exposure liste i standings liste
        List<ExposureDriver> drivers = driverRepository.findAll();
        exposureService.setNextRoundOfExposure(driverStandings, CURRENT_ROUND+1);
        for (DriverStanding driverStanding : driverStandings) {
            Optional<ExposureDriver> foundDriver = drivers.stream().filter(x -> driverStanding.getName().equals(x.getFullName()))
                    .findFirst();
            if (foundDriver.isPresent()) {
            } else {
                ExposureDriver newDriver = ExposureDriver.builder()
                        .fullName(driverStanding.getFirstName() + " " + driverStanding.getName())
                        .code(driverStanding.getCode()).build();
                driverRepository.save(newDriver);
            }
        }
    }

    private void initializeConstructorStandings(){
        List<ConstructorStanding> constructorStandings = new ArrayList<>();
        Map<String, ConstructorStandingByRound> constructorStandingsByRound = new HashMap<>();
        ErgastResponse response = ergastService.getConstructorStandings();
        response.getMrData().getStandingsTable().getStandingsLists().get(0).getConstructorStandings()
                    .forEach(ergastStanding -> {
                        constructorStandings.add(new ConstructorStanding(ergastStanding));
                        constructorStandingsByRound.put(ergastStanding.getConstructor().getConstructorId(), new ConstructorStandingByRound(ergastStanding, properties.getCurrentYear(), CURRENT_ROUND));
                    });
        response = ergastService.getResultsByRound(properties.getCurrentYear(),CURRENT_ROUND);
        response.getMrData().getRaceTable().getRaces().get(0).getResults()
                .forEach(ergastStanding -> {
                    constructorStandingsByRound.get(ergastStanding.getConstructor().getConstructorId()).incrementPointsThisRound(ergastStanding.getPoints());
                });
        constructorStandingsRepository.deleteAll();
        constructorStandingsRepository.saveAll(constructorStandings);
        constructorStandingsByRoundRepository.saveAll(constructorStandingsByRound.values());
    }

    @Override
    public Integer fetchSportSurgeLinks() throws IOException {
        Logger.log("fetchSportSurgeLinks");
        WebClient client = new WebClient();
        Page page = client.getPage(properties.getSportSurgeRoot());
        WebResponse response = page.getWebResponse();
        SportSurge sportSurge = new ObjectMapper().readValue(response.getContentAsString(), SportSurge.class);
        List<SportSurgeEvent> events = sportSurge.getEvents();
        List<SportSurgeStream> streams = new ArrayList<>();
        for (SportSurgeEvent event : events) {
            page = client.getPage(properties.getSportSurgeStreams() + event.getId());
            response = page.getWebResponse();
            sportSurge = new ObjectMapper().readValue(response.getContentAsString(), SportSurge.class);
            streams.addAll(sportSurge.getStreams());
        }
        deleteSportSurgeLinks();
        sportSurgeStreamRepository.saveAll(streams);
        sportSurgeEventRepository.saveAll(events);
        return getNextRefreshTick();
    }

    @Override
    public void deleteSportSurgeLinks() {
        Logger.log("deleteSportSurgeLinks");
        sportSurgeStreamRepository.deleteAll();
        sportSurgeEventRepository.deleteAll();
    }

    @Override
    public void closeExposurePoll() {
        Logger.log("closeExposurePoll");
        exposureService.closeExposurePoll();
    }

    @Override
    public F1Calendar getUpcomingRace() {
        ZonedDateTime gmtZoned = ZonedDateTime.now(ZoneId.of("Europe/London"));
        LocalDateTime gmtDateTime = gmtZoned.toLocalDateTime();
        return calendarRepository.findFirstByRaceAfterOrderByRace(gmtDateTime);
    }

    @Override
    public F1Calendar updateUpcomingRace(F1Calendar entry) {
        calendarRepository.save(entry);
        return entry;
    }

    @Override
    public Integer deleteComment(Integer mode, Integer id) {
        switch(mode) {
            case 1:
                return f1CommentRepository.updateStatus(id, 2);
            case 2:
                return awsCommentRepository.updateStatus(id, 2);
        }
        return -1;
    }

    @Override
    public AwsContent patchAwsPost(AwsContent entry) {
        return awsRepository.save(entry);
    }

    private Integer getNextRefreshTick() {
        CalendarData calendarData = clientService.getCountdownData(0);
        if(calendarData.getCountdownData().get("raceDays")>2){
            return null;
        }
        if(calendarData.getCountdownData().get("FP1Seconds")>600){
            return calendarData.getCountdownData().get("FP1Seconds")-600;
        }
        if(calendarData.getCountdownData().get("FP2Seconds")>600){
            return calendarData.getCountdownData().get("FP2Seconds")-600;
        }
        if(calendarData.getCountdownData().get("FP3Seconds")>600){
            return calendarData.getCountdownData().get("FP3Seconds")-600;
        }
        if(calendarData.getCountdownData().get("qualifyingSeconds")>600){
            return calendarData.getCountdownData().get("qualifyingSeconds")-600;
        }
        if(calendarData.getCountdownData().get("raceSeconds")>600){
            return calendarData.getCountdownData().get("raceSeconds")-600;
        }
        return null;
    }
}
