package sorim.f1.slasher.relentless.model.livetiming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RaceAnalysis {
    public String title;
    List<FrontendGraphWeatherData> weatherChartData;
    FrontendGraphScoringData scoringChartData;
    FrontendGraphLapPosData lapPosChartData;
    FrontendGraphLeaderboardData leaderboardData;
    List<Driver> driverData;
}
