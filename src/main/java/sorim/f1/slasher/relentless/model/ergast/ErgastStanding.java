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
public class ErgastStanding {

    private Integer position;
    private Integer points;
    private Integer wins;
    @JsonProperty("Driver")
    private ErgastDriver driver;
    @JsonProperty("Constructors")
    private List<ErgastConstructor> constructors;
    @JsonProperty("Constructor")
    private ErgastConstructor constructor;
}
