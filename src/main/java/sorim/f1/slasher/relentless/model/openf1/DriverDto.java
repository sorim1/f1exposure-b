package sorim.f1.slasher.relentless.model.openf1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverDto {
    private String broadcastName;
    private String countryCode;
    private Integer driverNumber;
    private String firstName;
    private String fullName;
    private String headshotUrl;
    private String lastName;
    private Integer meetingKey;
    private String nameAcronym;
    private Integer sessionKey;
    private String teamColour;
    private String teamName;
}
