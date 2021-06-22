package sorim.f1.slasher.relentless.model.livetiming;

import com.fasterxml.jackson.annotation.JsonProperty;
import sorim.f1.slasher.relentless.model.livetiming.RawData;

public class Best {
    public RawData data;
    public Integer seq;
    @JsonProperty("T")
    public long t;
    @JsonProperty("TY")
    public String tY;
}
