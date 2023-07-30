package sorim.f1.slasher.relentless.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopSpeeds {
    List<KeyValueInteger> fp1;
    List<KeyValueInteger> fp2;
    List<KeyValueInteger> fp3;
    List<KeyValueInteger> quali;
    List<KeyValueInteger> sprintShootout;
    List<KeyValueInteger> sprint;
}



