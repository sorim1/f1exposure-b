package sorim.f1.slasher.relentless.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import sorim.f1.slasher.relentless.entities.ergast.Race;
import sorim.f1.slasher.relentless.model.ergast.Circuit;
import sorim.f1.slasher.relentless.model.ergast.ErgastStanding;
import sorim.f1.slasher.relentless.model.livetiming.RaceAnalysis;

import javax.persistence.*;
import java.util.List;

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
