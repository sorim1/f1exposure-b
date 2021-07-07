package sorim.f1.slasher.relentless.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sorim.f1.slasher.relentless.configuration.MainProperties;
import sorim.f1.slasher.relentless.entities.*;
import sorim.f1.slasher.relentless.handling.ExceptionHandling;
import sorim.f1.slasher.relentless.model.ExposedChart;
import sorim.f1.slasher.relentless.model.ExposureResponse;
import sorim.f1.slasher.relentless.model.enums.ExposureStatusEnum;
import sorim.f1.slasher.relentless.repository.*;
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
    private final PropertiesRepository propertiesRepository;
    private static boolean exposureToday = false;
    private static boolean exposureNow = false;
    private static LocalDateTime exposureTime;
    private static String title = "Default";
    private static Integer currentRound = 0;
    private static String nextRaceId = "2021-default-next";
    @Override
    public Boolean exposeDrivers(String[] exposedList, String ipAddress) throws Exception {
        boolean alreadyExists = exposedVoteRepository.existsExposedVoteByIpAddressAndSeasonAndRound(ipAddress, properties.getCurrentYear(), currentRound);
        if(alreadyExists){
            ExceptionHandling.logException("IP_CHEATER", ipAddress);
           // return false;
        }
        exposedVoteRepository.save(ExposedVote.builder().drivers(exposedList)
                .ipAddress(ipAddress)
                .season(properties.getCurrentYear())
                .round(currentRound)
                .build());
        for (String driverCode : exposedList) {
            Integer counter = exposedRepository.incrementExposed(properties.getCurrentYear(), currentRound, driverCode);
            if(counter==0){
                exposedRepository.saveExposureData(properties.getCurrentYear(), currentRound, driverCode, 1);
            }
        }
        return !alreadyExists;
    }

     @Override
    public ExposedChart getExposedChartData() {
        List<String> drivers = new ArrayList<>();
        List<String> driverNames = new ArrayList<>();
        List<Integer> results = new ArrayList<>();
        List<Exposed> list = exposedRepository.findBySeasonAndRoundOrderByCounterDesc(properties.getCurrentYear(), currentRound);
        list.stream().forEach((exposed) -> {
            drivers.add(exposed.getDriver().getCode());
            driverNames.add(exposed.getDriver().getFullName());
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
            log.info("exposureToday: {}", exposureToday);
        } else {
            exposureToday=true;
            title = f1calendar.getLocation();
            currentRound = Integer.valueOf(propertiesRepository.findDistinctFirstByName("round").getValue())+1;
            propertiesRepository.updateProperty("round", currentRound.toString());
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

    @Override
    public void setNextRoundOfExposure(List<DriverStanding> driverStandings, int round) {
        driverStandings.forEach(standing->{
            exposedRepository.saveExposureData(properties.getCurrentYear(), currentRound+1, standing.getCode(), 0);
        });
    }


}
