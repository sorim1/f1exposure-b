package sorim.f1.slasher.relentless.model.livetiming;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RaceControlMessage {
    @JsonProperty("Utc")
    public Date utc;
    @JsonProperty("Lap")
    public Integer lap;
    @JsonProperty("Category")
    public String category;
    @JsonProperty("Flag")
    public String flag;
    @JsonProperty("Scope")
    public String scope;
    @JsonProperty("RacingNumber")
    public String racingNumber;
    @JsonProperty("Message")
    public String message;
    @JsonProperty("Status")
    public String status;
    @JsonProperty("Sector")
    public Integer sector;
    public String name;

}

