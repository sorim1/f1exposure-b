package sorim.f1.slasher.relentless.model.livetiming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.entities.ergast.RaceData;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpcomingRaceAnalysis {
    Integer season;
    String title;
    String circuitUrl;
    String raceDate;
    String circuitName;
    String imageUrl;
    String url;
    List<BasicRaceData> basicRaces;
    List<Driver> fp1;
    List<Driver> fp2;
    List<Driver> fp3;
    List<Driver> quali;
    List<LapTimeData> fp1Laps;
    List<LapTimeData> fp2Laps;
    List<LapTimeData> fp3Laps;
    List<LapTimeData> qualiLaps;

    public UpcomingRaceAnalysis(RaceData raceData) {
        this.season = Integer.valueOf(raceData.getSeason());
        this.title = raceData.getRaceName() + " - " + raceData.getCircuit().getCircuitName() ;
        this.circuitUrl = raceData.getCircuit().getUrl();
        this.circuitName = raceData.getCircuit().getCircuitName();
        this.raceDate = raceData.getDate();
        this.imageUrl = raceData.getImageUrl();
        this.url = raceData.getUrl();
        this.basicRaces = new ArrayList<>();
    }
}
