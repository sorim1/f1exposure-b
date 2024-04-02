package sorim.f1.slasher.relentless.model.openf1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StintDto {
    private String compound;
    private Integer driverNumber;
    private Integer lapEnd;
    private Integer lapStart;
    private Integer meetingKey;
    private Integer sessionKey;
    private Integer stintNumber;
    private Integer tyreAgeAtStart;
}
