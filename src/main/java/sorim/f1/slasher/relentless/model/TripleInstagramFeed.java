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
@Builder
public class TripleInstagramFeed {

    private List<InstagramPost> first = new ArrayList<>();
    private List<InstagramPost> second = new ArrayList<>();
    private List<InstagramPost> third = new ArrayList<>();

    public TripleInstagramFeed(List<InstagramPost> posts) {
        for(int i = 2; i<posts.size(); i+=3){
            first.add(posts.get(i-2));
            second.add(posts.get(i-1));
            third.add(posts.get(i));
        }
    }
}
