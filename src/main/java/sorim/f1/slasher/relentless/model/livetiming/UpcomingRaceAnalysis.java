package sorim.f1.slasher.relentless.model.livetiming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.entities.ergast.RaceData;
import sorim.f1.slasher.relentless.model.TimingStatCollection;
import sorim.f1.slasher.relentless.model.TopSpeeds;

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
    List<Driver> sprintQuali;
    List<Driver> sprint;
    List<LapTimeData> fp1Laps;
    List<LapTimeData> fp2Laps;
    List<LapTimeData> fp3Laps;
    List<LapTimeData> qualiLaps;
    List<LapTimeData> sprintQualiLaps;
    List<LapTimeData> sprintLaps;
    List<RadioData> fp1Radio;
    List<RadioData> fp2Radio;
    List<RadioData> fp3Radio;
    List<RadioData> qualiRadio;
    List<RadioData> sprintQualiRadio;
    List<RadioData> sprintRadio;
    String fp1LivetimingUrl;
    String fp2LivetimingUrl;
    String fp3LivetimingUrl;
    String qualiLivetimingUrl;
    String sprintQualiLivetimingUrl;
    String sprintLivetimingUrl;
    TopSpeeds topSpeeds = new TopSpeeds();
    TimingStatCollection timingStats = new TimingStatCollection();

    public UpcomingRaceAnalysis(RaceData raceData) {
        this.season = Integer.valueOf(raceData.getSeason());
        this.title = raceData.getRaceName();
        this.circuitUrl = raceData.getCircuit().getUrl();
        this.circuitName = raceData.getCircuit().getCircuitName();
        this.raceDate = raceData.getDate();
        this.imageUrl = raceData.getImageUrl();
        this.url = raceData.getUrl();
        this.basicRaces = new ArrayList<>();
    }
}
