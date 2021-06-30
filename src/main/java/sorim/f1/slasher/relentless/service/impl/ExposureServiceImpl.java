package sorim.f1.slasher.relentless.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sorim.f1.slasher.relentless.configuration.MainProperties;
import sorim.f1.slasher.relentless.entities.Driver;
import sorim.f1.slasher.relentless.entities.Exposed;
import sorim.f1.slasher.relentless.entities.ExposedVote;
import sorim.f1.slasher.relentless.entities.F1Calendar;
import sorim.f1.slasher.relentless.handling.ExceptionHandling;
import sorim.f1.slasher.relentless.model.ExposedChart;
import sorim.f1.slasher.relentless.model.ExposureResponse;
import sorim.f1.slasher.relentless.model.enums.ExposureStatusEnum;
import sorim.f1.slasher.relentless.repository.CalendarRepository;
import sorim.f1.slasher.relentless.repository.DriverRepository;
import sorim.f1.slasher.relentless.repository.ExposedRepository;
import sorim.f1.slasher.relentless.repository.ExposedVoteRepository;
import sorim.f1.slasher.relentless.service.ExposureService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExposureServiceImpl implements ExposureService {

    private final ExposedVoteRepository exposedVoteRepository;
    private final ExposedRepository exposedRepository;
    private final DriverRepository driverRepository;
    private final CalendarRepository calendarRepository;
    private final MainProperties properties;
    private static boolean exposureToday = false;
    private static boolean exposureNow = false;
    private static LocalDateTime exposureTime;
    private static String title = "Default";
    private static String raceId = "2021-default";

    @Override
    public Boolean exposeDrivers(String[] exposedList, String ipAddress) throws Exception {
        boolean alreadyExists = exposedVoteRepository.existsExposedVoteByIpAddressAndRaceId(ipAddress, raceId);
        if(alreadyExists){
            ExceptionHandling.logException("IP_CHEATER", ipAddress);
           // return false;
        }
        exposedVoteRepository.save(ExposedVote.builder().drivers(exposedList)
                .ipAddress(ipAddress)
                .raceId(raceId)
                .build());
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
                .status(ExposureStatusEnum.ACTIVE)
                .title(title)
                .year(properties.getCurrentYear().toString()).build();
    }

    @Override
    public boolean setExposureStartTime() {
        ZonedDateTime gmtZoned = ZonedDateTime.now(ZoneId.of("Europe/London"));
        LocalDateTime gmtDateTime = gmtZoned.toLocalDateTime();
        F1Calendar f1calendar = calendarRepository.findFirstByRaceAfterOrderByRace(gmtDateTime);
        Duration duration = Duration.between(gmtDateTime, f1calendar.getRace());
        if(duration.toDays()>0){
            exposureToday=false;
            log.info(String.valueOf(exposureToday));
        } else {
            exposureToday=true;
            title = f1calendar.getLocation();
            raceId = properties.getCurrentYear()+title;
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
