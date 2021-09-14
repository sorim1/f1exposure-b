package sorim.f1.slasher.relentless.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.entities.RedditPostNew;
import sorim.f1.slasher.relentless.entities.RedditPostTop;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoubleRedditTopFeed {

    private List<RedditPostTop> first = new ArrayList<>();
    private List<RedditPostTop> second = new ArrayList<>();

    public DoubleRedditTopFeed(List<RedditPostTop> posts) {
        for(int i = 1; i<posts.size(); i+=2){
            first.add(posts.get(i-1));
            second.add(posts.get(i));
        }
    }

}
