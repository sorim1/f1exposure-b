package sorim.f1.slasher.relentless.model.ergast;

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
public class StandingsTable {

    private String season;
    @JsonProperty("StandingsLists")
    private List<StandingsList> standingsLists;
}
