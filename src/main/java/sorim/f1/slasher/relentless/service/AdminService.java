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

    Boolean refreshCalendarOfCurrentSeason(String urlString) throws Exception;

    Boolean refreshCalendarOfCurrentSeasonSecondary(String urlString) throws Exception;

    Boolean deleteCalendar() throws Exception;

    void validateCalendarForNextRace() throws Exception;

    Boolean initializeStandings() throws IOException;

    Boolean initializeStandingsFromLivetiming(Map<String, DriverStanding> standingsMap, Map<String, sorim.f1.slasher.relentless.model.livetiming.Driver> driversMap, Integer newRound);

    Boolean initializeFullStandingsThroughRounds() throws IOException;

    Boolean fetchReplayLinks();

    F1Calendar getUpcomingRace();

    F1Calendar updateUpcomingRace(F1Calendar entry);

    Integer deleteComment(Integer mode, Integer id);

    NewsContent patchAwsPost(NewsContent entry);

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

    String setOverlays(String overlays);

    String setIframeLink(String link);

    Boolean generateChart();

    Boolean instagramCleanup() throws IGLoginException;

    Boolean checkCurrentStream() throws IOException;

    Boolean fetchFourChanPosts();

    Boolean deleteFourChanPosts();

    Boolean deleteFourChanPost(Integer id);

    Boolean removeVideo(Integer id);

    List<Replay>  saveVideos(List<Replay>  video);

    String updateCurrentSeasonPast(Integer season);

    F1Calendar getCalendar();

    F1Calendar saveCalendar(F1Calendar body);
}
