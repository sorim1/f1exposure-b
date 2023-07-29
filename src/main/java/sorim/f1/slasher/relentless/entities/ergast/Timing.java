package sorim.f1.slasher.relentless.entities.ergast;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Timing {
    private String driverId;
    private Integer position;
    private String time;
}
