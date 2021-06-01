package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.entities.F1Calendar;

public interface AdminService {
    void refreshCalendar() throws Exception;

    F1Calendar getCalendar();
}
