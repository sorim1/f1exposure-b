package sorim.f1.slasher.relentless.model.wikipedia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tempo {

    private Boolean success;
    private String tempo1;
    private Integer tempo2;

}
