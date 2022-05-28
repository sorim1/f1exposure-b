package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.entities.TwitterPost;

import java.util.List;

public interface TwitterService {

    List<TwitterPost> getTwitterPosts(Integer page);

    Boolean fetchTwitterPosts() throws Exception;

    Boolean cleanup() throws Exception;
}
