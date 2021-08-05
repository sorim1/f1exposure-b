package sorim.f1.slasher.relentless.entities.ergast;

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
public class Lap {
    private Integer number;
    @JsonProperty("Timings")
    private List<Timing> timings;
}
