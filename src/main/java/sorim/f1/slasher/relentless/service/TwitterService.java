package sorim.f1.slasher.relentless.service;

import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import sorim.f1.slasher.relentless.entities.TwitterPost;
import twitter4j.TwitterException;

import java.util.List;

public interface TwitterService {

    List<TwitterPost> getTwitterPosts(Integer page);

    Boolean fetchTwitterPosts() throws Exception;
}
