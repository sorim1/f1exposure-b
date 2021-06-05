package sorim.f1.slasher.relentless.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.fortuna.ical4j.util.MapTimeZoneCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sorim.f1.slasher.relentless.entities.*;
import sorim.f1.slasher.relentless.handling.ExceptionHandling;
import sorim.f1.slasher.relentless.model.AllStandings;
import sorim.f1.slasher.relentless.model.CalendarData;
import sorim.f1.slasher.relentless.model.ExposedChart;
import sorim.f1.slasher.relentless.repository.*;
import sorim.f1.slasher.relentless.service.ClientService;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClientServiceImpl implements ClientService {

    private final static Integer raceId = 1;
    private final ExposedVoteRepository exposedVoteRepository;
    private final ExposedRepository exposedRepository;
    private final CalendarRepository calendarRepository;
    private final DriverStandingsRepository driverStandingsRepository;
    private final ConstructorStandingsRepository constructorStandingsRepository;
    private final DriverRepository driverRepository;
    private final SportSurgeEventRepository sportSurgeEventRepository;

    @Override
    public Boolean exposeDrivers(String[] exposedList, String ipAddress) {
        boolean alreadyExists = exposedVoteRepository.existsExposedVoteByIpAddress(ipAddress);
        exposedVoteRepository.save(ExposedVote.builder().drivers(exposedList).ipAddress(ipAddress).build());
        for (String s : exposedList) {
            exposedRepository.incrementExposed(raceId, Integer.valueOf(s));
        }
        return !alreadyExists;
    }

    @Override
    public void validateHeader(String authorization) throws Exception {
        if (!"md123".equals(authorization)) {
            ExceptionHandling.raiseException("not authorized");
        }
    }

    @Override
    public String validateIp(HttpServletRequest request) {
//        log.info(request.getRemoteAddr());
//        log.info(request.getRequestURI());
//        log.info(request.getLocalAddr());
        return request.getRemoteAddr();
    }

    @Override
    public ExposedChart getExposedChartData() {
        List<Integer> drivers = new ArrayList<>();
        List<String> driverNames = new ArrayList<>();
        List<Integer> results = new ArrayList<>();

        List<Exposed> list = exposedRepository.findByRaceIdOrderByCounterDesc(raceId);
        list.stream().forEach((exposed) -> {
            drivers.add(exposed.getDriver().getId());
            driverNames.add(exposed.getDriver().getLastName());
            results.add(exposed.getCounter());
        });
        return ExposedChart.builder()
                .drivers(drivers.toArray(new Integer[drivers.size()]))
                .driverNames(driverNames.toArray(new String[driverNames.size()]))
                .results(results.toArray(new Integer[results.size()])).build();
    }

    @Override
    public CalendarData getCountdownData() {
        System.setProperty("net.fortuna.ical4j.timezone.cache.impl", MapTimeZoneCache.class.getName());
        ZonedDateTime gmtZoned = ZonedDateTime.now(ZoneId.of("Europe/London"));
        LocalDateTime gmtDateTime = gmtZoned.toLocalDateTime();
        log.info(String.valueOf(gmtDateTime));
        F1Calendar f1calendar = calendarRepository.findFirstByRaceAfterOrderByRace(gmtDateTime);
        Map<String, Integer> countdownData = getRemainingTime(gmtDateTime, f1calendar)   ;
        return CalendarData.builder().f1Calendar(f1calendar).countdownData(countdownData).build();
    }

    @Override
    public List<DriverStanding> getDriverStandings() {
        return driverStandingsRepository.findAll();
    }

    @Override
    public List<ConstructorStanding> getConstructorStandings() {
        return constructorStandingsRepository.findAll();
    }

    @Override
    public List<Driver> getExposureDriverList() {
        return driverRepository.findAll();
    }

    @Override
    public AllStandings getStandings() {
        return AllStandings.builder()
                .driverStandings(getDriverStandings())
                .constructorStandings(getConstructorStandings())
                .build();
    }

    @Override
    public List<SportSurgeEvent> getSportSurge() {
        return sportSurgeEventRepository.findAllOrderByIdDesc();
    }

    private Map<String, Integer> getRemainingTime(LocalDateTime gmtDateTime, F1Calendar f1calendar) {
        Map<String, Integer> output = new HashMap<>();
        Duration duration;
        duration = Duration.between(gmtDateTime, f1calendar.getPractice1());
        output.put("FP1Days", (int) duration.toDays());
        output.put("FP1Seconds", (int) duration.toSeconds());
        duration = Duration.between(gmtDateTime, f1calendar.getPractice2());
        output.put("FP2Days", (int) duration.toDays());
        output.put("FP2Seconds", (int) duration.toSeconds());
        duration = Duration.between(gmtDateTime, f1calendar.getPractice3());
        output.put("FP3Days", (int) duration.toDays());
        output.put("FP3Seconds", (int) duration.toSeconds());
        duration = Duration.between(gmtDateTime, f1calendar.getQualifying());
        output.put("qualifyingDays", (int) duration.toDays());
        output.put("qualifyingSeconds", (int) duration.toSeconds());
        duration = Duration.between(gmtDateTime, f1calendar.getRace());
        output.put("raceDays", (int) duration.toDays());
        output.put("raceSeconds", (int) duration.toSeconds());
        return output;
    }
}
