package sorim.f1.slasher.relentless.model.livetiming;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionDataSeries {
    @JsonProperty("Utc")
    LocalDateTime utc;
  //  LocalDateTime ZonedDateTime
    @JsonProperty("TrackStatus")
    String trackStatus;
    @JsonProperty("SessionStatus")
    String sessionStatus;
    @JsonProperty("Lap")
    Integer lap;
}
