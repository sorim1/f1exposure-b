package sorim.f1.slasher.relentless.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.entities.ConstructorStanding;
import sorim.f1.slasher.relentless.entities.ConstructorStandingByRound;
import sorim.f1.slasher.relentless.entities.DriverStanding;
import sorim.f1.slasher.relentless.entities.DriverStandingByRound;

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
}
