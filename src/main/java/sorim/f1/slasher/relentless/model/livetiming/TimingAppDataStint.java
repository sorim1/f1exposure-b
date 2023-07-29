package sorim.f1.slasher.relentless.model.livetiming;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TimingAppDataStint {
    @JsonProperty("LapTime")
    String lapTime;
    @JsonProperty("LapNumber")
    Integer lapNumber;
    @JsonProperty("Compound")
    String compound;
    @JsonProperty("TotalLaps")
    Integer totalLaps;
    @JsonProperty("StartLaps")
    Integer startLaps;

}
