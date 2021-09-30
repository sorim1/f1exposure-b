package sorim.f1.slasher.relentless.model.livetiming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DurationFormatUtils;
import sorim.f1.slasher.relentless.entities.ergast.Timing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class LapByLapData {
    public List<Integer> positions = new ArrayList<>();
    public List<Tyre> tyres = new ArrayList<>();
    public List<Integer> lapTimesX = new ArrayList<>();
    public List<String> lapTimesY = new ArrayList<>();
    public List<Integer> lapTimesYms = new ArrayList<>();
    public List<Integer> totalTimeByLapMs = new ArrayList<>();
    public Integer totalTimeMs = 0;

    public void addLapTime(Integer lapNumber, Timing timing) {
        String[] lapTime = timing.getTime().split(":");
        int miliseconds = Integer.parseInt(lapTime[0])*60000;
        String[] lapTime2 = lapTime[1].split(Pattern.quote("."));
        miliseconds += Integer.parseInt(lapTime2[0])*1000;
        miliseconds += Integer.parseInt(lapTime2[1]);
        lapTimesYms.add(miliseconds);
        lapTimesX.add(lapNumber);
        lapTimesY.add(timing.getTime());
        totalTimeMs = totalTimeMs + miliseconds;
        totalTimeByLapMs.add(totalTimeMs);
    }

}
