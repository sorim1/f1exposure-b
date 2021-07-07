package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.entities.DriverStanding;

import java.io.IOException;
import java.util.List;

public interface AdminService {
    void initialize() throws Exception;

    void refreshCalendar() throws Exception;

    Boolean initializeStandings() throws IOException;

    Integer fetchSportSurgeLinks() throws IOException;

    void deleteSportSurgeLinks();
}
