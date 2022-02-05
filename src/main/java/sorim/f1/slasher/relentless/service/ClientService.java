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

    TripleInstagramFeed getInstagramFeedPage(Integer page) throws IGLoginException;

    DoubleTwitterFeed getTwitterPosts(Integer page);

    DoubleRedditNewFeed getRedditNewPosts(Integer page);

    DoubleRedditTopFeed getRedditTopPosts(Integer page);

    Double4chanFeed get4chanPosts(Integer page);

    void fetchRedditPosts();

    Boolean fetch4chanPosts();

    Boolean fetchImageFeed() throws Exception;

    Boolean fetchTwitterPosts() throws Exception;

    byte[] getImage(String code);

    byte[] getArt(String code);

    String postContent(AwsContent content, String ipAddress);

    List<AwsContent> getNews(Integer page);

    AwsContent getAwsPost(String code);

    List<AwsComment> postAwsComment(AwsComment comment, String ipAddress);

    List<AwsComment> getAwsComments(String code);

    BasicResponse moderateComment(CommentModeration moderation);

    List<Replay> getReplays(Integer page);

    String setOverlays(String modes);

    String setIframeLink(String link);

    AwsContent getTopNews();

    String getStreamer();

    Boolean setStreamer(String streamer);

    UtilityContext getUtilityContext();
}
