package sorim.f1.slasher.relentless.model.livetiming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RadioData {
    Integer id;
    ZonedDateTime utc;
    String driverNumber;
    String driverName;
    String path;
    Boolean active = false;
}
