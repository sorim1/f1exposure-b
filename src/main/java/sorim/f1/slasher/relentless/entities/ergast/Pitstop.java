package sorim.f1.slasher.relentless.entities.ergast;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pitstop {
    private String driverId;
    private Integer lap;
}
