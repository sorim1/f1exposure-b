package sorim.f1.slasher.relentless.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.entities.SportSurgeEvent;
import sorim.f1.slasher.relentless.entities.SportSurgeStream;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SportSurge {

    private List<SportSurgeEvent> events;
    private List<SportSurgeStream> streams;

}
