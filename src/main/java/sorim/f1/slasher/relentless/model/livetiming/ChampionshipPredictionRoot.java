package sorim.f1.slasher.relentless.model.livetiming;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChampionshipPredictionRoot {
    @JsonProperty("Drivers")
    public Map<String, ChampionshipPrediction> drivers;
    @JsonProperty("Teams")
    public Map<String, ChampionshipPrediction> teams;
}

