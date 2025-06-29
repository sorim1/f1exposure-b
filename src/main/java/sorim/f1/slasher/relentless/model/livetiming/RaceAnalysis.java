package sorim.f1.slasher.relentless.model.livetiming;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.model.KeyValueInteger;
import sorim.f1.slasher.relentless.model.youtube.YouTubeVideo;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties
public class RaceAnalysis {
    Integer year;
    String title;
    Integer status;
    List<FrontendGraphWeatherData> weatherChartData;
    List<Driver> driverData;
    String art;
    String livetimingUrl;
    List<RadioData> radioData;
    List<ChampionshipPrediction> driversChampionshipPrediction;
    List<ChampionshipPrediction> teamsChampionshipPrediction;
    List<LapSeries> lapSeries;
    List<RaceControlMessage> raceControlMessages;
    List<TimingStat> timingStats;
    List<String> bestSpeedKeys;
    List<KeyValueInteger> topSpeeds;
    List<YouTubeVideo> raceYoutube;
}
