package sorim.f1.slasher.relentless.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActiveExposureChart {

    private String[] drivers;
    private String[] driverNames;
    private Integer[] results;
    private BigDecimal[] exposure;
    private BigDecimal[] exposureLegacy;
    private Integer voters;
    private Integer votes;
    private Integer season;
    private Integer round;
}
