package sorim.f1.slasher.relentless.service.impl;

import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sorim.f1.slasher.relentless.configuration.MainProperties;
import sorim.f1.slasher.relentless.entities.*;
import sorim.f1.slasher.relentless.handling.Logger;
import sorim.f1.slasher.relentless.model.*;

import sorim.f1.slasher.relentless.repository.*;
import sorim.f1.slasher.relentless.service.ClientService;
import sorim.f1.slasher.relentless.service.ExposureService;
import sorim.f1.slasher.relentless.service.InstagramService;
import sorim.f1.slasher.relentless.service.TwitterService;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClientServiceImpl implements ClientService {

    private final CalendarRepository calendarRepository;
    private final DriverStandingsRepository driverStandingsRepository;
    private final ConstructorStandingsRepository constructorStandingsRepository;
    private final DriverStandingsByRoundRepository driverStandingsByRoundRepository;
    private final ConstructorStandingsByRoundRepository constructorStandingsByRoundRepository;

    private final SportSurgeEventRepository sportSurgeEventRepository;
    private final F1CommentRepository f1CommentRepository;
    private final InstagramService instagramService;
    private final TwitterService twitterService;
    private final ExposureService exposureService;
    private final MainProperties properties;

    @Override
    public Boolean exposeDrivers(String[] exposedList, String ipAddress) throws Exception {
        return exposureService.exposeDrivers(exposedList, ipAddress);
    }

    @Override
    public ExposureData getExposedChartData() {
        return ExposureData.builder()
                .title(exposureService.getTitle())
                .activeExposureChart(exposureService.getExposedChartData())
                .exposureChampionshipData(exposureService.getExposureChampionshipData())
                .standings(exposureService.getExposureStandings())
                .standingsLegacy(exposureService.getExposureStandingsLegacy())
                .build();
    }

    @Override
    public CalendarData getCountdownData(Integer mode) {
        ZonedDateTime gmtZoned = ZonedDateTime.now(ZoneId.of("Europe/London"));
        LocalDateTime gmtDateTime = gmtZoned.toLocalDateTime();
        F1Calendar f1calendar = calendarRepository.findFirstByRaceAfterOrderByRace(gmtDateTime);
        Map<String, Integer> countdownData = getRemainingTime(gmtDateTime, f1calendar, mode);
        return CalendarData.builder().f1Calendar(f1calendar).countdownData(countdownData).build();
    }

    @Override
    public List<DriverStanding> getDriverStandings() {
        return driverStandingsRepository.findAllByOrderByPositionAsc();
    }

    @Override
    public List<ConstructorStanding> getConstructorStandings() {
        return constructorStandingsRepository.findAllByOrderByPositionAsc();
    }

    private List<List<ChartSeries>> getDriverStandingsByRound() {
        List<DriverStandingByRound> standingsBySeason = driverStandingsByRoundRepository.findAllByIdSeasonOrderByIdRoundAscNameAsc(properties.getCurrentYear());
        Map<String, ChartSeries> totalPoints = new TreeMap<>();
        Map<String, ChartSeries> roundPoints = new TreeMap<>();
        Map<String, ChartSeries> roundResults = new TreeMap<>();
        standingsBySeason.forEach(standing->{
            if(!totalPoints.containsKey(standing.getCode())){
                totalPoints.put(standing.getCode(), ChartSeries.builder()
                        .name(standing.getCode())
                        .color(standing.getColor())
                        .series(new ArrayList<>()).build());
                roundPoints.put(standing.getCode(), ChartSeries.builder()
                        .name(standing.getCode())
                        .color(standing.getColor())
                        .series(new ArrayList<>()).build());
                roundResults.put(standing.getCode(), ChartSeries.builder()
                        .name(standing.getCode())
                        .color(standing.getColor())
                        .series(new ArrayList<>()).build());
            }
            totalPoints.get(standing.getCode()).add(standing.getId().getRound(), standing.getPoints());
            roundPoints.get(standing.getCode()).add(standing.getId().getRound(), standing.getPointsThisRound());
            roundResults.get(standing.getCode()).add(standing.getId().getRound(), standing.getResultThisRound());
        });
        List<List<ChartSeries>> output = new ArrayList<>();
        output.add(new ArrayList<>(totalPoints.values()));
        output.add(new ArrayList<>(roundPoints.values()));
        output.add(new ArrayList<>(roundResults.values()));
        return output;
    }

    public List<List<ChartSeries>> getConstructorStandingsByRound() {
        List<ConstructorStandingByRound> standingsBySeason = constructorStandingsByRoundRepository.findAllByIdSeasonOrderByIdRoundAscNameAsc(properties.getCurrentYear());
        Map<String, ChartSeries> totalPoints = new TreeMap<>();
        Map<String, ChartSeries> roundPoints = new TreeMap<>();
        standingsBySeason.forEach(standing->{
            if(!totalPoints.containsKey(standing.getId().getId())){
                totalPoints.put(standing.getId().getId(), ChartSeries.builder()
                        .name(standing.getName())
                        .color(standing.getColor())
                        .series(new ArrayList<>()).build());
                roundPoints.put(standing.getId().getId(), ChartSeries.builder()
                        .name(standing.getName())
                        .color(standing.getColor())
                        .series(new ArrayList<>()).build());
            }
            roundPoints.get(standing.getId().getId()).add(standing.getId().getRound(), standing.getPointsThisRound());
            totalPoints.get(standing.getId().getId()).add(standing.getId().getRound(), standing.getPoints());
        });
        List<List<ChartSeries>> output = new ArrayList<>();
        output.add(new ArrayList<>(totalPoints.values()));
        output.add(new ArrayList<>(roundPoints.values()));
        return output;
    }

    @Override
    public ExposureResponse getExposureDriverList() {
        return exposureService.getExposureDriverList();
    }


    @Override
    public AllStandings getStandings() {
        List<List<ChartSeries>> driverSeries = getDriverStandingsByRound();
        List<List<ChartSeries>> constructorSeries = getConstructorStandingsByRound();
        return AllStandings.builder()
                .driverStandings(getDriverStandings())
                .constructorStandings(getConstructorStandings())
                .driverStandingByRound(driverSeries.get(0))
                .driverPointsByRound(driverSeries.get(1))
                .driverResultByRound(driverSeries.get(2))
                .constructorStandingByRound(constructorSeries.get(0))
                .constructorPointsByRound(constructorSeries.get(1))
                .build();
    }

    @Override
    public List<SportSurgeEvent> getSportSurge() {
        return sportSurgeEventRepository.findAllByOrderByIdDesc();
    }

    @Override
    public List<F1Comment> postComment(F1Comment comment) {
        comment.setTimestamp(new Date());
        if(comment.getComment().length()>900){
            comment.setComment(comment.getComment().substring(0,900));
        }
        f1CommentRepository.save(comment);
        return f1CommentRepository.findFirst30ByPageOrderByTimestampDesc(comment.getPage());
    }

    @Override
    public List<F1Comment> getComments(String page) {
        return f1CommentRepository.findFirst30ByPageOrderByTimestampDesc(Integer.valueOf(page));
    }

    @Override
    public List<InstagramPost> fetchInstagramFeed() throws IGLoginException {
        return instagramService.fetchInstagramFeed();
    }

    @Override
    public TripleInstagramFeed getInstagramFeed() throws IGLoginException {
        return instagramService.getInstagramFeed();
    }

    @Override
    public TripleInstagramFeed getInstagramFeedPage(Integer page) throws IGLoginException {
        return instagramService.getInstagramFeedPage(page);
    }

    @Override
    public DoubleTwitterFeed getTwitterPosts(Integer page) {
        return new DoubleTwitterFeed(twitterService.getTwitterPosts(page));
    }

    @Override
    public List<TwitterPost> fetchTwitterPosts() throws Exception {
        return twitterService.fetchTwitterPosts();
    }

    @Override
    public byte[] getImage(String code) {
        return instagramService.getImage(code);
    }


    private Map<String, Integer> getRemainingTime(LocalDateTime gmtDateTime, F1Calendar f1calendar, Integer mode) {
        Map<String, Integer> output = new HashMap<>();
        Duration duration;
        if(mode==0 || mode==1){
            duration = Duration.between(gmtDateTime, f1calendar.getPractice1());
            output.put("FP1Days", (int) duration.toDays());
            output.put("FP1Seconds", (int) duration.toSeconds());
        }
        if(mode==0 || mode==2){
            duration = Duration.between(gmtDateTime, f1calendar.getPractice2());
            output.put("FP2Days", (int) duration.toDays());
            output.put("FP2Seconds", (int) duration.toSeconds());
        }
        if(mode==0 || mode==3){
            duration = Duration.between(gmtDateTime, f1calendar.getPractice3());
            output.put("FP3Days", (int) duration.toDays());
            output.put("FP3Seconds", (int) duration.toSeconds());
        }
        if(mode==0 || mode==4){
            duration = Duration.between(gmtDateTime, f1calendar.getQualifying());
            output.put("qualifyingDays", (int) duration.toDays());
            output.put("qualifyingSeconds", (int) duration.toSeconds());
        }
        if(mode==0 || mode==5){
            duration = Duration.between(gmtDateTime, f1calendar.getRace());
            output.put("raceDays", (int) duration.toDays());
            output.put("raceSeconds", (int) duration.toSeconds());
        }
        return output;
    }
}
