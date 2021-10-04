package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.entities.*;
import sorim.f1.slasher.relentless.model.Aws;
import sorim.f1.slasher.relentless.model.FullBackup;
import sorim.f1.slasher.relentless.model.FullExposure;

import java.io.IOException;
import java.util.List;

public interface AdminService {
    void initialize() throws Exception;

    void refreshCalendarOfCurrentSeason() throws Exception;

    void validateCalendarForNextRace() throws Exception;


    Boolean initializeStandings() throws IOException;

    Boolean initializeFullStandingsThroughRounds() throws IOException;

    Integer fetchSportSurgeLinks() throws IOException;

    void deleteSportSurgeLinks();

    void closeExposurePoll();

    void openExposurePoll(Integer minutes);

    F1Calendar getUpcomingRace();

    F1Calendar updateUpcomingRace(F1Calendar entry);

    Integer deleteComment(Integer mode, Integer id);

    AwsContent patchAwsPost(AwsContent entry);

    List<Integer> updateCurrentRound(boolean increaseOnly);

    List<Integer> setCurrentRound(Integer newRound);

    FullExposure backupExposure();

    Integer getNextRefreshTick(Integer seconds) ;

    List<F1Comment> getAdminMessages();

    Boolean endRaceWeekendJobs();

    Boolean restoreExposureFromBackup(FullExposure fullExposure);

    List<ExposureDriver> getExposureDrivers();

    List<ExposureDriver> updateExposureDrivers(List<ExposureDriver> list);

    Aws backupPosts();

    Boolean restorePosts(Aws body);

    FullBackup fullBackup();

    Boolean restoreFromFullBackup(FullBackup body);
}
