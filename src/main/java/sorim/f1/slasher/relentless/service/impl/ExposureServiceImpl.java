package sorim.f1.slasher.relentless.service.impl;

import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sorim.f1.slasher.relentless.entities.*;
import sorim.f1.slasher.relentless.handling.ExceptionHandling;
import sorim.f1.slasher.relentless.model.*;
import sorim.f1.slasher.relentless.model.enums.ExposureStatusEnum;
import sorim.f1.slasher.relentless.repository.*;
import sorim.f1.slasher.relentless.service.ClientService;
import sorim.f1.slasher.relentless.service.ExposureService;
import sorim.f1.slasher.relentless.service.InstagramService;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExposureServiceImpl implements ExposureService {

    private final static Integer raceId = 1;
    private final ExposedVoteRepository exposedVoteRepository;
    private final ExposedRepository exposedRepository;
    private final DriverRepository driverRepository;
    private final CalendarRepository calendarRepository;
    private static boolean exposureToday = false;
    private static boolean exposureNow = false;
    private static LocalDateTime exposureTime;
    private static String title;
    @Override
    public Boolean exposeDrivers(String[] exposedList, String ipAddress) {
        boolean alreadyExists = exposedVoteRepository.existsExposedVoteByIpAddress(ipAddress);
        exposedVoteRepository.save(ExposedVote.builder().drivers(exposedList).ipAddress(ipAddress).build());
        for (String s : exposedList) {
            Integer counter = exposedRepository.incrementExposed(raceId, s);
            if(counter==0){
                exposedRepository.saveExposureData(raceId, s);
            }
        }
        return !alreadyExists;
    }

     @Override
    public ExposedChart getExposedChartData() {
        List<String> drivers = new ArrayList<>();
        List<String> driverNames = new ArrayList<>();
        List<Integer> results = new ArrayList<>();

        List<Exposed> list = exposedRepository.findByRaceIdOrderByCounterDesc(raceId);
        list.stream().forEach((exposed) -> {
            drivers.add(exposed.getDriver().getCode());
            driverNames.add(exposed.getDriver().getLastName());
            results.add(exposed.getCounter());
        });
        return ExposedChart.builder()
                .drivers(drivers.toArray(new String[drivers.size()]))
                .driverNames(driverNames.toArray(new String[driverNames.size()]))
                .results(results.toArray(new Integer[results.size()])).build();
    }

    @Override
    public ExposureResponse getExposureDriverList() {
        List<Driver> drivers = driverRepository.findAll();
        return ExposureResponse.builder().drivers(drivers)
                .status(ExposureStatusEnum.ACTIVE).build();
    }

    @Override
    public boolean setExposureStartTime() {
        ZonedDateTime gmtZoned = ZonedDateTime.now(ZoneId.of("Europe/London"));
        LocalDateTime gmtDateTime = gmtZoned.toLocalDateTime();
        F1Calendar f1calendar = calendarRepository.findFirstByRaceAfterOrderByRace(gmtDateTime);
        Duration duration = Duration.between(gmtDateTime, f1calendar.getRace());
        log.info("duration1");
        log.info(String.valueOf(duration));
        if(duration.toDays()>0){
            exposureToday=false;
            log.info(String.valueOf(exposureToday));
        } else {
            exposureToday=true;
            title = f1calendar.getLocation();
            exposureTime = LocalDateTime.now().plus(duration).plusHours(1);
        }

        return true;
    }

    @Override
    public boolean setExposureCloseTime() {
        exposureToday=false;
        exposureNow=false;
        return true;
    }

    @Override
    public boolean exposureOn() {
        if(exposureNow){
            return true;
        }
        if(!exposureToday){
            return false;
        }
        if(LocalDateTime.now().isAfter(exposureTime)){
            exposureNow=true;
            return true;
        }

        return false;
    }


}
