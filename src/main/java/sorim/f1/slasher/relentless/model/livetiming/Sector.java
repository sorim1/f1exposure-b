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
public class Sector {
    @JsonProperty("Stopped")
    public Boolean stopped;
    @JsonProperty("PreviousValue")
    public String previousValue;
    @JsonProperty("Segments")
    public Object segments;
    @JsonProperty("Value")
    public String value;
    @JsonProperty("Status")
    public Integer status;
    @JsonProperty("OverallFastest")
    public Boolean overallFastest;
    @JsonProperty("PersonalFastest")
    public Boolean personalFastest;
}



