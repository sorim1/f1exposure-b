package sorim.f1.slasher.relentless.service;

import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import sorim.f1.slasher.relentless.entities.FourChanImageRow;
import sorim.f1.slasher.relentless.entities.FourChanPostEntity;
import sorim.f1.slasher.relentless.entities.InstagramPost;
import sorim.f1.slasher.relentless.entities.RedditPost;
import sorim.f1.slasher.relentless.model.KeyValue;
import sorim.f1.slasher.relentless.model.TripleInstagramFeed;

import java.util.List;

public interface InstagramService {

    Boolean fetchInstagramFeed() throws Exception;

    TripleInstagramFeed getInstagramFeedPage(Integer mode, Integer page);

    List<KeyValue> getInstagramFollows() throws Exception;

    byte[] getImage(String code);

    byte[] getImageFromUrl(String urlString);

    Boolean cleanup() throws Exception;

    String postToInstagram(FourChanPostEntity chanPost, FourChanImageRow chanImage) throws IGLoginException;
    String postDankToInstagram(List<RedditPost> posts) throws IGLoginException;
    String setInstagramWorkerPassword(String password) throws Exception;
    String setInstagramWorker(String username, String password) throws Exception;

    void followMoreOnInstagram() throws Exception;

    String turnOnOffInstagram(Boolean bool);

    InstagramPost getLatestPost();
}
