package sorim.f1.slasher.relentless.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sorim.f1.slasher.relentless.entities.Marketing;
import sorim.f1.slasher.relentless.service.MarketingService;
import sorim.f1.slasher.relentless.service.SecurityService;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@RequestMapping("promotion")
public class MarketingController {

    private final SecurityService securityService;
    private final MarketingService marketingService;

    @GetMapping("/getRandomPromotion")
    public Marketing getRandomMarketing(@RequestHeader String client) throws Exception {
        securityService.validateHeader(client);
        return marketingService.getRandomMarketing();
    }

    @GetMapping("/getMarketing/{id}")
    public Marketing getMarketing(@RequestHeader String client, @PathVariable("id") Integer id) throws Exception {
        log.info("getMarketing: {}", id);
        securityService.validateAdminHeader(client);
        return marketingService.getMarketing(id);
    }

    @GetMapping("/deleteMarketing/{id}")
    public boolean deleteMarketing(@RequestHeader String client, @PathVariable("id") Integer id) throws Exception {
        log.info("deleteMarketing: {}", id);
        securityService.validateAdminHeader(client);
        marketingService.deleteMarketing(id);
        return true;
    }

    @PostMapping("/saveMarketing")
    public Marketing saveMarketing(@RequestHeader String client, @RequestBody Marketing marketing) throws Exception {
        log.info("saveMarketing");
        securityService.validateAdminHeader(client);
        return marketingService.saveMarketing(marketing);
    }

    @GetMapping("/backupMarketing")
    public List<Marketing> backupMarketing(@RequestHeader String client) throws Exception {
        log.info("backupMarketing");
        securityService.validateAdminHeader(client);
        return marketingService.backupMarketing();
    }

    @PostMapping("/restoreMarketing")
    public Boolean restoreMarketing(@RequestHeader String client, @RequestBody List<Marketing> marketings) throws Exception {
        log.info("restoreMarketing");
        securityService.validateAdminHeader(client);
        return marketingService.restoreMarketing(marketings);
    }
}
