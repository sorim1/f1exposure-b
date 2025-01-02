package sorim.f1.slasher.relentless.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.entities.DriverStanding;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverCompared {
    private String driverId;
    private String teamId;
    private String driverName;
    private String teamName;
    private BigDecimal points;
    private Integer race = 0;
    private Integer qualifying = 0;
    private Integer bestRace;
    private Integer bestQualifying;
    private Integer dnf = 0;
    private String imageUrl;

    public DriverCompared(DriverStanding driver) {
        this.driverId = driver.getId();
        this.driverName = driver.getName();
        this.teamId = driver.getErgastCode();
        this.teamName = driver.getCar();
        this.points = driver.getPoints();
    }
}
