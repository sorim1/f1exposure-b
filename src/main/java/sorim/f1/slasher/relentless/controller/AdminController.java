package sorim.f1.slasher.relentless.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sorim.f1.slasher.relentless.entities.DriverStanding;
import sorim.f1.slasher.relentless.service.AdminService;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("admin")
public class AdminController {

    private final AdminService service;
    //TODO validacija, zasebni key za admin sučelje

    @GetMapping("/refreshCalendar")
    boolean refreshCalendar() throws Exception {
        service.refreshCalendar();
        return true;
    }

    @GetMapping("/initialize")
    boolean initialize() throws Exception {
        service.initialize();
        return true;
    }

    @GetMapping("/initializeStandings")
    Boolean intializeStandings() throws Exception {
        return service.initializeStandings();
    }

    @GetMapping("/fetchSportSurgeLinks")
    void fetchSportSurgeLinks() throws Exception {
        service.fetchSportSurgeLinks();
    }

    @GetMapping("/closeExposurePoll")
    void closeExposurePoll() {
        service.closeExposurePoll();
    }
}
