package sorim.f1.slasher.relentless.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sorim.f1.slasher.relentless.entities.F1Calendar;
import sorim.f1.slasher.relentless.service.AdminService;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("admin")
public class AdminController {

    private final AdminService service;

    @GetMapping("/refreshCalendar")
    boolean refreshCalendar() throws Exception {
        service.refreshCalendar();
        return true;
    }

    @GetMapping("/getCalendar")
    F1Calendar getCalendar() throws Exception {
        return service.getCalendar();
    }

}
