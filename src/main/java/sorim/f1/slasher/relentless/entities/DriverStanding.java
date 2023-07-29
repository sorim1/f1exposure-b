package sorim.f1.slasher.relentless.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.model.ergast.ErgastStanding;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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

    //rename to teamCode (and liquibase)
    private String ergastCode;
    private String nationality;
    private String car = "";
    private BigDecimal points;
    private Integer wins;
    private Integer permanentNumber;
    private String driverUrl;
    private String constructorUrl;

    public DriverStanding(ErgastStanding ergastStanding) {
        this.id = ergastStanding.getDriver().getDriverId();
        this.position = ergastStanding.getPosition();
        this.name = ergastStanding.getDriver().getGivenName() + " " + ergastStanding.getDriver().getFamilyName();
        if (ergastStanding.getDriver().getCode() != null) {
            this.code = ergastStanding.getDriver().getCode();
        } else {
            this.code = ergastStanding.getDriver().getDriverId();
        }
        this.ergastCode = ergastStanding.getConstructors().get(ergastStanding.getConstructors().size() - 1).getConstructorId();
        this.driverUrl = ergastStanding.getDriver().getUrl();
        this.nationality = ergastStanding.getDriver().getNationality();

        ergastStanding.getConstructors().forEach(constructor -> {
            this.car = this.car + constructor.getName() + " / ";
        });
        if (this.car.length() > 0) {
            this.car = this.car.substring(0, this.car.length() - 3);
        }

        this.constructorUrl = ergastStanding.getConstructors().get(ergastStanding.getConstructors().size() - 1).getUrl();
        this.points = ergastStanding.getPoints();
        this.wins = ergastStanding.getWins();
        this.permanentNumber = ergastStanding.getDriver().getPermanentNumber();
    }
}
