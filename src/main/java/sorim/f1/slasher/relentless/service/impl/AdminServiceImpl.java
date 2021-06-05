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
import net.fortuna.ical4j.util.MapTimeZoneCache;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sorim.f1.slasher.relentless.entities.*;
import sorim.f1.slasher.relentless.model.SportSurge;
import sorim.f1.slasher.relentless.repository.*;
import sorim.f1.slasher.relentless.service.AdminService;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminServiceImpl implements AdminService {

    private static final String CALENDAR_URL = "https://www.formula1.com/calendar/Formula_1_Official_Calendar.ics";
    private static final String DRIVER_STANDINGS_URL = "https://www.formula1.com/en/results.html/2021/drivers.html";
    private static final String CONSTRUCTOR_STANDINGS_URL = "https://www.formula1.com/en/results.html/2021/team.html";
    private static final String SPORTSURGE_GROUP_13 = "https://api.sportsurge.net/events/list?group=13";
    private static final String SPORTSURGE_GROUP = "https://api.sportsurge.net/events/list";
    private static final String SPORTSURGE_STREAMS = "https://api.sportsurge.net/streams/list?event=";

    private final CalendarRepository calendarRepository;
    private final DriverStandingsRepository driverStandingsRepository;
    private final ConstructorStandingsRepository constructorStandingsRepository;
    private final DriverRepository driverRepository;
    private final ConstructorRepository constructorRepository;
    private final SportSurgeStreamRepository sportSurgeStreamRepository;
    private final SportSurgeEventRepository sportSurgeEventRepository;

    @Override
    public void initialize() throws Exception {
        refreshCalendar();
        initializeStandings();
    }

    @Override
    public void refreshCalendar() throws Exception {
        System.setProperty("net.fortuna.ical4j.timezone.cache.impl", MapTimeZoneCache.class.getName());
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
    public List<DriverStanding> initializeStandings() throws IOException {
        List<DriverStanding> driverStandings = initializeDriverStandings();
        List<ConstructorStanding> constructorStandings = initializeConstructorStandings();
        return driverStandings;
    }
    private List<DriverStanding> initializeDriverStandings() throws IOException {
        List<DriverStanding> driverStandings = new ArrayList<>();
        Document doc = Jsoup.connect(DRIVER_STANDINGS_URL).get();
        Elements tBody = doc.select("tbody");
        Elements tRows = tBody.select("tr");
        tRows.forEach(row -> {
            DriverStanding driverStanding = createNewDriverStandingFromRow(row);
            driverStandings.add(driverStanding);
        });
        enrichDriverListWithId(driverStandings);
        driverStandingsRepository.deleteAll();
        driverStandingsRepository.saveAll(driverStandings);
        return driverStandings;
    }

    private void enrichDriverListWithId(List<DriverStanding> driverStandings) {
        List<Driver> drivers = driverRepository.findAll();
        for(DriverStanding driverStanding: driverStandings){
            Optional<Driver> foundDriver = drivers.stream().filter(x -> driverStanding.getName().equals(x.getFullName()))
                    .findFirst();
            if(foundDriver.isPresent()){
                driverStanding.setId(foundDriver.get().getId());
            } else{
                Driver newDriver = Driver.builder().firstName(driverStanding.getFirstName())
                        .lastName(driverStanding.getLastName()).fullName(driverStanding.getName())
                        .build();
               newDriver = driverRepository.save(newDriver);
                driverStanding.setId(newDriver.getId());
            }
        }
    }
    private void enrichConstructorListWithId(List<ConstructorStanding> constructorStandings) {
        List<Constructor> constructors = constructorRepository.findAll();
        for(ConstructorStanding constructorStanding: constructorStandings){
            Optional<Constructor> found = constructors.stream().filter(x -> constructorStanding.getName().equals(x.getName()))
                    .findFirst();
            if(found.isPresent()){
                constructorStanding.setId(found.get().getId());
            } else{
                Constructor newConstructor = Constructor.builder().name(constructorStanding.getName()).build();
                newConstructor = constructorRepository.save(newConstructor);
                constructorStanding.setId(newConstructor.getId());
            }
        }
    }

    private List<ConstructorStanding> initializeConstructorStandings() throws IOException {
        List<ConstructorStanding> constructorStandings = new ArrayList<>();
        Document doc = Jsoup.connect(CONSTRUCTOR_STANDINGS_URL).get();
        Elements tBody = doc.select("tbody");
        Elements tRows = tBody.select("tr");
        tRows.forEach(row -> {
            ConstructorStanding constructorStanding = createNewConstructorStandingFromRow(row);
            constructorStandings.add(constructorStanding);
        });
        enrichConstructorListWithId(constructorStandings);
        constructorStandingsRepository.deleteAll();
        constructorStandingsRepository.saveAll(constructorStandings);
        return constructorStandings;
    }

    @Override
    public void fetchSportSurgeLinks() throws IOException {
        WebClient client = new WebClient();
        Page page = client.getPage(SPORTSURGE_GROUP_13);
        WebResponse response = page.getWebResponse();
        SportSurge sportSurge = new ObjectMapper().readValue(response.getContentAsString(), SportSurge.class);
        List<SportSurgeEvent> events = sportSurge.getEvents();
        List<SportSurgeStream> streams = new ArrayList<>();
        for(SportSurgeEvent event: events){
            page = client.getPage(SPORTSURGE_STREAMS + event.getId());
            response = page.getWebResponse();
            sportSurge = new ObjectMapper().readValue(response.getContentAsString(), SportSurge.class);
            streams.addAll(sportSurge.getStreams());
        }
        sportSurgeStreamRepository.saveAll(streams);
        sportSurgeEventRepository.saveAll(events);
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
