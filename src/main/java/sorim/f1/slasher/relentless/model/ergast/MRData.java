package sorim.f1.slasher.relentless.model.ergast;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MRData {

    private String xmlns;
    private String series;
    private String url;
    private String limit;
    private String offset;

    @JsonProperty("RaceTable")
    private RaceTable raceTable;

    @JsonProperty("StandingsTable")
    private StandingsTable standingsTable;

}
