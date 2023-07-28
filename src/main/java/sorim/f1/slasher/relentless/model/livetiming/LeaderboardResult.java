package sorim.f1.slasher.relentless.model.livetiming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sorim.f1.slasher.relentless.util.MainUtility;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class LeaderboardResult {
    private String driverCode;
    private String driverName;
    private String team;
    private String finalGap;
    private String fastestLap;
    private Integer position;

    public LeaderboardResult(List<String> data) {
        this.driverCode = data.get(0);
        this.driverName = MainUtility.getDriverNameFromCode(this.driverCode);
        this.team = data.get(2);
        this.position = Integer.valueOf(data.get(3));
        this.finalGap = data.get(4);
    }
}
