package sorim.f1.slasher.relentless.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.entities.ConstructorStanding;
import sorim.f1.slasher.relentless.entities.DriverStanding;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllStandings {

    List<DriverStanding> driverStandings;
    List<ConstructorStanding> constructorStandings;
    List<ChartSeries> driverStandingByRound;
    List<ChartSeries> constructorStandingByRound;
    List<ChartSeries> driverPointsByRound;
    List<ChartSeries> constructorPointsByRound;
    List<ChartSeries> driverResultByRound;
    List<ChartSeries> gridToResultChartWithoutDnf;
    List<ChartSeries> gridToResultChartWithDnf;
    List<FrontendRace> races;
    Integer currentYear;
    private String wikiSummary;
    private String wikiImage;
}
