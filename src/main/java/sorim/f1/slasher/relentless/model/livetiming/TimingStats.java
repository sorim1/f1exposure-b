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
public class TimingStats {
    @JsonProperty("Withheld")
    public Boolean withheld;
    @JsonProperty("Lines")
    public Map<String, TimingStat> lines;
    @JsonProperty("SessionType")
    public String sessionType;

}

