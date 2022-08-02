package sorim.f1.slasher.relentless.service;

import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import sorim.f1.slasher.relentless.entities.FourChanImageRow;
import sorim.f1.slasher.relentless.entities.FourChanPostEntity;
import sorim.f1.slasher.relentless.model.TripleInstagramFeed;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface InstagramService {

    Boolean fetchInstagramFeed() throws IGLoginException;

    TripleInstagramFeed getInstagramFeedPage(Integer mode, Integer page);

    List<String> getInstagramFollows() throws IGLoginException;

    byte[] getImage(String code);

    byte[] getImageFromUrl(String urlString);

    Boolean cleanup() throws IGLoginException;

    String postToInstagram(FourChanPostEntity chanPost, FourChanImageRow chanImage) throws IGLoginException;

    void followMoreOnInstagram() throws Exception;
}
