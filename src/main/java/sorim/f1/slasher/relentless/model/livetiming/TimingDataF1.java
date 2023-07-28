package sorim.f1.slasher.relentless.model.livetiming;

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
public class TimingDataF1 {
    @JsonProperty("Stats")
    public List<Object> stats;
    @JsonProperty("GapToLeader")
    public String gapToLeader;
    @JsonProperty("IntervalToPositionAhead")
    public Object intervalToPositionAhead;
    @JsonProperty("TimeDiffToFastest")
    public String timeDiffToFastest;
    @JsonProperty("TimeDiffToPositionAhead")
    public String timeDiffToPositionAhead;
    @JsonProperty("BestLapTimes")
    public List<LapValuePosition> bestLapTimes;
    @JsonProperty("Line")
    public String line;
    @JsonProperty("Position")
    public String position;
    @JsonProperty("ShowPosition")
    public String showPosition;
    @JsonProperty("RacingNumber")
    public String racingNumber;
    @JsonProperty("Retired")
    public Boolean retired;
    @JsonProperty("InPit")
    public Boolean onPit;
    @JsonProperty("PitOut")
    public Boolean pitOut;
    @JsonProperty("Stopped")
    public Boolean stopped;
    @JsonProperty("KnockedOut")
    public Boolean knockedOut;
    @JsonProperty("Cutoff")
    public Boolean cutoff;
    @JsonProperty("Status")
    public Integer status;
    @JsonProperty("NumberOfLaps")
    public Integer numberOfLaps;
    @JsonProperty("NumberOfPitStops")
    public Integer numberOfPitStops;
    @JsonProperty("Sectors")
    public List<Sector> sectors;
    @JsonProperty("Speeds")
    public Object speeds;
    @JsonProperty("BestLapTime")
    public LapValuePosition bestLapTime;
    @JsonProperty("LastLapTime")
    public Object lastLapTime;
}

