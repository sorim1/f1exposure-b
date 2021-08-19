package sorim.f1.slasher.relentless.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sorim.f1.slasher.relentless.configuration.MainProperties;
import sorim.f1.slasher.relentless.entities.*;
import sorim.f1.slasher.relentless.handling.Logger;
import sorim.f1.slasher.relentless.model.*;
import sorim.f1.slasher.relentless.model.enums.ExposureStatusEnum;
import sorim.f1.slasher.relentless.repository.*;
import sorim.f1.slasher.relentless.service.ExposureService;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExposureServiceImpl implements ExposureService {

    private final ExposedVoteRepository exposedVoteRepository;
    private final ExposedRepository exposedRepository;
    private final ExposureChampionshipRepository exposureChampionshipRepository;
    private final ExposureChampionshipStandingsRepository exposureChampionshipStandingsRepository;
    private final DriverStandingsByRoundRepository driverStandingsByRoundRepository;
    private final DriverRepository driverRepository;
    private final CalendarRepository calendarRepository;
    private final MainProperties properties;
    private final PropertiesRepository propertiesRepository;
    private static boolean exposureToday = false;
    private static boolean exposureNow = false;
    private static LocalDateTime exposureTime;
    private static String title = "Something Went Wrong";
    private static Integer currentRound = 3;

    @Override
    public Boolean exposeDrivers(String[] exposedList, String ipAddress) throws Exception {
        boolean alreadyExists = exposedVoteRepository.existsExposedVoteByIpAddressAndSeasonAndRound(ipAddress, properties.getCurrentYear(), currentRound);
        if(alreadyExists){
            Logger.log("IP_CHEATER- exposeDrivers ", ipAddress + " - " + exposedList);
            //TODO uncomment
        //    return false;
        }
        exposedVoteRepository.save(ExposedVote.builder().drivers(exposedList)
                .ipAddress(ipAddress)
                .season(properties.getCurrentYear())
                .round(currentRound)
                .build());
        for (String driverCode : exposedList) {
            Integer counter = exposedRepository.incrementExposed(properties.getCurrentYear(), currentRound, driverCode);
            if(counter==0){
                exposedRepository.insertExposureData(properties.getCurrentYear(), currentRound, driverCode, 1);
            }
        }
        Integer counter = exposedRepository.incrementTotal(properties.getCurrentYear(), currentRound, exposedList.length);
        if(counter==0){
            exposedRepository.insertExposureTotal(properties.getCurrentYear(), currentRound, exposedList.length);
        }
        return !alreadyExists;
    }

     @Override
    public ActiveExposureChart getExposedChartData() {
        List<String> drivers = new ArrayList<>();
        List<String> driverNames = new ArrayList<>();
        List<Integer> results = new ArrayList<>();
        List<BigDecimal> exposureList = new ArrayList<>();
        List<Exposed> list = exposedRepository.findByIdSeasonAndIdRoundOrderByCounterDesc(properties.getCurrentYear(), currentRound);

         ExposedVoteTotals total = exposedRepository.findExposedTotalBySeasonAndRound(properties.getCurrentYear(), currentRound);
         list.forEach((exposed) -> {
             drivers.add(exposed.getRelatedDriver().getCode());
             driverNames.add(exposed.getRelatedDriver().getFullName());
             results.add(exposed.getCounter());
             BigDecimal exposure = new BigDecimal(exposed.getCounter()*100).divide(new BigDecimal(total.getVoters()), 2, RoundingMode.HALF_UP);
             exposureList.add(exposure);
         });
         return ActiveExposureChart.builder()
                .drivers(drivers.toArray(new String[drivers.size()]))
                .driverNames(driverNames.toArray(new String[driverNames.size()]))
                .results(results.toArray(new Integer[results.size()]))
                 .exposure(exposureList.toArray(new BigDecimal[results.size()]))
                 .round(currentRound)
                 .season(properties.getCurrentYear())
                 .votes(total.getVotes())
                 .voters(total.getVoters())
                 .build();
    }

    @Override
    public ExposureResponse getExposureDriverList() {
        ExposureResponse response = ExposureResponse.builder()
                .title(title)
                .year(properties.getCurrentYear())
                .exposureTime(exposureTime)
                .exposureNow(exposureNow)
                .exposureToday(exposureToday)
                .currentRound(currentRound)
                .build();
        if(exposureOn()) {
           // if(true) {
            List<ExposureDriver> drivers = driverRepository.findAll();
            response.setDrivers(drivers);
            response.setStatus(ExposureStatusEnum.ACTIVE);
        } else {
            if(exposureToday){
                response.setStatus(ExposureStatusEnum.SOON);
            } else {
                response.setStatus(ExposureStatusEnum.OVER);
            }

        }
        return response;
    }

    @Override
    public boolean setExposureStartTime() {
        ZonedDateTime gmtZoned = ZonedDateTime.now(ZoneId.of("Europe/London"));
        LocalDateTime gmtDateTime = gmtZoned.toLocalDateTime();
        F1Calendar f1calendar = calendarRepository.findFirstByRaceAfterOrderByRace(gmtDateTime);
        if(f1calendar!=null){
        Duration duration = Duration.between(gmtDateTime, f1calendar.getRace());
        if(duration.toDays()>0){
            exposureToday=false;
            log.info("exposureToday: {}", exposureToday);
        } else {
            exposureToday=true;
            title = f1calendar.getLocation();
            currentRound = Integer.parseInt(propertiesRepository.findDistinctFirstByName("round").getValue())+1;
            propertiesRepository.updateProperty("round", currentRound.toString());
            exposureTime = LocalDateTime.now().plus(duration).plusHours(1);
        }
        }
        return true;
    }

    @PostConstruct
    public void initializeExposure() {
        ZonedDateTime gmtZoned = ZonedDateTime.now(ZoneId.of("Europe/London"));
        LocalDateTime gmtDateTime = gmtZoned.toLocalDateTime();
        F1Calendar f1calendar = calendarRepository.findFirstByRaceBeforeOrderByRaceDesc(gmtDateTime);
        if(f1calendar!=null){
        title = f1calendar.getLocation();
        currentRound = Integer.parseInt(propertiesRepository.findDistinctFirstByName("round").getValue());
        f1calendar = calendarRepository.findFirstByRaceAfterOrderByRace(gmtDateTime);
        log.info("title2: {}" + title);
        Duration duration = Duration.between(gmtDateTime, f1calendar.getRace());
        exposureTime = LocalDateTime.now().plus(duration).plusHours(1);
        }
    }


    @Override
    public boolean closeExposurePoll() {
        exposureToday=false;
        exposureNow=false;
        ActiveExposureChart exposedData = getExposedChartData();
        List<ExposureChampionship> list = new ArrayList<>();
        for(int i = 0; i<exposedData.getDrivers().length; i++){
            SeasonRoundDriverId idModern = SeasonRoundDriverId.builder()
                    .season(properties.getCurrentYear())
                    .round(currentRound)
                    .driver(exposedData.getDrivers()[i])
                    .build();
            DriverStandingByRound dsbr = driverStandingsByRoundRepository.findFirstByCode(idModern.getDriver());
            String color = null;
            if(dsbr!=null){
                color = dsbr.getColor();
            }
            list.add(ExposureChampionship.builder()
                    .id(idModern)
                    .exposure(exposedData.getExposure()[i])
                    .color(color)
                    .build());
        }
        exposureChampionshipRepository.saveAll(list);
        List<ExposureChampionshipData> exposureChampionshipData = getExposureChampionshipData();
        List<ExposureChampionshipStanding> exposureStandings = new ArrayList<>();
        exposureChampionshipData.forEach((v)->{
            ExposureChampionshipStandingId id = ExposureChampionshipStandingId.builder()
                    .season(properties.getCurrentYear())
                    .driver(v.getCode())
                    .build();
            exposureStandings.add(ExposureChampionshipStanding.builder().id(id).exposure(v.getScore()).build());
        });
        exposureChampionshipStandingsRepository.saveAll(exposureStandings);
        exposureChampionshipStandingsRepository.updateChampionshipNames();

        return true;
    }

    @Override
    public Integer getCurrentRound() {
        return currentRound;
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
            try {
                exposedRepository.insertExposureData(properties.getCurrentYear(), currentRound + 1, standing.getCode(), 0);
            }catch(Exception e){
                Logger.log("setNextRoundOfExposure->insertExposureData- FAIL ", e.getMessage());
            }
        });
    }

    @Override
    public List<ExposureChampionshipData>  getExposureChampionshipData() {
        List<ExposureChampionship> rawData = exposureChampionshipRepository.findAllByIdSeasonOrderByIdRound(properties.getCurrentYear());
        Map<String, ExposureChampionshipData> map = new TreeMap<>();
        rawData.forEach(row->{
            if (map.containsKey(row.getId().getDriver())){
                ExposureChampionshipData data = map.get(row.getId().getDriver());
                List<BigDecimal> newResult = new ArrayList<>();
                newResult.add(BigDecimal.valueOf(row.getId().getRound()));
                newResult.add(row.getExposure());
                    BigDecimal newScore = data.getScore().add(row.getExposure());
                    List<BigDecimal> newStanding = new ArrayList<>();
                    newStanding.add(BigDecimal.valueOf(row.getId().getRound()));
                    newStanding.add(newScore);
                    data.getScoresByRound().add(newResult);
                    data.getScoresThroughRounds().add(newStanding);
                data.updateMaxExposure(row.getId().getRound(), row.getExposure());
                 data.setScore(newScore);

            } else{
                ExposureChampionshipData data = new ExposureChampionshipData();
                data.setCode(row.getId().getDriver());
                data.setColor(row.getColor());
                List<BigDecimal> newResult = new ArrayList<>();
                newResult.add(BigDecimal.valueOf(row.getId().getRound()));
                newResult.add(row.getExposure());
                    data.getScoresByRound().add(newResult);
                    data.getScoresThroughRounds().add(newResult);
                    data.setScore(row.getExposure());
                data.setMaxExposure(row.getExposure());
                data.setMaxExposureRound(row.getId().getRound());
                map.put(row.getId().getDriver(), data);
            }
        });
        return new ArrayList<>(map.values());
    }

    @Override
    public List<ExposureChampionshipStanding> getExposureStandings() {
        return exposureChampionshipStandingsRepository.findAllByIdSeasonOrderByExposureDesc(properties.getCurrentYear());
    }

    @Override
    public List<Integer> getExposureVoters() {
        return exposedRepository.getVoterCountOfSeason(properties.getCurrentYear());
    }

    @Override
    public String getTitle() {
        return title;
    }

}
