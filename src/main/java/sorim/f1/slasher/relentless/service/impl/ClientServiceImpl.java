package sorim.f1.slasher.relentless.service.impl;

import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sorim.f1.slasher.relentless.entities.*;
import sorim.f1.slasher.relentless.handling.ExceptionHandling;
import sorim.f1.slasher.relentless.model.*;

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
public class ClientServiceImpl implements ClientService {

    private final CalendarRepository calendarRepository;
    private final DriverStandingsRepository driverStandingsRepository;
    private final ConstructorStandingsRepository constructorStandingsRepository;
    private final SportSurgeEventRepository sportSurgeEventRepository;
    private final F1CommentRepository f1CommentRepository;
    private final InstagramService instagramService;
    private final ExposureService exposureService;

    @Override
    public Boolean exposeDrivers(String[] exposedList, String ipAddress) throws Exception {
        return exposureService.exposeDrivers(exposedList, ipAddress);
    }

    @Override
    public void validateHeader(String authorization) throws Exception {
        if (!"md123".equals(authorization)) {
            ExceptionHandling.raiseException("not authorized");
        }
    }

    @Override
    public void checkHeader(String authorization) {
        log.info("ovo je authorization: {}", authorization);
    }

    @Override
    public String validateIp(HttpServletRequest request) {
        log.info(request.getRemoteAddr());
        log.info(request.getRequestURI());
        log.info(request.getLocalAddr());
        return request.getRemoteAddr();
    }

    @Override
    public ExposedChart getExposedChartData() {

        return exposureService.getExposedChartData();
    }

    @Override
    public CalendarData getCountdownData() {
        ZonedDateTime gmtZoned = ZonedDateTime.now(ZoneId.of("Europe/London"));
        LocalDateTime gmtDateTime = gmtZoned.toLocalDateTime();
        F1Calendar f1calendar = calendarRepository.findFirstByRaceAfterOrderByRace(gmtDateTime);
        Map<String, Integer> countdownData = getRemainingTime(gmtDateTime, f1calendar);
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
    public ExposureResponse getExposureDriverList() {
        return exposureService.getExposureDriverList();
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
        return sportSurgeEventRepository.findAllByOrderByIdDesc();
    }

    @Override
    public List<F1Comment> postComment(F1Comment comment) {
        comment.setTimestamp(new Date());
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
    public byte[] getImage(String code) {
        return instagramService.getImage(code);
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
