package sorim.f1.slasher.relentless.model.livetiming;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionInfo {
    @JsonProperty("ArchiveStatus")
    private ArchiveStatus archiveStatus;

    @JsonProperty("Type")
    private String type;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Path")
    private String path;

    @JsonProperty("StartDate")
    private LocalDateTime startDate;

    @JsonProperty("EndDate")
    private LocalDateTime endDate;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class ArchiveStatus {
        @JsonProperty("Status")
        private String status;
    }
}
