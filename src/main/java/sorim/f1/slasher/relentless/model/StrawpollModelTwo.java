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
public class StrawpollModelTwo {

    private Poll poll;
    private Integer success;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public class Poll {
            private String id;
            private String title;
            private String url;
            private String status;
            private List<PollOptions> poll_options;
            private PollMeta poll_meta;

         }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PollOptions {
        private String id;
        private String value;
        private Integer position;
        private Integer vote_count;
        private Integer max_votes;
        private Integer rcv_vote_count;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PollMeta {
        private Integer participant_count;
        private Integer vote_count;
        private Integer view_count;
    }
}
