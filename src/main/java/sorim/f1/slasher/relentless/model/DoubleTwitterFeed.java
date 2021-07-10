package sorim.f1.slasher.relentless.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.entities.InstagramPost;
import sorim.f1.slasher.relentless.entities.TwitterPost;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoubleTwitterFeed {

    private List<TwitterPost> first = new ArrayList<>();
    private List<TwitterPost> second = new ArrayList<>();

    public DoubleTwitterFeed(List<TwitterPost> posts) {
        for(int i = 1; i<posts.size(); i+=2){
            first.add(posts.get(i-1));
            second.add(posts.get(i));
        }
    }
}
