package sorim.f1.slasher.relentless.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sorim.f1.slasher.relentless.service.ErgastService;
import sorim.f1.slasher.relentless.service.LiveTimingService;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("ergast")
public class ErgastController {

    private final ErgastService service;
    //TODO validacija, zasebni key za admin sučelje, ovo je isto admin-only kontroler

    @GetMapping("/tempoPython")
    boolean tempoPython() throws Exception {
        service.tempoPython();
        return true;
    }

    @GetMapping("/fetchCurrentSeason")
    String fetchCurrentSeason() throws Exception {
        return service.fetchCurrentSeason();
    }
}
