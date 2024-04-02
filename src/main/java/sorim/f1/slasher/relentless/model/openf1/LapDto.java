package sorim.f1.slasher.relentless.model.openf1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LapDto {
    private LocalDateTime dateStart;
    private Integer driverNumber;
    private Double durationSector1;
    private Double durationSector2;
    private Double durationSector3;
    private Integer i1Speed;
    private Integer i2Speed;
    private Boolean isPitOutLap;
    private Double lapDuration;
    private Integer lapNumber;
    private Integer meetingKey;
    private List<Integer> segmentsSector1;
    private List<Integer> segmentsSector2;
    private List<Integer> segmentsSector3;
    private Integer sessionKey;
    private Integer stSpeed;
}
