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
public class CarDataDto {

    private Integer brake;
    private LocalDateTime date;
    private Integer driverNumber;
    private Integer drs;
    private Integer meetingKey;
    private Integer nGear;
    private Integer rpm;
    private Integer sessionKey;
    private Integer speed;
    private Integer throttle;
}
