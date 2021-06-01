package sorim.f1.slasher.relentless.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.entities.F1Calendar;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CountdownData {

    private F1Calendar f1Calendar;
    private Integer days;
    private Integer seconds;

}
