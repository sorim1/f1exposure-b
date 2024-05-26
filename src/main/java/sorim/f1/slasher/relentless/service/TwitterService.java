package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.entities.TwitterPost;

import java.util.Date;
import java.util.List;

public interface TwitterService {

    List<TwitterPost> getTwitterPosts(Integer page);

    TwitterPost getMostPopularDailyPost();

    Boolean fetchTwitterPosts() throws Exception;

    List<TwitterPost> updateTwitterFerrariPosts(List<TwitterPost> posts) throws Exception;

    List<TwitterPost> getTwitterFerrariPosts() throws Exception;

    Boolean cleanup() throws Exception;

    Date getLatestTweetDate();

    List<String> setTwitterEndpoints(List<String> endpoints);
}
