package sorim.f1.slasher.relentless.model.livetiming;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionDataSeries {
    @JsonProperty("Utc")
    ZonedDateTime utc;
    @JsonProperty("TrackStatus")
    String trackStatus;
    @JsonProperty("SessionStatus")
    String sessionStatus;
    @JsonProperty("Lap")
    Integer lap;
}
