package sorim.f1.slasher.relentless.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FourchanPost {
    private Integer no;
    private String sub;
    private String com;
    private Integer w;
    private Integer h;
    private String tim;
    private String ext;
    private Integer fsize;

    private Integer replyCounter = 0;

    public void incrementReplyCounter() {
        this.replyCounter++;
    }
}
