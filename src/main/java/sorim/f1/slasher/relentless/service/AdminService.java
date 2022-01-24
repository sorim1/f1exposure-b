package sorim.f1.slasher.relentless.service;

import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import sorim.f1.slasher.relentless.entities.*;
import sorim.f1.slasher.relentless.model.Aws;
import sorim.f1.slasher.relentless.model.FullBackup;
import sorim.f1.slasher.relentless.model.FullExposure;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface AdminService {
    void initialize() throws Exception;

    void refreshCalendarOfCurrentSeason() throws Exception;

    void validateCalendarForNextRace() throws Exception;


    Boolean initializeStandings() throws IOException;

    Boolean initializeStandingsFromLivetiming(Map<String, DriverStanding> standingsMap, Map<String, sorim.f1.slasher.relentless.model.livetiming.Driver> driversMap, Integer newRound);

    Boolean initializeFullStandingsThroughRounds() throws IOException;

    Integer fetchSportSurgeLinks() throws IOException;

    Boolean fetchReplayLinks();

    void deleteSportSurgeLinks();

    F1Calendar getUpcomingRace();

    F1Calendar updateUpcomingRace(F1Calendar entry);

    Integer deleteComment(Integer mode, Integer id);

    AwsContent patchAwsPost(AwsContent entry);

    List<Integer> updateCurrentRound(boolean increaseOnly);

    List<Integer> setCurrentRound(Integer newRound);

    FullExposure backupExposure();

    Integer getNextRefreshTick(Integer seconds);

    List<F1Comment> getAdminMessages();

    Boolean endRaceWeekendJobs();

    Boolean restoreExposureFromBackup(FullExposure fullExposure);

    List<Driver> getExposureDrivers();

    List<Driver> updateExposureDrivers(List<Driver> list);

    Aws backupPosts();

    Boolean restorePosts(Aws body);

    FullBackup fullBackup();

    Boolean restoreFromFullBackup(FullBackup body);

    Integer deleteAwsContent(String username);

    String setCountdownMode(String mode);

    Boolean generateChart();

    Boolean instagramCleanup() throws IGLoginException;

    Boolean checkCurrentStream() throws IOException;
}
