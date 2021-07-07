package sorim.f1.slasher.relentless.service;

import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import sorim.f1.slasher.relentless.entities.InstagramPost;
import sorim.f1.slasher.relentless.model.TripleInstagramFeed;

import java.util.List;

public interface InstagramService {

    List<InstagramPost> fetchInstagramFeed() throws IGLoginException;

    TripleInstagramFeed getInstagramFeed() throws IGLoginException;

    TripleInstagramFeed getInstagramFeedPage(Integer page);

    void getMyFollows() throws IGLoginException;
}
