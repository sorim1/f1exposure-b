package sorim.f1.slasher.relentless.model.livetiming;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class LapPosGraph {
    public String xtitle;
    public String ytitle;
    public String ztitle;
    public RawData data;
    @JsonProperty("TrackStatus")
    public List<String> trackStatus;
}
