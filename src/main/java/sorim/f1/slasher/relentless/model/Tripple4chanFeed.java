package sorim.f1.slasher.relentless.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.entities.FourChanPostEntity;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tripple4chanFeed {

    private List<FourChanPostEntity> first = new ArrayList<>();
    private List<FourChanPostEntity> second = new ArrayList<>();
    private List<FourChanPostEntity> third = new ArrayList<>();

    public Tripple4chanFeed(Integer mode, List<FourChanPostEntity> posts) {
        if(mode==2){
            for(int i = 1; i<posts.size(); i+=2){
                first.add(posts.get(i-1));
                second.add(posts.get(i));
            }}
        if(mode==3){
            for(int i = 2; i<posts.size(); i+=3){
                first.add(posts.get(i-2));
                second.add(posts.get(i-1));
                third.add(posts.get(i));
            }}
    }
}
