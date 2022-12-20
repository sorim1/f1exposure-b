package sorim.f1.slasher.relentless.model.strawpoll;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StrawpollPollConfig {
    private Integer deadline_at;
    private Boolean is_multiple_choice;
    private Boolean is_private;

}
