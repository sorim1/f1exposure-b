package sorim.f1.slasher.relentless.entities.ergast;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import sorim.f1.slasher.relentless.model.ergast.Circuit;
import sorim.f1.slasher.relentless.model.ergast.ErgastStanding;
import sorim.f1.slasher.relentless.model.livetiming.RaceAnalysis;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ERGAST_CURRENT_SEASON_RACES")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Race {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Integer id;
    private Integer round;
    private String season;
    private String url;
    private String raceName;
    private String date;
    private String time;

    @Transient
    @JsonProperty("Laps")
    private List<Lap> laps;

    @JsonProperty("Circuit")
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Circuit circuit;

    private String liveTiming;
    private String timingAppData;
    private String circuitId;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private RaceAnalysis raceAnalysis;

    @JsonProperty("Results")
    @Transient
    private List<ErgastStanding> results;
}
