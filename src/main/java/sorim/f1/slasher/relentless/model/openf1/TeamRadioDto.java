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
public class TeamRadioDto {
    private LocalDateTime date;
    private Integer driverNumber;
    private Integer meetingKey;
    private String recordingUrl;
    private Integer sessionKey;
}
