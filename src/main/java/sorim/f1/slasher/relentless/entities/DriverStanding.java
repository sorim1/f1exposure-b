package sorim.f1.slasher.relentless.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.model.ergast.ErgastStanding;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;

@Entity
@Table(name = "CURRENT_DRIVER_STANDINGS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverStanding {
    @Id
    private String id;
    private Integer position;
    private String name;
    private String code;
    private String ergastCode;
    private String nationality;
    private String car;
    private BigDecimal points;
    private Integer wins;
    private Integer permanentNumber;
    private String driverUrl;
    private String constructorUrl;

    @Transient
    private String firstName;
    @Transient
    private String lastName;

    public DriverStanding(ErgastStanding ergastStanding) {
        this.id = ergastStanding.getDriver().getDriverId();
        this.position = ergastStanding.getPosition();
        this.name = ergastStanding.getDriver().getGivenName() + " " + ergastStanding.getDriver().getFamilyName();
        this.code = ergastStanding.getDriver().getCode();
        this.ergastCode = ergastStanding.getDriver().getDriverId();
        this.driverUrl = ergastStanding.getDriver().getUrl();
        this.nationality = ergastStanding.getDriver().getNationality();
        this.car = ergastStanding.getConstructors().get(0).getName();
        this.constructorUrl = ergastStanding.getConstructors().get(0).getUrl();
        this.points = ergastStanding.getPoints();
        this.wins = ergastStanding.getWins();
        this.permanentNumber = ergastStanding.getDriver().getPermanentNumber();
    }
}
