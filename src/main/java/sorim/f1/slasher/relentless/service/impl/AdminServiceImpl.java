package sorim.f1.slasher.relentless.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.util.MapTimeZoneCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sorim.f1.slasher.relentless.entities.F1Calendar;
import sorim.f1.slasher.relentless.repository.CalendarRepository;
import sorim.f1.slasher.relentless.service.AdminService;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminServiceImpl implements AdminService {

    private static final String CALENDAR_URL = "https://www.formula1.com/calendar/Formula_1_Official_Calendar.ics";
    private final CalendarRepository calendarRepository;

    @Override
    public void refreshCalendar() throws Exception {
        System.setProperty("net.fortuna.ical4j.timezone.cache.impl", MapTimeZoneCache.class.getName());
        URL url = new URL(CALENDAR_URL);
        Reader r = new InputStreamReader(url.openStream());
        CalendarBuilder builder = new CalendarBuilder();
        Calendar calendar = builder.build(r);
        int counter = 1;
        int raceId = 0;
        int currentRaceId;
        List<F1Calendar> f1calendarList = new ArrayList<>();
        F1Calendar f1Calendar = null;
        for (CalendarComponent component : calendar.getComponents().getAll()) {
            if (component.getName().equals("VEVENT")) {
                PropertyList properties = component.getProperties();
                String[] idAndRound = properties.get("UID").get(0).getValue().split("@");
                currentRaceId = Integer.parseInt(idAndRound[1]);
                if (currentRaceId == raceId) {
                    f1Calendar.setDateFromRoundDescription(idAndRound[0], properties.get("DTSTART").get(0).getValue());
                } else {
                    if (f1Calendar != null) {
                        f1calendarList.add(f1Calendar);
                    }
                    raceId = currentRaceId;
                    f1Calendar = new F1Calendar(properties);
                }
            }
        }
        log.info("size123: {}", f1calendarList.size());
        calendarRepository.saveAll(f1calendarList);

    }

    @Override
    public F1Calendar getCalendar() {
        return calendarRepository.findById(4);
    }

}
