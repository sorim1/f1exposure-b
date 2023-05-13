package sorim.f1.slasher.relentless.service.impl;

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

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClientServiceImpl implements ClientService {

    private static final String SYSTEM_MESSAGE = "### SYSTEM MESSAGE ###";
    public static String overlays;
    public static List<String> overlayList;
    public static String iframeLink;
    public static Boolean fourchanDisabled;

    public static String FOURCHAN_DISABLED = "FOURCHAN_DISABLED";
    private static Boolean allowNonRedditNews;

    private static SidebarData sidebarData = new SidebarData();
    private final MainProperties properties;
    private final CalendarRepository calendarRepository;
    private final DriverStandingsRepository driverStandingsRepository;
    private final ConstructorStandingsRepository constructorStandingsRepository;
    private final JsonRepository jsonRepository;
    private final F1CommentRepository f1CommentRepository;
    private final NewsRepository newsRepository;
    private final NewsCommentRepository newsCommentRepository;
    private final InstagramService instagramService;
    private final TwitterService twitterService;
    private final TwitchService twitchService;
    private final RedditService redditService;
    private final FourchanService forchanService;
    private final ExposureStrawpollService exposureService;
    private final ErgastService ergastService;
    private final VideoService videoService;
    private final PropertiesRepository propertiesRepository;
    private final ArtImageRepository artImageRepository;

    @Override
    public Boolean exposeDrivers(String[] exposedList, String ipAddress) throws Exception {
        // return exposureService.exposeDrivers(exposedList, ipAddress);
        return true;
    }

    @Override
    public CalendarData getCountdownData(Integer mode) {
       // ZonedDateTime gmtZoned = ZonedDateTime.now(ZoneId.of("Europe/London"));
        ZonedDateTime utcZoned = ZonedDateTime.now(ZoneId.of("UTC"));
        LocalDateTime utcDateTime = utcZoned.toLocalDateTime();
        LocalDateTime keepCalendarOneHourLonger = utcDateTime.minusHours(1);
        //int londonOffsetMinutes = (utcZoned.getOffset().getTotalSeconds()) / 60;
        F1Calendar f1calendar = calendarRepository.findFirstByRaceAfterOrPractice3AfterOrderByPractice1(keepCalendarOneHourLonger, keepCalendarOneHourLonger);
        if (f1calendar == null) {
            return CalendarData.builder()
                   // .londonOffset(londonOffsetMinutes)
                    .overlays(overlayList)
                    .iframeLink(iframeLink)
                    .build();
        }
        Map<String, Integer> countdownData = getRemainingTime(utcDateTime, f1calendar, mode);
        return CalendarData.builder().f1Calendar(f1calendar).countdownData(countdownData)
                .overlays(overlayList)
                .iframeLink(iframeLink)
             //   .londonOffset(londonOffsetMinutes)
                .build();
    }

    @Override
    public List<DriverStanding> getDriverStandings() {
        return driverStandingsRepository.findAllByOrderByPositionAsc();
    }

    @Override
    public List<ConstructorStanding> getConstructorStandings() {
        return constructorStandingsRepository.findAllByOrderByPositionAsc();
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
                .driverStandingByRound(getJsonCharts("DRIVERS_TOTAL_POINTS"))
                .driverPointsByRound(getJsonCharts("DRIVERS_ROUND_POINTS"))
                .driverResultByRound(getJsonCharts("DRIVERS_ROUND_RESULTS"))
                .constructorStandingByRound(getJsonCharts("CONSTRUCTOR_TOTAL_POINTS"))
                .constructorPointsByRound(getJsonCharts("CONSTRUCTOR_ROUND_POINTS"))
                .gridToResultChartWithDnf(getJsonCharts("GRID_TO_RESULT_WITH_DNF"))
                .gridToResultChartWithoutDnf(getJsonCharts("GRID_TO_RESULT_WITHOUT_DNF"))
                .races(ergastService.getRacesOfSeason(String.valueOf(properties.getCurrentSeasonPast())))
                .currentYear(properties.getCurrentSeasonPast())
                .build();
    }

    private List<ChartSeries> getJsonCharts(String jsonId) {
        Object response = jsonRepository.findAllById(jsonId).getJson();
        return (List<ChartSeries>) response;
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
    public Boolean fetchInstagramPosts() throws Exception {
        return instagramService.fetchInstagramFeed();
    }

    @Override
    public TripleInstagramFeed getInstagramFeedPage(Integer mode, Integer page) {
        if (page > 40) {
            return new TripleInstagramFeed();
        }
        return instagramService.getInstagramFeedPage(mode, page);
    }

    @Override
    public TrippleTwitterFeed getTwitterPosts(Integer mode, Integer page) {
        return new TrippleTwitterFeed(mode, twitterService.getTwitterPosts(page));
    }

    @Override
    public TrippleRedditFeed getRedditPosts(Integer mode, Integer page) {
        return new TrippleRedditFeed(mode, redditService.getRedditPosts(page));
    }

    @Override
    public void fetchRedditPosts() {
        NewsContent latestNews = redditService.fetchRedditPosts();
        if (latestNews != null && !allowNonRedditNews) {
            setSidebarData(latestNews);
        } else {
            setSidebarData(newsRepository.findFirstByStatusLessThanEqualOrderByTimestampActivityDesc(5));
        }
    }

    @Override
    public Tripple4chanFeed get4chanPosts(Integer mode, Integer page) {
        return new Tripple4chanFeed(mode, forchanService.get4chanPosts(page));
    }

    @Override
    public List<Streamable> getStreamables() {
        return forchanService.getStreamables();
    }

    @Override
    public Boolean fetch4chanPosts() {
        return forchanService.fetch4chanPosts();
    }

    @Override
    public Boolean fetchImageFeed() throws Exception {
        fetchTwitterPosts();
        fetchRedditPosts();
        fetchInstagramPosts();
        // fetch4chanPosts();
        return true;
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
    public String postContent(NewsContent content, String ipAddress) {
        //String code = UUID.randomUUID().toString();
        String code = MainUtility.generateCodeFromTitle(content.getTitle());
        content.setCode(code);
        if (content.getStatus() == null) {
            content.setStatus(1);
        }
        if (content.getUrl() == null) {
            content.setIconUrl("./assets/img/favicon.png");
        }
        content.setTimestampCreated(new Date());
        content.setTimestampActivity(new Date());
        content.setCommentCount(0);
        String username = MainUtility.handleUsername(content.getUsername());
        content.setUsername(username);
        content.setIp(ipAddress);
        newsRepository.save(content);
        if (content.getUrl() != null) {
            updatePostImagesAsync(content);
        }
        return code;
    }

    @Async
    void updatePostImagesAsync(NewsContent content) {
        new Thread(() -> {
            redditService.updatePostImages(content);
            newsRepository.save(content);
        }).start();
    }

    @Override
    public List<NewsContent> getNews(Integer page) {
        Pageable paging = PageRequest.of(page, 15);
        return newsRepository.findAllByStatusLessThanEqualOrderByTimestampActivityDesc(5, paging);
    }

    @Override
    public NewsContent getNewsPost(String code) {
        NewsContent response = newsRepository.findByCodeAndStatusLessThanEqual(code, 5);
        if (response != null) {
            response.setComments(newsCommentRepository.findAllByContentCodeAndStatusLessThanOrderByTimestampCreatedDesc(code, 5));
        }
        return response;
    }
    @Override
    public Boolean bumpNewsPost(String code) {
        NewsContent response = newsRepository.findByCodeAndStatusLessThanEqual(code, 5);
        if (response != null) {
            response.setTimestampActivity(new Date());
            newsRepository.save(response);
            fetchRedditPosts();
            return true;
        }
        return false;
    }


    @Override
    public NewsComment postNewsComment(NewsComment comment, String ipAddress) {
        comment.setTimestampCreated(new Date());
        comment.setTextContent(comment.getTextContent().replaceAll("[\n\n\n]+", "\n"));
        String username = MainUtility.handleUsername(comment.getUsername());
        comment.setUsername(username);
        if(comment.getStatus()==null){
            comment.setStatus(1);
        }
        comment.setIp(ipAddress);
        newsCommentRepository.save(comment);
        newsRepository.updateActivityAndCommentCount(comment.getContentCode(), new Date());
        return comment;
    }

    @Override
    public List<NewsComment> getNewsComments(String code) {
        return newsCommentRepository.findAllByContentCodeAndStatusLessThanOrderByTimestampCreatedDesc(code, 3);
    }

    @Override
    public BasicResponse moderateComment(CommentModeration moderation) {
        String message;
        Integer state = -1;
        Integer oppositeStatus;
        Integer oppositeStatusNewComment;
        String executedAction = "";
        if (moderation.getAction() == 1) {
            oppositeStatus = 2;
            oppositeStatusNewComment = 3;
            executedAction = " restored";
        } else {
            oppositeStatus = 1;
            oppositeStatusNewComment = 1;
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
            NewsComment comment = newsCommentRepository.findNewsCommentById(moderation.getCommentId());
            if (comment != null) {
                state = newsCommentRepository.updateStatus(moderation.getCommentId(), moderation.getAction()+1);
                NewsComment notification = NewsComment.builder()
                        .textContent(message)
                        .status(1)
                        .contentCode(comment.getContentCode())
                        .timestampCreated(new Date())
                        .username(SYSTEM_MESSAGE)
                        .build();
                newsCommentRepository.save(notification);
            }

        }
        return BasicResponse.builder().state(state).message(message).build();
    }

    @Override
    public List<Replay> getReplays(Integer page) {
        return videoService.getReplays(page);
    }

    @Override
    public String setOverlays(String newOverlays, boolean keepOldOverlays) {
        String oldOverlays = "";
        if (keepOldOverlays) {
            if (overlays.contains("apustaja")) {
                oldOverlays += ",apustaja";
            }
            if (overlays.contains("bottom-right")) {
                oldOverlays += ",bottom-right";
            }
            if (overlays.contains("bottom-left")) {
                oldOverlays += ",bottom-left";
            }
        }
        String fullOverlays = newOverlays + oldOverlays;
        AppProperty ap = AppProperty.builder().name("OVERLAYS").value(fullOverlays).build();
        propertiesRepository.save(ap);
        String response = overlays + " -> ";
        overlays = fullOverlays;
        response = response + overlays;
        overlayList = stringToList(overlays);
        return response;
    }

    private List<String> stringToList(String string) {
        return Arrays.asList(string.split(",", -1));
    }

    @Override
    public String setIframeLink(String link) {
        AppProperty ap = AppProperty.builder().name("IFRAME_LINK").value(link).build();
        propertiesRepository.save(ap);
        String response = iframeLink + " -> ";
        iframeLink = link;
        response = response + iframeLink;
        return response;
    }

    @Override
    public SidebarData getSidebarData() {
        return sidebarData;
    }

    @Override
    public String getStreamer() {
        return twitchService.getStreamer();
    }

    @Override
    public Boolean setStreamer(String streamer) {
        return twitchService.setStreamer(streamer);
    }

    @Override
    public UtilityContext getUtilityContext() {
        return UtilityContext.builder().overlays(overlayList).build();
    }

    @Override
    public List<Replay> getVideos() {
        return videoService.getVideos();
    }

    @Override
    public Boolean getFourchanDisabled() {
        return fourchanDisabled;
    }

    @Override
    public Boolean setFourchanDisabled(String value) {
        fourchanDisabled = Boolean.valueOf(value);
        AppProperty ap = AppProperty.builder().name(FOURCHAN_DISABLED).value(value).build();
        propertiesRepository.save(ap);
        return fourchanDisabled;
    }

    @PostConstruct
    public void init() {
        initOverlays();
        initIframeLink();
        initFourchanDisabled();
        setAllowNonRedditNews();
        setSidebarData(newsRepository.findFirstByStatusLessThanEqualOrderByTimestampActivityDesc(5));
        log.info("clientServiceInit: {} -{}", iframeLink, overlays);
    }

    private void setSidebarData(NewsContent topNews) {
        this.sidebarData = SidebarData.builder()
                .topNews(topNews)
                .latestTwitterPost(twitterService.getMostPopularDailyPost())
                .exposedDriver(exposureService.getLatestRaceExposureWinner())
                .build();
    }

    private void setAllowNonRedditNews() {
        AppProperty ap = propertiesRepository.findDistinctFirstByName("ALLOW_NONREDDIT_TOP_NEWS");
        if (ap == null) {
            setAllowNonRedditNewsProperty(true);
        } else {
            allowNonRedditNews = Boolean.valueOf(ap.getValue());
        }
    }


    @Override
    public void setAllowNonRedditNewsProperty(Boolean bool) {
        allowNonRedditNews = bool;
        AppProperty ap = AppProperty.builder().name("ALLOW_NONREDDIT_TOP_NEWS").value(String.valueOf(bool)).build();
        propertiesRepository.save(ap);
    }


    private void initOverlays() {
        AppProperty ap = propertiesRepository.findDistinctFirstByName("OVERLAYS");
        if (ap == null) {
            ap = AppProperty.builder().name("OVERLAYS").value("0").build();
            propertiesRepository.save(ap);
        }
        overlays = ap.getValue();
        overlayList = stringToList(overlays);
    }

    private void initIframeLink() {
        AppProperty ap = propertiesRepository.findDistinctFirstByName("IFRAME_LINK");
        if (ap == null) {
            ap = AppProperty.builder().name("IFRAME_LINK").value("https://streamable.com/o/kzq7xz").build();
            propertiesRepository.save(ap);
        }
        iframeLink = ap.getValue();
    }

    private void initFourchanDisabled() {
        AppProperty ap = propertiesRepository.findDistinctFirstByName(FOURCHAN_DISABLED);
        if (ap == null) {
            ap = AppProperty.builder().name(FOURCHAN_DISABLED).value("false").build();
            propertiesRepository.save(ap);
        }
        fourchanDisabled = Boolean.valueOf(ap.getValue());
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
            if (f1calendar.getPractice3() != null) {
                duration = Duration.between(gmtDateTime, f1calendar.getPractice3());
                output.put("FP3Days", (int) duration.toDays());
                output.put("FP3Seconds", (int) duration.toSeconds());
            }
        }
        if (mode == 0 || mode == 4) {
            if (f1calendar.getQualifying() != null) {
                duration = Duration.between(gmtDateTime, f1calendar.getQualifying());
                output.put("qualifyingDays", (int) duration.toDays());
                output.put("qualifyingSeconds", (int) duration.toSeconds());
            }
        }
        if (mode == 0 || mode == 5) {
            if (f1calendar.getRace() != null) {
                duration = Duration.between(gmtDateTime, f1calendar.getRace());
                output.put("raceDays", (int) duration.toDays());
                output.put("raceSeconds", (int) duration.toSeconds());
            }
        }
        if (mode == 0 || mode == 6) {
            if (f1calendar.getSprint() != null) {
                duration = Duration.between(gmtDateTime, f1calendar.getSprint());
                output.put("sprintDays", (int) duration.toDays());
                output.put("sprintSeconds", (int) duration.toSeconds());
            }
        }
        return output;
    }
}
