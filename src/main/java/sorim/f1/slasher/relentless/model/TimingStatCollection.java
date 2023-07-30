package sorim.f1.slasher.relentless.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.model.livetiming.TimingStat;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimingStatCollection {
    List<TimingStat> fp1;
    List<TimingStat> fp2;
    List<TimingStat> fp3;
    List<TimingStat> quali;
    List<TimingStat> sprintShootout;
    List<TimingStat> sprint;
}



