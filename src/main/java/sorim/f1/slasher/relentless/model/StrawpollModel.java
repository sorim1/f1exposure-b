package sorim.f1.slasher.relentless.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StrawpollModel {

    private Content content;
    private Integer success;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class Content {
        private String id;
        private String title;
        private LocalDateTime created_at;
        private LocalDateTime deadline;
        private String status;
        private Poll poll;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public class Poll {
            private String is_votable;
            private String last_vote_at;
            private List<StrawpollAnswer> poll_answers;
            private Integer total_voters;
            private Integer total_votes;
        }
    }
}
