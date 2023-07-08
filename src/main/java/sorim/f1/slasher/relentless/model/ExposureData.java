package sorim.f1.slasher.relentless.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.entities.ExposureChampionshipStanding;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExposureData {
    private String title;
    private Integer currentYear;
    private Integer delay;
    private ActiveExposureChart activeExposureChart;
    private List<ExposureChampionshipData> exposureChampionshipData;
    private List<ExposureChampionshipStanding> standings;
    private List<Integer> voters;
    private Object exposureRaces;
    private Boolean showWinner;
}
