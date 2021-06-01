package sorim.f1.slasher.relentless.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sorim.f1.slasher.relentless.model.ExposedChart;
import sorim.f1.slasher.relentless.service.ClientService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClientController {

    private final ClientService service;

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
