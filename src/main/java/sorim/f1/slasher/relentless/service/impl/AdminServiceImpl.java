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
import sorim.f1.slasher.relentless.configuration.MainProperties;
import sorim.f1.slasher.relentless.entities.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminServiceImpl implements AdminService {

    private static final String CALENDAR_URL = "https://www.formula1.com/calendar/Formula_1_Official_Calendar.ics";
    private static String DRIVER_STANDINGS_URL = "https://www.formula1.com/en/results.html/2021/drivers.html";
    private static String CONSTRUCTOR_STANDINGS_URL = "https://www.formula1.com/en/results.html/2021/team.html";
    private static final String SPORTSURGE_GROUP_13 = "https://api.sportsurge.net/events/list?group=13";
    private static final String SPORTSURGE_GROUP = "https://api.sportsurge.net/events/list";
    private static final String SPORTSURGE_STREAMS = "https://api.sportsurge.net/streams/list?event=";
    private static Integer CURRENT_ROUND;

    private final CalendarRepository calendarRepository;
    private final DriverStandingsRepository driverStandingsRepository;
    private final ConstructorStandingsRepository constructorStandingsRepository;
    private final DriverRepository driverRepository;
    private final SportSurgeStreamRepository sportSurgeStreamRepository;
    private final SportSurgeEventRepository sportSurgeEventRepository;
    private final PropertiesRepository propertiesRepository;

    private final ErgastService ergastService;
    private final MainProperties properties;
    private final ClientService clientService;
    private final ExposureService exposureService;



    @PostConstruct
    public void onInit() {
        DRIVER_STANDINGS_URL = DRIVER_STANDINGS_URL.replace("2021", properties.getCurrentYear().toString());
        CONSTRUCTOR_STANDINGS_URL = CONSTRUCTOR_STANDINGS_URL.replace("2021", properties.getCurrentYear().toString());
        CURRENT_ROUND = Integer.valueOf(propertiesRepository.findDistinctFirstByName("round").getValue());
            }

    @Override
    public void initialize() throws Exception {
        refreshCalendar();
        initializeStandings();
        fetchSportSurgeLinks();
    }

    @Override
    public void refreshCalendar() throws Exception {
        URL url = new URL(CALENDAR_URL);
        Reader r = new InputStreamReader(url.openStream());
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
    public Boolean initializeStandings() throws IOException {
        List<DriverStanding> driverStandings = initializeDriverStandingsFromErgast();
        if(driverStandings== null){
            log.info("initializeStandings - nista novo");
            return false;
        }
        initializeConstructorStandings();
        return true;
    }

    private List<DriverStanding> initializeDriverStandingsFromErgast() {
        List<DriverStanding> driverStandings = new ArrayList<>();
         ErgastResponse response = ergastService.getDriverStandings();
         if(response.getMrData().getStandingsTable().getStandingsLists().get(0).getRound()!=CURRENT_ROUND){
             response.getMrData().getStandingsTable().getStandingsLists().get(0).getDriverStandings()
                     .forEach(ergastStanding -> {
                         driverStandings.add(new DriverStanding(ergastStanding));
                     });
             driverStandingsRepository.deleteAll();
             driverStandingsRepository.saveAll(driverStandings);
             CURRENT_ROUND = response.getMrData().getStandingsTable().getStandingsLists().get(0).getRound();
             propertiesRepository.updateProperty("round", CURRENT_ROUND.toString());
             updateExposureDriverList(driverStandings);
             return driverStandings;
         } else {
             return null;
         }
    }

    private List<DriverStanding> initializeDriverStandingsFromOfficialWebsite() throws IOException {
        List<DriverStanding> driverStandings = new ArrayList<>();
        Document doc = Jsoup.connect(DRIVER_STANDINGS_URL).get();
        Elements tBody = doc.select("tbody");
        Elements tRows = tBody.select("tr");
        tRows.forEach(row -> {
            DriverStanding driverStanding = createNewDriverStandingFromRow(row);
            driverStandings.add(driverStanding);
        });
        updateExposureDriverList(driverStandings);
        driverStandingsRepository.deleteAll();
        driverStandingsRepository.saveAll(driverStandings);
        return driverStandings;
    }


    private void updateExposureDriverList(List<DriverStanding> driverStandings) {
        //poveznica između exposure liste i standings liste
        List<Driver> drivers = driverRepository.findAll();
        exposureService.setNextRoundOfExposure(driverStandings, CURRENT_ROUND+1);
        for (DriverStanding driverStanding : driverStandings) {
            Optional<Driver> foundDriver = drivers.stream().filter(x -> driverStanding.getName().equals(x.getFullName()))
                    .findFirst();
            if (foundDriver.isPresent()) {
            } else {
                Driver newDriver = Driver.builder().firstName(driverStanding.getFirstName())
                        .lastName(driverStanding.getLastName()).fullName(driverStanding.getName())
                        .code(driverStanding.getCode()).build();
                driverRepository.save(newDriver);
            }
        }
    }

    private List<ConstructorStanding> initializeConstructorStandings() throws IOException {

        List<ConstructorStanding> constructorStandings = new ArrayList<>();
        ErgastResponse response = ergastService.getConstructorStandings();
        response.getMrData().getStandingsTable().getStandingsLists().get(0).getConstructorStandings()
                    .forEach(ergastStanding -> {
                        constructorStandings.add(new ConstructorStanding(ergastStanding));
                    });
        constructorStandingsRepository.deleteAll();
        constructorStandingsRepository.saveAll(constructorStandings);
        return constructorStandings;
    }
    private List<ConstructorStanding> initializeConstructorStandingsFromOfficialWebsite() throws IOException {
        List<ConstructorStanding> constructorStandings = new ArrayList<>();
        Document doc = Jsoup.connect(CONSTRUCTOR_STANDINGS_URL).get();
        Elements tBody = doc.select("tbody");
        Elements tRows = tBody.select("tr");
        tRows.forEach(row -> {
            ConstructorStanding constructorStanding = createNewConstructorStandingFromRow(row);
            constructorStandings.add(constructorStanding);
        });
        constructorStandingsRepository.deleteAll();
        constructorStandingsRepository.saveAll(constructorStandings);
        return constructorStandings;
    }

    @Override
    public Integer fetchSportSurgeLinks() throws IOException {
        WebClient client = new WebClient();
        Page page = client.getPage(SPORTSURGE_GROUP_13);
        WebResponse response = page.getWebResponse();
        SportSurge sportSurge = new ObjectMapper().readValue(response.getContentAsString(), SportSurge.class);
        List<SportSurgeEvent> events = sportSurge.getEvents();
        List<SportSurgeStream> streams = new ArrayList<>();
        for (SportSurgeEvent event : events) {
            page = client.getPage(SPORTSURGE_STREAMS + event.getId());
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
        sportSurgeStreamRepository.deleteAll();
        sportSurgeEventRepository.deleteAll();
    }

    private Integer getNextRefreshTick() {
        CalendarData calendarData = clientService.getCountdownData();
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

    private DriverStanding createNewDriverStandingFromRow(Element row) {
        Elements tColumns = row.select("td");
        Elements spans = tColumns.select("span");
        Elements links = tColumns.select("a");
        DriverStanding driverStanding = new DriverStanding();
        driverStanding.setPosition(Integer.valueOf(tColumns.get(1).wholeText()));
        driverStanding.setNationality(tColumns.get(3).wholeText());
        driverStanding.setPoints(Integer.valueOf(tColumns.get(5).wholeText()));
        driverStanding.setFirstName(spans.get(0).wholeText());
        driverStanding.setLastName(spans.get(1).wholeText());
        driverStanding.setName(spans.get(0).wholeText() + " " + spans.get(1).wholeText());
        driverStanding.setCode(spans.get(2).wholeText());
        driverStanding.setCar(links.get(1).wholeText());
        return driverStanding;
    }

    private ConstructorStanding createNewConstructorStandingFromRow(Element row) {
        Elements tColumns = row.select("td");
        ConstructorStanding constructorStanding = new ConstructorStanding();
        constructorStanding.setPosition(Integer.valueOf(tColumns.get(1).wholeText()));
        constructorStanding.setName(tColumns.get(2).wholeText().trim());
        constructorStanding.setPoints(Integer.valueOf(tColumns.get(3).wholeText()));
        return constructorStanding;
    }
}
