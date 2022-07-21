package sorim.f1.slasher.relentless.model.livetiming;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionData {

    @JsonProperty("Series")
    private List<SessionDataSeries> series;
    @JsonProperty("StatusSeries")
    private List<SessionDataSeries> statusSeries;
}
