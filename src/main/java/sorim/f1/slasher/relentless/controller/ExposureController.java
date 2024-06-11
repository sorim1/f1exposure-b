package sorim.f1.slasher.relentless.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sorim.f1.slasher.relentless.entities.JsonRepositoryModel;
import sorim.f1.slasher.relentless.entities.JsonRepositoryTwoModel;
import sorim.f1.slasher.relentless.model.ExposureData;
import sorim.f1.slasher.relentless.model.FullExposure;
import sorim.f1.slasher.relentless.model.strawpoll.StrawpollModelThree;
import sorim.f1.slasher.relentless.model.strawpoll.StrawpollPoll;
import sorim.f1.slasher.relentless.service.ExposureStrawpollService;
import sorim.f1.slasher.relentless.service.SecurityService;
import sorim.f1.slasher.relentless.service.StrawpollService;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("exposure")
@Slf4j
public class ExposureController {

    private final ExposureStrawpollService exposureService;
    private final StrawpollService strawpollService;
    private final SecurityService securityService;

    @GetMapping("/initializeStrawpoll/{id}")
    String initializeStrawpoll(@RequestHeader String client, @PathVariable("id") String id) throws Exception {
        securityService.validateAdminHeader(client);
        String response = exposureService.initializeStrawpoll(id);
        log.info("initializeStrawpoll called:" + response);
        return response;
    }

    @GetMapping("/setStrawpoll/{id}")
    String setStrawpoll(@RequestHeader String client, @PathVariable("id") String id) throws Exception {
        securityService.validateAdminHeader(client);
        String response = exposureService.setStrawpoll(id);
        log.info("setStrawpoll called:" + response);
        return response;
    }

    @GetMapping("/findAndInitializeStrawpoll")
    String findAndInitializeStrawpoll(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        String response = exposureService.initializeStrawpoll(null);
        log.info("initializeStrawpoll called:" + response);
        return response;
    }

    @GetMapping("/resetLatestPoll")
    Integer resetLatestPoll(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return exposureService.resetLatestPoll();
    }

    @GetMapping("/getExposureResults")
    ExposureData getExposedChartData(@RequestHeader String client) throws Exception {
        securityService.validateHeader(client);
        return exposureService.getExposedChartData();
    }

    @GetMapping("/getSingleExposureResult/{season}/{round}")
    Object getSingleExposureResult(@RequestHeader String client, @PathVariable("season") Integer season, @PathVariable("round") Integer round) throws Exception {
        securityService.validateHeader(client);
        return exposureService.getSingleExposureResult(season, round);
    }

    @GetMapping("/archiveExposureData")
    JsonRepositoryModel archiveExposureData(@RequestHeader String client) throws Exception {
        securityService.validateHeader(client);
        return exposureService.archiveExposureData();
    }


    @GetMapping("/closeExposurePoll")
    void closeExposurePoll(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        exposureService.closeExposurePoll(true);
    }

    @GetMapping("/startPolling")
    void startPolling(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        exposureService.startPolling();
    }

    @GetMapping("/openExposurePoll/{minutes}")
    void openExposurePoll(@RequestHeader String client, @PathVariable("minutes") Integer minutes) throws Exception {
        securityService.validateAdminHeader(client);
        exposureService.openExposurePoll(minutes);
    }

    @GetMapping("/incrementExposureRound")
    List<Integer> incrementExposureRound(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return exposureService.incrementExposureRound();
    }

    @GetMapping("/updateCurrentRound/{round}")
    List<Integer> setCurrentRound(@RequestHeader String client, @PathVariable("round") String round) throws Exception {
        securityService.validateAdminHeader(client);
        return exposureService.setCurrentRound(Integer.valueOf(round));
    }

    @GetMapping("/getCurrentRoundUp")
    Integer getCurrentRoundUp(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return exposureService.getCurrentRoundUp();
    }

    @GetMapping("/getExposureStrawpoll")
    String getExposureStrawpoll(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return exposureService.getExposureStrawpoll();
    }

    @GetMapping("/showWinner/{value}")
    String showWinner(@RequestHeader String client, @PathVariable("value") Boolean value) throws Exception {
        securityService.validateAdminHeader(client);
        return exposureService.changeShowWinner(value);
    }

    @GetMapping("/backupExposure")
    FullExposure backupExposure(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return exposureService.backupExposure();
    }

    @PostMapping("/restoreExposureFromBackup")
    Boolean restoreExposureFromBackup(@RequestHeader String client, @RequestBody FullExposure body) throws Exception {
        securityService.validateAdminHeader(client);
        return exposureService.restoreExposureFromBackup(body);
    }

    @GetMapping("/generateStrawpoll")
    StrawpollModelThree generateStrawpoll(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return strawpollService.generateStrawpoll();
    }

    @GetMapping("/postStrawpoll")
    StrawpollPoll postStrawpoll(@RequestHeader String client) throws Exception {
        securityService.validateHeader(client);
        log.info("POKUSAJ KREIRANJA STRAWPOLLA");
        if(exposureService.checkIfStrawpollCanBeStarted()){
            exposureService.setExposureNow(true);
            StrawpollPoll poll = strawpollService.postStrawpoll();
            exposureService.setStrawpoll(poll.getId());
            return poll;
        } else {
            log.info("POKUSAJ KREIRANJA STRAWPOLLA NIJE DOZVOLJEN");
        }
        return null;
    }

    @GetMapping("/postStrawpollForced")
    StrawpollPoll postStrawpollForced(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        exposureService.setExposureNow(true);
        StrawpollPoll poll = strawpollService.postStrawpoll();
        exposureService.setStrawpoll(poll.getId());
        return poll;
    }

    @GetMapping("/postStrawpollWithoutSubscribing")
    StrawpollPoll postStrawpollWithoutSubscribing(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return strawpollService.postStrawpoll();
    }

    @GetMapping("/getStrawpoll")
    JsonRepositoryTwoModel getStrawpoll(@RequestHeader String client) throws Exception {
        securityService.validateAdminHeader(client);
        return strawpollService.getStrawpoll();
    }

    @PostMapping("/saveStrawpoll")
    StrawpollModelThree saveStrawpoll(@RequestHeader String client, @RequestBody StrawpollModelThree body) throws Exception {
        securityService.validateAdminHeader(client);
        return strawpollService.saveStrawpoll(body);
    }

    @GetMapping("/getLapCount/{month}")
    Integer getLapCount(@RequestHeader String client, @PathVariable("value") Integer month) throws Exception {
        securityService.validateAdminHeader(client);
        return exposureService.getLapCount(2024, month);
    }

}
