package sorim.f1.slasher.relentless.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sorim.f1.slasher.relentless.entities.ergast.Race;
import sorim.f1.slasher.relentless.service.ErgastService;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@RequestMapping("ergast")
public class ErgastController {

    private final ErgastService service;
    //TODO validacija, zasebni key za admin sučelje, ovo je isto admin-only kontroler


    @GetMapping("/fetchCurrentSeason")
    List<Race> fetchCurrentSeason() throws Exception {
        return service.fetchCurrentSeason();
    }

    @GetMapping("/fetchSeason")
    List<Race> fetchSeason(@PathVariable String year) throws Exception {
        log.info("year: {}", year);
        return service.fetchSeason(year);
    }
}
