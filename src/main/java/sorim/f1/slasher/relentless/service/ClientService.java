package sorim.f1.slasher.relentless.service;

import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import sorim.f1.slasher.relentless.entities.*;
import sorim.f1.slasher.relentless.model.*;

import java.util.List;

public interface ClientService {
    Boolean exposeDrivers(String[] exposedList, String ipAddress) throws Exception;


    CalendarData getCountdownData(Integer mode);

    List<DriverStanding> getDriverStandings();

    List<ConstructorStanding> getConstructorStandings();

    ExposureResponse getExposureDriverList();

    AllStandings getStandings();

    List<F1Comment> postComment(F1Comment comment, String ipAddress);

    void sendMessage(F1Comment message, String ipAddress);

    List<F1Comment> getComments(String page);

    Boolean fetchInstagramPosts() throws IGLoginException;

    TripleInstagramFeed getInstagramFeedPage(Integer mode, Integer page) throws IGLoginException;

    TrippleTwitterFeed getTwitterPosts(Integer mode, Integer page);

    TrippleRedditFeed getRedditPosts(Integer mode, Integer page);

    Tripple4chanFeed get4chanPosts(Integer mode, Integer page);

    List<Streamable> getStreamables();

    void fetchRedditPosts();

    Boolean fetch4chanPosts();

    Boolean fetchImageFeed() throws Exception;

    Boolean fetchTwitterPosts() throws Exception;

    byte[] getImage(String code);

    byte[] getArt(String code);

    String postContent(NewsContent content, String ipAddress);

    List<NewsContent> getNews(Integer page);

    NewsContent getNewsPost(String code);

    List<NewsComment> postNewsComment(NewsComment comment, String ipAddress);

    List<NewsComment> getNewsComments(String code);

    BasicResponse moderateComment(CommentModeration moderation);

    List<Replay> getReplays(Integer page);

    String setOverlays(String modes, boolean keepOldOverlays);

    String setIframeLink(String link);

    SidebarData getSidebarData();

    String getStreamer();

    Boolean setStreamer(String streamer);

    UtilityContext getUtilityContext();

    List<Replay> getVideos();

    Boolean getFourchanDisabled();

    Boolean setFourchanDisabled(String value);
}
