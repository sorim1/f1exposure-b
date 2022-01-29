package sorim.f1.slasher.relentless.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class FourchanThread {
    List<FourchanPost> posts;
    private Integer no;
    private String sub;
    private String com;
    private Integer w;
    private Integer h;
    private String tim;
    private String ext;
}
