package sorim.f1.slasher.relentless.service.impl;

import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import sorim.f1.slasher.relentless.configuration.MainProperties;
import sorim.f1.slasher.relentless.entities.*;
import sorim.f1.slasher.relentless.model.*;
import sorim.f1.slasher.relentless.repository.*;
import sorim.f1.slasher.relentless.service.*;
import sorim.f1.slasher.relentless.util.MainUtility;

import java.math.BigDecimal;
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
    private final AwsRepository awsRepository;
    private final AwsCommentRepository awsCommentRepository;
    private final InstagramService instagramService;
    private final TwitterService twitterService;
    private final RedditService redditService;
    private final FourchanService forchanService;
    private final ExposureService exposureService;
    private final ErgastService ergastService;
    private final MainProperties properties;
    private final ArtImageRepository artImageRepository;
    private static final String SYSTEM_MESSAGE = "### SYSTEM MESSAGE ###";


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
                .voters(exposureService.getExposureVoters())
                .exposureRaces(ergastService.getRacesSoFar(String.valueOf(properties.getCurrentYear()), exposureService.getCurrentExposureRound()))
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
    public CalendarData getCountdownDataPrevious(Integer mode) {
        ZonedDateTime gmtZoned = ZonedDateTime.now(ZoneId.of("Europe/London"));
        LocalDateTime gmtDateTime = gmtZoned.toLocalDateTime();
        F1Calendar f1calendar = calendarRepository.findFirstByRaceBeforeOrderByRaceDesc(gmtDateTime);
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
        standingsBySeason.forEach(standing -> {
            if (!totalPoints.containsKey(standing.getCode())) {
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
            if (standing.getResultThisRound() != null) {
                roundResults.get(standing.getCode()).add(standing.getId().getRound(), new BigDecimal(standing.getResultThisRound()));
            }
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
        standingsBySeason.forEach(standing -> {
            if (!totalPoints.containsKey(standing.getId().getId())) {
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
                .races(ergastService.getRacesOfSeason(String.valueOf(properties.getCurrentYear())))
                .build();
    }

    @Override
    public List<SportSurgeEvent> getSportSurge() {
        return sportSurgeEventRepository.findAllByOrderByIdDesc();
    }

    @Override
    public List<F1Comment> postComment(F1Comment comment, String ipAddress) {
        comment.setTimestamp(new Date());
        String username = MainUtility.handleUsername(comment.getNickname());
        comment.setStatus(1);
        comment.setNickname(username);
        if (comment.getComment().length() > 900) {
            comment.setComment(comment.getComment().substring(0, 900));
        }
        comment.setIp(ipAddress);
        f1CommentRepository.save(comment);
        return f1CommentRepository.findFirst30ByPageAndStatusOrderByTimestampDesc(comment.getPage(), 1);
    }

    @Override
    public void sendMessage(F1Comment message, String ipAddress) {
        message.setTimestamp(new Date());
        message.setPage(47);
        message.setIp(ipAddress);
        message.setStatus(1);
        f1CommentRepository.save(message);
    }

    @Override
    public List<F1Comment> getComments(String page) {
        return f1CommentRepository.findFirst30ByPageAndStatusOrderByTimestampDesc(Integer.valueOf(page), 1);
    }

    @Override
    public Boolean fetchInstagramFeed() throws IGLoginException {
        return instagramService.fetchInstagramFeed();
    }

    @Override
    public TripleInstagramFeed getInstagramFeedPage(Integer page) throws IGLoginException {
        if(page>40){
            return new TripleInstagramFeed();
        }
        return instagramService.getInstagramFeedPage(page);
    }

    @Override
    public DoubleTwitterFeed getTwitterPosts(Integer page) {
        return new DoubleTwitterFeed(twitterService.getTwitterPosts(page));
    }

    @Override
    public DoubleRedditNewFeed getRedditNewPosts(Integer page) {
        return new DoubleRedditNewFeed(redditService.getRedditNewPosts(page));
    }

    @Override
    public DoubleRedditTopFeed getRedditTopPosts(Integer page) {
        return new DoubleRedditTopFeed(redditService.getRedditTopPosts(page));
    }

    @Override
    public void fetchRedditPosts() {
        redditService.fetchRedditPosts();
    }

    @Override
    public Double4chanFeed get4chanPosts(Integer page) {
        return new Double4chanFeed(forchanService.get4chanPosts(page));
    }

    @Override
    public List<ForchanPost> fetch4chanPosts() {
        return forchanService.fetch4chanPosts();
    }

    @Override
    public Boolean fetchTwitterPosts() throws Exception {
        return twitterService.fetchTwitterPosts();
    }

    @Override
    public byte[] getImage(String code) {
        return instagramService.getImage(code);
    }

    @Override
    public byte[] getArt(String code) {
        return artImageRepository.findFirstByCode(code).getImage();
    }

    @Override
    public String postContent(AwsContent content, String ipAddress) {
        //String code = UUID.randomUUID().toString();
        String code = MainUtility.generateCodeFromTitle(content.getTitle());
        content.setCode(code);
        if(content.getStatus()==null) {
            content.setStatus(1);
        }
        if(content.getUrl()==null) {
            content.setIconUrl("./assets/img/favicon.png");
        }
        content.setTimestampCreated(new Date());
        content.setTimestampActivity(new Date());
        content.setCommentCount(0);
        String username = MainUtility.handleUsername(content.getUsername());
        content.setUsername(username);
        content.setIp(ipAddress);
        awsRepository.save(content);
        log.info("PRIJE ASYNC1");
        if(content.getUrl()!=null) {
            updatePostImagesAsync(content);
        }
        log.info("POSLIJE ASYNC1");
        return code;
    }

    @Async
    void updatePostImagesAsync(AwsContent content) {
        new Thread(() -> {
            log.info("UNUTAR2a ASYNC1");
            redditService.updatePostImages(content);
            awsRepository.save(content);
            log.info("UNUTAR2 ASYNC1");
        }).start();
    }

    @Override
    public List<AwsContent> getAwsContent(Integer page) {
        Pageable paging = PageRequest.of(page, 15);
        return awsRepository.findAllByStatusLessThanEqualOrderByTimestampActivityDesc(3, paging);
    }

    @Override
    public AwsContent getAwsPost(String code) {
        AwsContent response = awsRepository.findByCodeAndStatusLessThanEqual(code, 3);
        if (response != null) {
            response.setComments(awsCommentRepository.findAllByContentCodeAndStatusOrderByTimestampCreatedDesc(code, 1));
        }
        return response;
    }

    @Override
    public List<AwsComment> postAwsComment(AwsComment comment, String ipAddress) {
        comment.setTimestampCreated(new Date());
        comment.setTextContent(comment.getTextContent().replaceAll("[\n\n\n]+", "\n"));
        String username = MainUtility.handleUsername(comment.getUsername());
        comment.setUsername(username);
        comment.setStatus(1);
        comment.setIp(ipAddress);
        awsCommentRepository.save(comment);
        awsRepository.updateActivityAndCommentCount(comment.getContentCode(), new Date());
        return awsCommentRepository.findAllByContentCodeAndStatusOrderByTimestampCreatedDesc(comment.getContentCode(), 1);
    }

    @Override
    public List<AwsComment> getAwsComments(String code) {
        return awsCommentRepository.findAllByContentCodeAndStatusOrderByTimestampCreatedDesc(code, 1);
    }

    @Override
    public BasicResponse moderateComment(CommentModeration moderation) {
        String message;
        Integer state = -1;
        Integer oppositeStatus;
        String executedAction = "";
        if (moderation.getAction() == 1) {
            oppositeStatus = 2;
            executedAction = " restored";
        } else {
            oppositeStatus = 1;
            executedAction = " deleted";
        }
        message = "Comment no." + moderation.getCommentId() + executedAction + " by moderator. Reason: " + moderation.getReason();
        if (moderation.getPanel() == 1) {
            F1Comment comment = f1CommentRepository.findF1CommentByIdAndStatus(moderation.getCommentId(), oppositeStatus);
            if (comment != null) {
                state = f1CommentRepository.updateStatus(moderation.getCommentId(), moderation.getAction());
                F1Comment notification = F1Comment.builder()
                        .comment(message)
                        .page(comment.getPage())
                        .status(1)
                        .timestamp(new Date())
                        .nickname(SYSTEM_MESSAGE)
                        .build();
                f1CommentRepository.save(notification);
            }
        }
        if (moderation.getPanel() == 2) {
            AwsComment comment = awsCommentRepository.findAwsCommentByIdAndStatus(moderation.getCommentId(), oppositeStatus);
            if (comment != null) {
                state = awsCommentRepository.updateStatus(moderation.getCommentId(), moderation.getAction());
                AwsComment notification = AwsComment.builder()
                        .textContent(message)
                        .status(1)
                        .contentCode(comment.getContentCode())
                        .timestampCreated(new Date())
                        .username(SYSTEM_MESSAGE)
                        .build();
                awsCommentRepository.save(notification);
            }

        }
        return BasicResponse.builder().state(state).message(message).build();
    }

    private Map<String, Integer> getRemainingTime(LocalDateTime gmtDateTime, F1Calendar f1calendar, Integer mode) {
        Map<String, Integer> output = new HashMap<>();
        Duration duration;
        if (mode == 0 || mode == 1) {
            duration = Duration.between(gmtDateTime, f1calendar.getPractice1());
            output.put("FP1Days", (int) duration.toDays());
            output.put("FP1Seconds", (int) duration.toSeconds());
        }
        if (mode == 0 || mode == 2) {
            duration = Duration.between(gmtDateTime, f1calendar.getPractice2());
            output.put("FP2Days", (int) duration.toDays());
            output.put("FP2Seconds", (int) duration.toSeconds());
        }
        if (mode == 0 || mode == 3) {
            duration = Duration.between(gmtDateTime, f1calendar.getPractice3());
            output.put("FP3Days", (int) duration.toDays());
            output.put("FP3Seconds", (int) duration.toSeconds());
        }
        if (mode == 0 || mode == 4) {
            duration = Duration.between(gmtDateTime, f1calendar.getQualifying());
            output.put("qualifyingDays", (int) duration.toDays());
            output.put("qualifyingSeconds", (int) duration.toSeconds());
        }
        if (mode == 0 || mode == 5) {
            duration = Duration.between(gmtDateTime, f1calendar.getRace());
            output.put("raceDays", (int) duration.toDays());
            output.put("raceSeconds", (int) duration.toSeconds());
        }
        return output;
    }
}
