package sorim.f1.slasher.relentless.model.livetiming;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LapSeries {
    @JsonProperty("RacingNumber")
    public String racingNumber;
    @JsonProperty("LapPosition")
    public List<String> lapPosition;
    public String name;
}

