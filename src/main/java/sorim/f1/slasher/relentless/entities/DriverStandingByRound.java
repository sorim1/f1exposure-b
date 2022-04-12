package sorim.f1.slasher.relentless.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.model.ergast.ErgastStanding;
import sorim.f1.slasher.relentless.util.MainUtility;

import javax.persistence.*;
import java.math.BigDecimal;

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
    private BigDecimal points;
    private BigDecimal pointsThisRound;
    private Integer resultThisRound;
    private Integer resultThisRoundDnf;
    private Integer wins;
    private Integer permanentNumber;
    private String color;
    @Transient
    private String firstName;
    @Transient
    private String lastName;

    private Integer grid;
    private String status;
    private String resultThisRoundText;

    public DriverStandingByRound(ErgastStanding ergastStanding, Integer season, Integer round) {
        this.basicData(ergastStanding, season, round);
        this.position = ergastStanding.getPosition();
    }

    public DriverStandingByRound(ErgastStanding ergastStanding, Integer season, Integer round, Boolean positionBool) {
        this.basicData(ergastStanding, season, round);
        if(positionBool){
            this.position = ergastStanding.getPosition();
        }
    }

    public void incrementPointsThisRound(BigDecimal value){
        if(this.pointsThisRound==null){
            this.pointsThisRound=value;
        } else {
            this.pointsThisRound = this.pointsThisRound.add(value);
        }
    }

    private void basicData(ErgastStanding ergastStanding, Integer season, Integer round) {
        this.id = new EmbeddedStandingId();
        this.id.setSeason(season);
        this.id.setRound(round);
        this.id.setId(ergastStanding.getDriver().getDriverId());
        this.name = ergastStanding.getDriver().getGivenName() + " " + ergastStanding.getDriver().getFamilyName();
        if(ergastStanding.getDriver().getCode()!=null) {
            this.code = ergastStanding.getDriver().getCode();
        } else {
            this.code = ergastStanding.getDriver().getDriverId();
        }
        this.nationality = ergastStanding.getDriver().getNationality();
        if(ergastStanding.getConstructors()!=null) {
            this.car = ergastStanding.getConstructors().get(0).getName();
            this.color = MainUtility.getTeamColor(ergastStanding.getConstructors().get(0).getConstructorId());;
        }
        this.points = ergastStanding.getPoints();
        this.wins = ergastStanding.getWins();
        this.permanentNumber = ergastStanding.getDriver().getPermanentNumber();

    }

    public void setDataFromARound(ErgastStanding ergastStanding, Integer maxPosition) {
        this.pointsThisRound = ergastStanding.getPoints();
        this.resultThisRoundDnf = ergastStanding.getPosition();
//        if("Finished".equals(ergastStanding.getStatus())|| ergastStanding.getStatus().contains("Lap")){
//            this.resultThisRound = ergastStanding.getPosition();
//        } else {
//            this.resultThisRound = maxPosition+1;
//        }
        this.resultThisRound = ergastStanding.getPosition();
        this.grid = ergastStanding.getGrid();
        this.status = ergastStanding.getStatus();
        this.resultThisRoundText = ergastStanding.getPositionText();
    }

}
