package sorim.f1.slasher.relentless.model.livetiming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LapTimeData {
    Integer position;
    Integer lapTimeMs;
    String lapTime;
    Integer driverNumber;
    String driverName;
    Integer lapNumber;
    String sessionName;

    public LapTimeData(Integer driverNumber) {
        this.driverNumber=driverNumber;
    }
}
