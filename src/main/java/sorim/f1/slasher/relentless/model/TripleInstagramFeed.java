package sorim.f1.slasher.relentless.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.entities.InstagramPost;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripleInstagramFeed {
    private Boolean fetchOk;
    private List<InstagramPost> first = new ArrayList<>();
    private List<InstagramPost> second = new ArrayList<>();
    private List<InstagramPost> third = new ArrayList<>();

    public TripleInstagramFeed(Integer mode, List<InstagramPost> posts, Boolean fetchOk) {
        this.fetchOk = fetchOk;
        if (mode == 2) {
            for (int i = 1; i < posts.size(); i += 2) {
                first.add(posts.get(i - 1));
                second.add(posts.get(i));
            }
        }
        if (mode == 3) {
            for (int i = 2; i < posts.size(); i += 3) {
                first.add(posts.get(i - 2));
                second.add(posts.get(i - 1));
                third.add(posts.get(i));
            }
        }
    }
}
