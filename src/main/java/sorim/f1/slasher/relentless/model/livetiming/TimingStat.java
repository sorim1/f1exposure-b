package sorim.f1.slasher.relentless.model.livetiming;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimingStat {
    @JsonProperty("Line")
    public Integer line;
    @JsonProperty("RacingNumber")
    public String racingNumber;
    @JsonProperty("PersonalBestLapTime")
    public LapValuePosition personalBestLapTime;
    @JsonProperty("BestSectors")
    public List<LapValuePosition> bestSectors;
    @JsonProperty("BestSpeeds")
    public Map<String, LapValuePosition> bestSpeeds;
    public String name;
}

