package sorim.f1.slasher.relentless.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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


class InitData {
    @JsonProperty("Drivers")
    public List<Driver> drivers;
}

class RawData {
    private Map<String, Object> dataFields = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getDataFields() {
        return dataFields;
    }

    @JsonAnySetter
    public void setDataField(String fieldName, Object value) {
        dataFields.put(fieldName, value);
    }

    public Object getDataField(String fieldName) {
        return dataFields.get(fieldName);
    }
}

class ScoresGraph {
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

class LapPosGraph {
    public String xtitle;
    public String ytitle;
    public String ztitle;
    public RawData data;
    @JsonProperty("TrackStatus")
    public List<String> trackStatus;
}

class Scores {
    public ScoresGraph graph;
}

class LapPos {
    public LapPosGraph graph;
}

class Driver {
    @JsonProperty("Name")
    public String name;
    @JsonProperty("Initials")
    public String initials;
    @JsonProperty("FullName")
    public String fullName;
    @JsonProperty("FirstName")
    public String firstName;
    @JsonProperty("LastName")
    public String lastName;
    @JsonProperty("Color")
    public String color;
    @JsonProperty("Team")
    public String team;
    @JsonProperty("Num")
    public String num;
}

class Init {
    public InitData data;
    public Integer seq;
    @JsonProperty("T")
    public long t;
    @JsonProperty("TY")
    public String tY;
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


class Best {
    public RawData data;
    public Integer seq;
    @JsonProperty("T")
    public long t;
    @JsonProperty("TY")
    public String tY;
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

class Free {
    public RawData data;
    public Integer seq;
    @JsonProperty("T")
    public long t;
    @JsonProperty("TY")
    public String tY;
}

class Xtra {
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



