package sorim.f1.slasher.relentless.model.ergast;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErgastDriver {

    private String driverId;
    private Integer permanentNumber;
    private String code;
    private String url;
    private String givenName;
    private String familyName;
    private String dateOfBirth;
    private String nationality;

}
