package sorim.f1.slasher.relentless.service;

import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import sorim.f1.slasher.relentless.entities.*;
import sorim.f1.slasher.relentless.model.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ClientService {
    Boolean exposeDrivers(String[] exposedList, String ipAddress) throws Exception;

    void validateHeader(String authorization) throws Exception;

    void checkHeader(String authorization) throws Exception;

    String validateIp(HttpServletRequest request);

    ExposureData getExposedChartData();

    CalendarData getCountdownData();

    List<DriverStanding> getDriverStandings();

    List<ConstructorStanding> getConstructorStandings();

    ExposureResponse getExposureDriverList();

    AllStandings getStandings();

    List<SportSurgeEvent> getSportSurge();

    List<F1Comment> postComment(F1Comment comment);

    List<F1Comment> getComments(String page);

    List<InstagramPost>  fetchInstagramFeed() throws IGLoginException;

    TripleInstagramFeed getInstagramFeed() throws IGLoginException;

    TripleInstagramFeed getInstagramFeedPage(Integer page) throws IGLoginException;

    DoubleTwitterFeed getTwitterPosts(Integer page);

    List<TwitterPost> fetchTwitterPosts() throws Exception;

    byte[] getImage(String code);
}
