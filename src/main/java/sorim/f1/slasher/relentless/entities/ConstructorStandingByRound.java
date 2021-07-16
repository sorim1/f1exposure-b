package sorim.f1.slasher.relentless.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.model.ergast.ErgastStanding;
import sorim.f1.slasher.relentless.util.MainUtility;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
    private Integer points;
    private Integer wins;
    private Integer pointsThisRound = 0;
    private String color;

    public ConstructorStandingByRound(ErgastStanding ergastStanding, Integer season, Integer round) {
        this.id = new EmbeddedStandingId();
        this.id.setSeason(season);
        this.id.setRound(round);
        this.id.setId(ergastStanding.getConstructor().getConstructorId());
        this.position = ergastStanding.getPosition();
        this.name = ergastStanding.getConstructor().getName();
        this.points = ergastStanding.getPoints();
        this.wins = ergastStanding.getWins();
        this.color = MainUtility.getTeamColor(ergastStanding.getConstructor().getConstructorId());
    }

    public void incrementPointsThisRound(Integer value){
        this.pointsThisRound= this.pointsThisRound+value;
    }
}
