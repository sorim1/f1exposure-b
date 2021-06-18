package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.entities.DriverStanding;

import java.io.IOException;
import java.util.List;

public interface AdminService {
    void initialize() throws Exception;

    void refreshCalendar() throws Exception;

    List<DriverStanding> initializeStandings() throws IOException;

    void fetchSportSurgeLinks() throws IOException;
}
