package sorim.f1.slasher.relentless.model.strawpoll;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StrawpollModelThree {

    private StrawpollPoll poll;
    private Boolean finalVersion;
    private Integer driverCount;
}
