package sorim.f1.slasher.relentless.model.strawpoll;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StrawpollPoll {
    private String id;
    private String title;
    private String url;
    private String status;
    private Boolean is_votable;
    private List<StrawpollPollOption> poll_options;
    private StrawpollPollMeta poll_meta;
    private StrawpollPollConfig poll_config;
}
