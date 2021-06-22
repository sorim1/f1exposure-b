package sorim.f1.slasher.relentless.model.livetiming;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Init {
    public InitData data;
    public Integer seq;
    @JsonProperty("T")
    public long t;
    @JsonProperty("TY")
    public String tY;
}
