package sorim.f1.slasher.relentless.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sorim.f1.slasher.relentless.configuration.MainProperties;
import sorim.f1.slasher.relentless.entities.*;
import sorim.f1.slasher.relentless.handling.ExceptionHandling;
import sorim.f1.slasher.relentless.model.*;
import sorim.f1.slasher.relentless.model.enums.ExposureModeEnum;
import sorim.f1.slasher.relentless.model.enums.ExposureStatusEnum;
import sorim.f1.slasher.relentless.repository.*;
import sorim.f1.slasher.relentless.service.ExposureService;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
public class ExposureServiceImpl implements ExposureService {

    private final ExposedVoteRepository exposedVoteRepository;
    private final ExposedRepository exposedRepository;
    private final ExposureChampionshipRepository exposureChampionshipRepository;
    private final ExposureChampionshipStandingsRepository exposureChampionshipStandingsRepository;
    private final DriverRepository driverRepository;
    private final CalendarRepository calendarRepository;
    private final MainProperties properties;
    private final PropertiesRepository propertiesRepository;
    private static boolean exposureToday = false;
    private static boolean exposureNow = false;
    private static LocalDateTime exposureTime;
    private static String title = "Default";
    private static Integer currentRound = 3;

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
        List<BigDecimal> exposureLegacyList = new ArrayList<>();
        List<Exposed> list = exposedRepository.findBySeasonAndRoundOrderByCounterDesc(properties.getCurrentYear(), currentRound);
        ExposedVoteTotals total = exposedRepository.findExposedTotalBySeasonAndRound(properties.getCurrentYear(), currentRound);
         list.forEach((exposed) -> {
             drivers.add(exposed.getDriver().getCode());
             driverNames.add(exposed.getDriver().getFullName());
             results.add(exposed.getCounter());
             BigDecimal exposure = new BigDecimal(exposed.getCounter()*100).divide(new BigDecimal(total.getVoters()), 2, RoundingMode.HALF_UP);
             BigDecimal exposureLegacy = new BigDecimal(exposed.getCounter()*100).divide(new BigDecimal(total.getVotes()), 2, RoundingMode.HALF_UP);
             exposureList.add(exposure);
             exposureLegacyList.add(exposureLegacy);
         });
         return ActiveExposureChart.builder()
                .drivers(drivers.toArray(new String[drivers.size()]))
                .driverNames(driverNames.toArray(new String[driverNames.size()]))
                .results(results.toArray(new Integer[results.size()]))
                 .exposure(exposureList.toArray(new BigDecimal[results.size()]))
                 .exposureLegacy(exposureLegacyList.toArray(new BigDecimal[results.size()]))
                 .round(currentRound)
                 .season(properties.getCurrentYear())
                 .votes(total.getVotes())
                 .voters(total.getVoters())
                 .build();
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
            currentRound = Integer.parseInt(propertiesRepository.findDistinctFirstByName("round").getValue())+1;
            propertiesRepository.updateProperty("round", currentRound.toString());
            exposureTime = LocalDateTime.now().plus(duration).plusHours(1);
        }

        return true;
    }


    @Override
    public boolean closeExposurePoll() {
        exposureToday=false;
        exposureNow=false;
        ActiveExposureChart exposedData = getExposedChartData();
        List<ExposureChampionship> list = new ArrayList<>();
        for(int i = 0; i<exposedData.getDrivers().length; i++){
            ExposureChampionshipId idModern = ExposureChampionshipId.builder()
                    .season(properties.getCurrentYear())
                    .round(currentRound)
                    .driver(exposedData.getDrivers()[i])
                    .mode(ExposureModeEnum.Modern).build();
            list.add(ExposureChampionship.builder()
                    .id(idModern)
                    .exposure(exposedData.getExposure()[i])
                    .build());

            ExposureChampionshipId idLegacy = ExposureChampionshipId.builder()
                    .season(properties.getCurrentYear())
                    .round(currentRound)
                    .driver(exposedData.getDrivers()[i])
                    .mode(ExposureModeEnum.Legacy).build();
            list.add(ExposureChampionship.builder()
                    .id(idLegacy)
                    .exposure(exposedData.getExposureLegacy()[i])
                    .build());
        }
        exposureChampionshipRepository.saveAll(list);
        List<ExposureChampionshipData> exposureChampionshipData = getExposureChampionshipData();
        List<ExposureChampionshipStanding> exposureStandings = new ArrayList<>();
        exposureChampionshipData.forEach((v)->{
            ExposureChampionshipStandingId id = ExposureChampionshipStandingId.builder()
                    .season(properties.getCurrentYear())
                    .driver(v.getCode())
                    .mode(ExposureModeEnum.Modern).build();
            exposureStandings.add(ExposureChampionshipStanding.builder().id(id).exposure(v.getScore()).build());
            id = ExposureChampionshipStandingId.builder()
                    .season(properties.getCurrentYear())
                    .driver(v.getCode())
                    .mode(ExposureModeEnum.Legacy).build();
            exposureStandings.add(ExposureChampionshipStanding.builder().id(id).exposure(v.getScoreLegacy()).build());
        });
        exposureChampionshipStandingsRepository.saveAll(exposureStandings);
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

    @Override
    public List<ExposureChampionshipData>  getExposureChampionshipData() {
        List<ExposureChampionship> rawData = exposureChampionshipRepository.findAllByIdSeasonOrderByIdRoundAsc(properties.getCurrentYear());
        Map<String, ExposureChampionshipData> map = new HashMap<>();
        List<ExposureChampionshipData> response = new ArrayList<>();
        rawData.forEach(row->{
            if (map.containsKey(row.getId().getDriver())){
                ExposureChampionshipData data = map.get(row.getId().getDriver());
                List<BigDecimal> newResult = new ArrayList<>();
                newResult.add(BigDecimal.valueOf(row.getId().getRound()));
                newResult.add(row.getExposure());

                if(row.getId().getMode().equals(ExposureModeEnum.Modern)){
                    BigDecimal newScore = data.getScore().add(row.getExposure());
                    List<BigDecimal> newStanding = new ArrayList<>();
                    newStanding.add(BigDecimal.valueOf(row.getId().getRound()));
                    newStanding.add(newScore);
                    data.getScoresByRound().add(newResult);
                    data.getScoresThroughRounds().add(newStanding);
                    data.setScore(newScore);
                }
                if(row.getId().getMode().equals(ExposureModeEnum.Legacy)){
                    BigDecimal newScore = data.getScoreLegacy().add(row.getExposure());
                    List<BigDecimal> newStanding = new ArrayList<>();
                    newStanding.add(BigDecimal.valueOf(row.getId().getRound()));
                    newStanding.add(newScore);
                    data.getScoresByRoundLegacy().add(newResult);
                    data.getScoresThroughRoundsLegacy().add(newStanding);
                    data.setScoreLegacy(newScore);
                }
            } else{
                ExposureChampionshipData data = new ExposureChampionshipData();
                data.setCode(row.getId().getDriver());
                List<BigDecimal> newResult = new ArrayList<>();
                newResult.add(BigDecimal.valueOf(row.getId().getRound()));
                newResult.add(row.getExposure());
                if(row.getId().getMode().equals(ExposureModeEnum.Modern)){
                    data.getScoresByRound().add(newResult);
                    data.getScoresThroughRounds().add(newResult);
                    data.setScore(row.getExposure());

                }
                if(row.getId().getMode().equals(ExposureModeEnum.Legacy)){
                    data.getScoresByRoundLegacy().add(newResult);
                    data.getScoresThroughRoundsLegacy().add(newResult);
                    data.setScoreLegacy(row.getExposure());
                }
                map.put(row.getId().getDriver(), data);
            }
        });
        map.forEach((key, value)->{
            response.add(value);
        });
        return response;
    }

    @Override
    public List<ExposureChampionshipStanding> getExposureStandings() {
        return exposureChampionshipStandingsRepository.findAllByIdSeasonAndIdModeOrderByExposureDesc(properties.getCurrentYear(), ExposureModeEnum.Modern);
    }

    @Override
    public List<ExposureChampionshipStanding> getExposureStandingsLegacy() {
        return exposureChampionshipStandingsRepository.findAllByIdSeasonAndIdModeOrderByExposureDesc(properties.getCurrentYear(), ExposureModeEnum.Legacy);

    }

}
