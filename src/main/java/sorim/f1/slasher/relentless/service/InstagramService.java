package sorim.f1.slasher.relentless.service;

import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import sorim.f1.slasher.relentless.model.TripleInstagramFeed;

public interface InstagramService {

    Boolean fetchInstagramFeed() throws IGLoginException;

    TripleInstagramFeed getInstagramFeed() throws IGLoginException;

    TripleInstagramFeed getInstagramFeedPage(Integer page);

    void getMyFollows() throws IGLoginException;

    byte[] getImage(String code);

    Boolean cleanup() throws IGLoginException;
}
