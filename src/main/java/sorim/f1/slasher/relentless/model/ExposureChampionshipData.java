package sorim.f1.slasher.relentless.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExposureChampionshipData {
    private String code;
    private String name;
    private String color;
    private BigDecimal score = BigDecimal.ZERO;
    private List<List<BigDecimal>> scoresByRound = new ArrayList<>();
    private List<List<BigDecimal>> scoresThroughRounds = new ArrayList<>();
    private Integer maxExposureRound;
    private BigDecimal maxExposure = BigDecimal.ZERO;

    public void updateMaxExposure(Integer round, BigDecimal exposure) {
        if(exposure.compareTo(maxExposure) > 0){
            this.maxExposure=exposure;
            this.maxExposureRound=round;
        }
    }
}
