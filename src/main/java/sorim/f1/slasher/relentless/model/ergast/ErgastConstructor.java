package sorim.f1.slasher.relentless.model.ergast;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErgastConstructor {
    private String constructorId;
    private String url;
    private String name;
    private String nationality;
}
