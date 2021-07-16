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
    private String color;
    private BigDecimal score = BigDecimal.ZERO;
    private BigDecimal scoreLegacy = BigDecimal.ZERO;
    private List<List<BigDecimal>> scoresByRound = new ArrayList<>();
    private List<List<BigDecimal>> scoresByRoundLegacy = new ArrayList<>();
    private List<List<BigDecimal>> scoresThroughRounds = new ArrayList<>();
    private List<List<BigDecimal>> scoresThroughRoundsLegacy = new ArrayList<>();
}
