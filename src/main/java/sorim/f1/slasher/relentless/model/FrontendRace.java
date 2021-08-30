package sorim.f1.slasher.relentless.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.entities.ergast.Race;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FrontendRace {

    private Integer round;
    private String raceName;
    private String url;
    private String date;
    private String circuitName;
    private String x;
    private String y;

    public FrontendRace(Race race) {
        this.round=race.getRound();
        this.raceName=race.getRaceName();
        this.url = race.getUrl();
        this.date = race.getDate();
        this.circuitName = race.getCircuit().getCircuitName();
        this.x = race.getCircuit().getLocation().getLat();
        this.y = race.getCircuit().getLocation().getLongitude();
    }
}
