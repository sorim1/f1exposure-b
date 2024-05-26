package sorim.f1.slasher.relentless.model.openf1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntervalDto {

    private String date;
    private Integer driverNumber;
    private Double gapToLeader;
    private Double interval;
    private Integer meetingKey;
    private Integer sessionKey;
}
