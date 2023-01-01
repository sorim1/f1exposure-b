package sorim.f1.slasher.relentless.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sorim.f1.slasher.relentless.entities.Driver;
import sorim.f1.slasher.relentless.entities.F1Calendar;
import sorim.f1.slasher.relentless.entities.JsonRepositoryTwoModel;
import sorim.f1.slasher.relentless.model.strawpoll.*;
import sorim.f1.slasher.relentless.repository.CalendarRepository;
import sorim.f1.slasher.relentless.repository.DriverRepository;
import sorim.f1.slasher.relentless.repository.JsonRepository;
import sorim.f1.slasher.relentless.repository.JsonRepositoryTwo;
import sorim.f1.slasher.relentless.service.StrawpollService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StrawpollServiceImpl implements StrawpollService {
    private static final String strawPollApiV3 = "https://api.strawpoll.com/v3/polls/";
    private final DriverRepository driverRepository;
    private final CalendarRepository calendarRepository;
    private final JsonRepositoryTwo jsonRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();
    @Override
    public StrawpollModelThree generateStrawpoll() {
        List<StrawpollPollOption> pollOptions = generateStrawpollDrivers();
        F1Calendar f1calendar = getUpcomingRace();
        StrawpollPollMeta meta = generateStrawpollMeta(f1calendar);
        StrawpollPollConfig config = generateStrawpollConfig();
        String gpName = getRaceName(f1calendar);
        StrawpollPoll poll = StrawpollPoll.builder().poll_options(pollOptions)
                .poll_meta(meta).poll_config(config)
                .title("" + f1calendar.getRace().getYear() + " " + gpName + " - official /EXPOSED/ poll")
                .build();
        StrawpollModelThree fullStrawpoll = StrawpollModelThree.builder().poll(poll)
                .driverCount(pollOptions.size()).build();
        saveStrawpoll(fullStrawpoll);
        return fullStrawpoll;
    }

    private StrawpollPollMeta generateStrawpollMeta(F1Calendar f1calendar) {
        return StrawpollPollMeta.builder().location(f1calendar.getLocation()).build();
    }
    private StrawpollPollConfig generateStrawpollConfig() {
        return StrawpollPollConfig.builder()
                .is_multiple_choice(true)
                .is_private(true)
                .build();
    }
    private String getRaceName(F1Calendar f1calendar){
        String gpName = f1calendar.getErgastName();
        if(gpName==null){
            gpName = f1calendar.getLocation();
        }
        return gpName;
    }

    private F1Calendar getUpcomingRace() {
        ZonedDateTime gmtZoned = ZonedDateTime.now(ZoneId.of("Europe/London"));
        LocalDateTime gmtDateTime = gmtZoned.toLocalDateTime();
        F1Calendar f1calendar = calendarRepository.findFirstByRaceAfterOrderByRace(gmtDateTime);
        if(f1calendar==null){
            //TODO ovo se ne bi smjelo dogodit za pravi poll
            f1calendar = calendarRepository.findFirstByRaceBeforeOrderByRaceDesc(gmtDateTime);
        }
        return f1calendar;
           }

    @Override
    public List<StrawpollPollOption> getStrawpollDrivers() {
        return null;
    }
    @Override
    public List<StrawpollPollOption> generateStrawpollDrivers() {
        List<Driver> drivers = driverRepository.findAllByStatusOrderByFullName(1);
        List<StrawpollPollOption> pollOptions = new ArrayList<>();
        drivers.forEach(driver -> {
            pollOptions.add(new StrawpollPollOption(driver));
        });
        return pollOptions;
    }
    @Override
    public String setStrawpollDrivers(String drivers) {
        return null;
    }

    @Override
    public StrawpollModelThree getStrawpoll() {
        return null;
    }

    @Override
    public StrawpollModelThree saveStrawpoll(StrawpollModelThree object) {
        JsonRepositoryTwoModel data = JsonRepositoryTwoModel.builder().id("STRAWPOLL_CURRENT").json(object)
                .build();
        jsonRepository.save(data);
        return (StrawpollModelThree) data.getJson();
    }

    @Override
    public StrawpollPoll postStrawpoll() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-API-Key", "ec64bbae-7323-11ed-ab5a-9556bc37aaf0");
        JsonRepositoryTwoModel data = jsonRepository.findAllById("STRAWPOLL_CURRENT");
        StrawpollModelThree pollFromDatabase =  objectMapper.convertValue(data.getJson(), StrawpollModelThree.class);
        Long currentUnixSeconds = Instant.now().getEpochSecond();
        Long inThreeHours = currentUnixSeconds + 10800;
        pollFromDatabase.getPoll().getPoll_config().setDeadline_at(inThreeHours);
        HttpEntity entity = new HttpEntity(pollFromDatabase.getPoll(), headers);
        ResponseEntity<StrawpollPoll> response = restTemplate.exchange(
                strawPollApiV3, HttpMethod.POST, entity, StrawpollPoll.class);
        return response.getBody();
    }
}
