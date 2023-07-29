package sorim.f1.slasher.relentless.model.ergast;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StandingsList {

    private Integer season;
    private Integer round;
    @JsonProperty("DriverStandings")
    private List<ErgastStanding> driverStandings;
    @JsonProperty("ConstructorStandings")
    private List<ErgastStanding> constructorStandings;
}
