package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.entities.NewsContent;
import sorim.f1.slasher.relentless.entities.RedditPost;

import java.util.List;

public interface RedditService {

    List<RedditPost> getRedditPosts(Integer page);

    void updatePostImages(NewsContent post);

    NewsContent fetchRedditPosts();
}
