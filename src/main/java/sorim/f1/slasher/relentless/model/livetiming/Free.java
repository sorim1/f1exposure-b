package sorim.f1.slasher.relentless.model.livetiming;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Free {
    public RawData data;
    public Integer seq;
    @JsonProperty("T")
    public long t;
    @JsonProperty("TY")
    public String tY;
}
