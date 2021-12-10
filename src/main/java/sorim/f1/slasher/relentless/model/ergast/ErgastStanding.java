package sorim.f1.slasher.relentless.model.ergast;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErgastStanding {

    private Integer position;
    private BigDecimal points;
    private Integer wins;
    private String status;

    @JsonProperty("Driver")
    private ErgastDriver driver;
    @JsonProperty("Constructors")
    private List<ErgastConstructor> constructors;
    @JsonProperty("Constructor")
    private ErgastConstructor constructor;
}
