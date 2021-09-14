package sorim.f1.slasher.relentless.service;

import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import sorim.f1.slasher.relentless.entities.*;
import sorim.f1.slasher.relentless.model.*;

import java.util.List;

public interface ClientService {
    Boolean exposeDrivers(String[] exposedList, String ipAddress) throws Exception;

    ExposureData getExposedChartData();

    CalendarData getCountdownData(Integer mode);

    CalendarData getCountdownDataPrevious(Integer mode);

    List<DriverStanding> getDriverStandings();

    List<ConstructorStanding> getConstructorStandings();

    ExposureResponse getExposureDriverList();

    AllStandings getStandings();

    List<SportSurgeEvent> getSportSurge();

    List<F1Comment> postComment(F1Comment comment, String ipAddress);

    void sendMessage(F1Comment message, String ipAddress);

    List<F1Comment> getComments(String page);

    Boolean fetchInstagramFeed() throws IGLoginException;

    TripleInstagramFeed getInstagramFeedPage(Integer page) throws IGLoginException;

    DoubleTwitterFeed getTwitterPosts(Integer page);

    DoubleRedditNewFeed getRedditNewPosts(Integer page);

    DoubleRedditTopFeed getRedditTopPosts(Integer page);

    Double4chanFeed get4chanPosts(Integer page);

    void fetchRedditPosts();

    List<ForchanPost> fetch4chanPosts();

    Boolean fetchTwitterPosts() throws Exception;

    byte[] getImage(String code);

    String postContent(AwsContent content, String ipAddress);

    List<AwsContent> getAwsContent(String page);

    AwsContent getAwsPost(String code);

    List<AwsComment> postAwsComment(AwsComment comment, String ipAddress);

    List<AwsComment> getAwsComments(String code);

    BasicResponse moderateComment(CommentModeration moderation);
}
