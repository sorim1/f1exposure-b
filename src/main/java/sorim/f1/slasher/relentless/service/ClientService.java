package sorim.f1.slasher.relentless.service;

import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import sorim.f1.slasher.relentless.entities.*;
import sorim.f1.slasher.relentless.model.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ClientService {
    Boolean exposeDrivers(String[] exposedList, String ipAddress) throws Exception;

    ExposureData getExposedChartData();

    CalendarData getCountdownData(Integer mode);

    List<DriverStanding> getDriverStandings();

    List<ConstructorStanding> getConstructorStandings();

    ExposureResponse getExposureDriverList();

    AllStandings getStandings();

    List<SportSurgeEvent> getSportSurge();

    List<F1Comment> postComment(F1Comment comment);

    void sendMessage(F1Comment message);

    List<F1Comment> getComments(String page);

    Boolean fetchInstagramFeed() throws IGLoginException;

    TripleInstagramFeed getInstagramFeed() throws IGLoginException;

    TripleInstagramFeed getInstagramFeedPage(Integer page) throws IGLoginException;

    DoubleTwitterFeed getTwitterPosts(Integer page);

    Boolean fetchTwitterPosts() throws Exception;

    byte[] getImage(String code);

    String postContent(AwsContent content);

    List<AwsContent> getAwsContent(String page);

    AwsContent getAwsPost(String code);

    List<AwsComment> postAwsComment(AwsComment comment);

    List<AwsComment> getAwsComments(String code);
}
