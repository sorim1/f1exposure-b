package sorim.f1.slasher.relentless.model.livetiming;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChampionshipPrediction {
    @JsonProperty("TeamName")
    public String teamName;
    @JsonProperty("RacingNumber")
    public String racingNumber;
    @JsonProperty("CurrentPosition")
    public Integer currentPosition;
    @JsonProperty("PredictedPosition")
    public Integer predictedPosition;
    @JsonProperty("CurrentPoints")
    public Integer currentPoints;
    @JsonProperty("PredictedPoints")
    public Integer predictedPoints;
    public String name;
}

