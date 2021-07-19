package sorim.f1.slasher.relentless.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.model.ergast.ErgastStanding;
import sorim.f1.slasher.relentless.util.MainUtility;

import javax.persistence.*;

@Entity
@Table(name = "DRIVER_STANDINGS_BY_ROUND")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverStandingByRound {

    @EmbeddedId
    private EmbeddedStandingId id = new EmbeddedStandingId();
    private Integer position;
    private String name;
    private String code;
    private String nationality;
    private String car;
    private Integer points;
    private Integer pointsThisRound = 0;
    private Integer resultThisRound;
    private Integer wins;
    private Integer permanentNumber;
    private String color;
    @Transient
    private String firstName;
    @Transient
    private String lastName;

    public DriverStandingByRound(ErgastStanding ergastStanding, Integer season, Integer round) {
        this.id = new EmbeddedStandingId();
        this.id.setSeason(season);
        this.id.setRound(round);
        this.id.setId(ergastStanding.getDriver().getDriverId());
        this.position = ergastStanding.getPosition();
        this.name = ergastStanding.getDriver().getGivenName() + " " + ergastStanding.getDriver().getFamilyName();
        this.code = ergastStanding.getDriver().getCode();
        this.nationality = ergastStanding.getDriver().getNationality();
        this.car = ergastStanding.getConstructors().get(0).getName();
        this.points = ergastStanding.getPoints();
        this.wins = ergastStanding.getWins();
        this.permanentNumber = ergastStanding.getDriver().getPermanentNumber();
        this.color = MainUtility.getTeamColor(ergastStanding.getConstructors().get(0).getConstructorId());
    }

    public void incrementPointsThisRound(Integer value){
        this.pointsThisRound= this.pointsThisRound+value;
    }
}
