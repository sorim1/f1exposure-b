package sorim.f1.slasher.relentless.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sorim.f1.slasher.relentless.entities.ExposedChart;
import sorim.f1.slasher.relentless.entities.Tempo;
import sorim.f1.slasher.relentless.service.MainService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Controller {

    private final MainService service;

    @GetMapping("/sorim")
    Tempo getTempo() {
        return service.getTempo();
    }

    @PostMapping("/expose")
    Boolean exposeDrivers(@RequestHeader String authorization, @RequestBody String[] exposedList, HttpServletRequest request) throws Exception {
        service.validateHeader(authorization);
        String ipAddress = service.validateIp(request);
        return service.exposeDrivers(exposedList, ipAddress);
    }

    @GetMapping("/exposed")
    ExposedChart getExposedChartData(@RequestHeader String authorization) throws Exception {
        service.validateHeader(authorization);
        return service.getExposedChartData();
    }
}
