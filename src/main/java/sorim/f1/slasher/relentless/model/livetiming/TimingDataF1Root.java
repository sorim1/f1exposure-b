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
public class TimingDataF1Root {
    @JsonProperty("NoEntries")
    public List<Integer> noEntries;
    @JsonProperty("SessionPart")
    public Integer sessionPart;
    @JsonProperty("CutOffTime")
    public String cutOffTime;
    @JsonProperty("CutOffPercentage")
    public String cutOffPercentage;
    @JsonProperty("Lines")
    public Map<String, TimingDataF1> lines;
    @JsonProperty("Withheld")
    public Boolean withheld;
}

