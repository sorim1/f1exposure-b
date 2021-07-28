package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.entities.AwsContent;
import sorim.f1.slasher.relentless.entities.DriverStanding;
import sorim.f1.slasher.relentless.entities.F1Calendar;

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

    F1Calendar getUpcomingRace();

    F1Calendar updateUpcomingRace(F1Calendar entry);

    Integer deleteComment(Integer mode, Integer id);

    AwsContent patchAwsPost(AwsContent entry);
}
