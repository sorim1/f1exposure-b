package sorim.f1.slasher.relentless.model.ergast;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.entities.ergast.RaceData;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CircuitTable {

    @JsonProperty("Circuits")
    private List<Circuit> circuits;

}
