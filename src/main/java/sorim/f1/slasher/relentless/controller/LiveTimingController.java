package sorim.f1.slasher.relentless.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sorim.f1.slasher.relentless.service.LiveTimingService;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("liveTiming")
public class LiveTimingController {

    private final LiveTimingService service;
    //TODO validacija, zasebni key za admin sučelje, ovo je isto admin-only kontroler

    @GetMapping("/getLatestRaceData")
    boolean getLatestRaceData() throws Exception {
        service.getLatestRaceData();
        return true;
    }

}
