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
public class LapValuePosition {
    @JsonProperty("Value")
    public String value;
    @JsonProperty("Lap")
    public Integer lap;
    @JsonProperty("Position")
    public Integer position;
}



