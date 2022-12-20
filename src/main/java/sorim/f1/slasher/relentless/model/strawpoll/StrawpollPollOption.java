package sorim.f1.slasher.relentless.model.strawpoll;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.entities.Driver;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StrawpollPollOption {
    private String id;
    private String value;
    private Integer position;
    private Integer vote_count;
    private Integer max_votes;
    private Integer rcv_vote_count;

    public StrawpollPollOption(Driver driver) {
        this.value = driver.getFullName();
    }
}
