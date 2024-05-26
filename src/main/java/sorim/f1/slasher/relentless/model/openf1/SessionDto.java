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
public class SessionDto {
    private Integer circuitKey;
    private String circuitShortName;
    private String countryCode;
    private Integer countryKey;
    private String countryName;
    private String dateEnd;
    private String dateStart;
    private String gmtOffset;
    private String location;
    private Integer meetingKey;
    private Integer sessionKey;
    private String sessionName;
    private String sessionType;
    private Integer year;
}
