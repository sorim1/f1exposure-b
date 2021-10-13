package sorim.f1.slasher.relentless.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sorim.f1.slasher.relentless.entities.*;
import sorim.f1.slasher.relentless.model.*;
import sorim.f1.slasher.relentless.service.AdminService;
import sorim.f1.slasher.relentless.service.ExposureStrawpollService;
import sorim.f1.slasher.relentless.service.SecurityService;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("exposure")
@Slf4j
public class ExposureController {

    private final ExposureStrawpollService service;
    private final SecurityService securityService;

    @GetMapping("/initializeStrawpoll/{id}")
    boolean initializeStrawpoll(@RequestHeader String client, @PathVariable("id") String id) throws Exception {
        securityService.validateAdminHeader(client);
        boolean response = service.initializeStrawpoll(id);
        log.info("initializeStrawpoll called:" + response);
        return response;
    }

    @GetMapping("/resetLatestPoll")
    Integer resetLatestPoll(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.resetLatestPoll();
    }

    @GetMapping("/getExposureResults")
    ExposureData getExposedChartData(@RequestHeader String client) throws Exception {
        securityService.validateHeader(client);
        return service.getExposedChartData();
    }

    @GetMapping("/getExposureResultsPartial")
    ActiveExposureChart getActiveExposureChart(@RequestHeader String client) throws Exception {
        securityService.validateHeader(client);
        return service.getActiveExposureChart();
    }


    @GetMapping("/closeExposurePoll")
    void closeExposurePoll(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        service.closeExposurePoll();
    }

    @GetMapping("/startPolling")
    void startPolling(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        service.startPolling();
    }

    @GetMapping("/openExposurePoll/{minutes}")
    void openExposurePoll(@RequestHeader String client, @PathVariable("minutes") Integer minutes) throws Exception {
        securityService.validateAdminHeader(client);
        service.openExposurePoll(minutes);
    }

    @GetMapping("/updateCurrentRound")
    List<Integer> updateCurrentRound(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.updateCurrentRound();
    }

    @GetMapping("/updateCurrentRound/{round}")
    List<Integer> setCurrentRound(@RequestHeader String client, @PathVariable("round") String round) throws Exception {
        securityService.validateAdminHeader(client);
        return service.setCurrentRound(Integer.valueOf(round));
    }

    @GetMapping("/getCurrentRoundUp")
    Integer getCurrentRoundUp(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.getCurrentRoundUp();
    }

    @GetMapping("/getExposureStrawpoll")
    String getExposureStrawpoll(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return service.getExposureStrawpoll();
    }
}
