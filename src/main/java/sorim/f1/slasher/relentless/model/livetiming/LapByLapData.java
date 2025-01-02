package sorim.f1.slasher.relentless.model.livetiming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sorim.f1.slasher.relentless.entities.ergast.Timing;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class LapByLapData {
    public List<Integer> positions = new ArrayList<>();
    public List<TimingAppDataStint> tyres = new ArrayList<>();
    public List<Integer> lapTimesX = new ArrayList<>();
    public List<String> lapTimesY = new ArrayList<>();
    public List<Integer> lapTimesYms = new ArrayList<>();
    public List<Integer> lapTimePositions = new ArrayList<>();
    public List<Integer> totalTimeByLapMs = new ArrayList<>();
    public Integer totalTimeMs = 0;

    public Integer addLapTime(Integer lapNumber, Timing timing) {
        String[] lapTime = timing.getTime().split(":");
        int miliseconds = Integer.parseInt(lapTime[0]) * 60000;
        String[] lapTime2 = lapTime[1].split(Pattern.quote("."));
        miliseconds += Integer.parseInt(lapTime2[0]) * 1000;
        miliseconds += Integer.parseInt(lapTime2[1]);
        lapTimesYms.add(miliseconds);
        lapTimesX.add(lapNumber);
        lapTimesY.add(timing.getTime());
        totalTimeMs = totalTimeMs + miliseconds;
        totalTimeByLapMs.add(totalTimeMs);

        positions.add(timing.getPosition());

        return miliseconds;
    }

    public void addLapTimePosition(String code, Integer lapNumber, Integer lapTimePosition) {
        if (lapNumber == lapTimePositions.size() + 1) {
            lapTimePositions.add(lapTimePosition);
        } else if (lapNumber > lapTimePositions.size() + 1) {
            log.error("VECI PROBLEM1: {} vs {}", lapNumber, lapTimePositions.size() + 1);
            for (int i = 0; i < lapNumber - lapTimePositions.size() + 1; i++) {
                lapTimePositions.add(null);
            }
        } else if (lapNumber < lapTimePositions.size() + 1) {
            log.error("MANJI PROBLEM1: {} vs {}", lapNumber, lapTimePositions.size() + 1);
            lapTimePositions.set(lapNumber - 1, lapTimePosition);
        } else {
            log.error("NEMOGUCE1");
        }

    }

}
