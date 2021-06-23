package sorim.f1.slasher.relentless.model.livetiming;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Xtra {
    public RawData data;
    public Integer seq;
    @JsonProperty("T")
    public long t;
    @JsonProperty("TY")
    public String tY;
}
