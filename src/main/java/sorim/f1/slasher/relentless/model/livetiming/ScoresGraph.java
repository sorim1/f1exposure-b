package sorim.f1.slasher.relentless.model.livetiming;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ScoresGraph {
    public String xtitle;
    public String ytitle;
    public String ztitle;
    @JsonProperty("Steering")
    public RawData steering;
    @JsonProperty("GforceLat")
    public RawData gforceLat;
    @JsonProperty("GforceLong")
    public RawData gforceLong;
    @JsonProperty("Brake")
    public RawData brake;
    @JsonProperty("Performance")
    public RawData performance;
    @JsonProperty("Throttle")
    public RawData throttle;
    @JsonProperty("TrackStatus")
    public List<String> trackStatus;
}
