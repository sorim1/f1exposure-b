package sorim.f1.slasher.relentless.model.strawpoll;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StrawpollPollMeta {
    private Integer participant_count;
    private Integer vote_count;
    private Integer view_count;
    private String description;
    private String location;
}
