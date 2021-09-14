package sorim.f1.slasher.relentless.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.entities.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FullExposure {

    private List<ExposedVote> exposedVote;
    private List<Exposed> exposed;
    private List<ExposureChampionship> exposureChampionship;
    private List<ExposureChampionshipStanding> exposureChampionshipStandings;
    private List<ExposedVoteTotals> exposedVoteTotals;
}
