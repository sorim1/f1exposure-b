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
public class RaceControlDto {
    private String category;
    private String date;
    private Integer driverNumber;
    private String flag;
    private Integer lapNumber;
    private Integer meetingKey;
    private String message;
    private String scope;
    private Integer sector;
    private Integer sessionKey;
}
