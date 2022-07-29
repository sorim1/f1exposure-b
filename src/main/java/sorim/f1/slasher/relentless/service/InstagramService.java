package sorim.f1.slasher.relentless.service;

import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import sorim.f1.slasher.relentless.model.TripleInstagramFeed;

import java.util.List;

public interface InstagramService {

    Boolean fetchInstagramFeed() throws IGLoginException;

    TripleInstagramFeed getInstagramFeedPage(Integer mode, Integer page);

    List<String> getInstagramFollows();

    byte[] getImage(String code);

    byte[] getImageFromUrl(String urlString);

    Boolean cleanup() throws IGLoginException;
}
