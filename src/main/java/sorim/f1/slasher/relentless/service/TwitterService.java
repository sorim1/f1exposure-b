package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.entities.TwitterPost;

import java.util.List;

public interface TwitterService {

    List<TwitterPost> getTwitterPosts(Integer page);
    TwitterPost getMostPopularDailyPost();

    Boolean fetchTwitterPosts() throws Exception;

    List<TwitterPost> fetchTwitterFerrariPosts() throws Exception;

    List<TwitterPost> getTwitterFerrariPosts() throws Exception;

    Boolean cleanup() throws Exception;
}
