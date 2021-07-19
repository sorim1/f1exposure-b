package sorim.f1.slasher.relentless.model.livetiming;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LiveTimingData {
    @JsonProperty("Weather")
    public Weather weather;
    @JsonProperty("Scores")
    public Scores scores;
    @JsonProperty("LapPos")
    public LapPos lapPos;
    public Init init;
    public Best best;
    public Opt opt;
    public Sq sq;
    public Free free;
    public Xtra xtra;
    public Commentary commentary;
    public String prefix;
    public String path;
    public Integer timezoneOffset;
}


class DR {
    @JsonProperty("B")
    public List<Object> b;
    @JsonProperty("STOP")
    public List<Integer> sTOP;
    @JsonProperty("O")
    public List<Object> o;
    @JsonProperty("OC")
    public List<String> oC;
    @JsonProperty("UC")
    public List<Integer> uC;
    @JsonProperty("G")
    public List<Object> g;
    @JsonProperty("F")
    public List<Object> f;
    @JsonProperty("X")
    public List<String> x;
    @JsonProperty("TI")
    public List<Integer> tI;
}


class Opt {
    public RawData data;
    public Integer seq;
    @JsonProperty("T")
    public long t;
    @JsonProperty("TY")
    public String tY;
}

class Sq {
    public RawData data;
    public Integer seq;
    @JsonProperty("T")
    public long t;
    @JsonProperty("TY")
    public String tY;
}

class Commentary {
    public RawData data;
    public Integer seq;
    @JsonProperty("T")
    public long t;
    @JsonProperty("TY")
    public String tY;
}



