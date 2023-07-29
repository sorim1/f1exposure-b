package sorim.f1.slasher.relentless.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.model.ergast.ErgastStanding;
import sorim.f1.slasher.relentless.util.MainUtility;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "CONSTRUCTOR_STANDINGS_BY_ROUND")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConstructorStandingByRound {

    @EmbeddedId
    private EmbeddedStandingId id;
    private String name;
    private Integer position;
    private BigDecimal points;
    private Integer wins;
    private BigDecimal pointsThisRound;
    private String color;

    public ConstructorStandingByRound(ErgastStanding ergastStanding, Integer season, Integer round) {
        this.setBaseData(ergastStanding, season, round);
        this.position = ergastStanding.getPosition();
        this.points = ergastStanding.getPoints();
    }

    public ConstructorStandingByRound(ErgastStanding ergastStanding, Integer season, Integer round, Boolean containsStanding) {
        this.setBaseData(ergastStanding, season, round);
        if (containsStanding) {
            this.position = ergastStanding.getPosition();
            this.points = ergastStanding.getPoints();
        }
    }

    private void setBaseData(ErgastStanding ergastStanding, Integer season, Integer round) {
        this.id = new EmbeddedStandingId();
        this.id.setSeason(season);
        this.id.setRound(round);
        this.id.setId(ergastStanding.getConstructor().getConstructorId());
        this.name = ergastStanding.getConstructor().getName();
        this.wins = ergastStanding.getWins();
        this.color = MainUtility.getTeamColor(ergastStanding.getConstructor().getConstructorId());
    }

    public void incrementPointsThisRound(BigDecimal value) {
        if (this.pointsThisRound == null) {
            this.pointsThisRound = value;
        } else {
            this.pointsThisRound = this.pointsThisRound.add(value);
        }
    }
}
