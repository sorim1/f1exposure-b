package sorim.f1.slasher.relentless.model.livetiming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.model.ergast.ErgastStanding;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BasicStanding {
    private Integer position;
    private String driverCode;
    private String driver;
    private String constructorName;

    public BasicStanding(ErgastStanding ergastStanding) {
        this.position = ergastStanding.getPosition();
        this.driverCode = ergastStanding.getDriver().getDriverId();
        this.driver = ergastStanding.getDriver().getGivenName() + " " + ergastStanding.getDriver().getFamilyName();
        this.constructorName = ergastStanding.getConstructor().getName();
    }
}
