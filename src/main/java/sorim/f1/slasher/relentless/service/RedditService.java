package sorim.f1.slasher.relentless.service;

import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import sorim.f1.slasher.relentless.entities.NewsContent;
import sorim.f1.slasher.relentless.entities.RedditPost;

import java.util.List;

public interface RedditService {

    List<RedditPost> getRedditPosts(Integer page);

    void updatePostImages(NewsContent post);

    String postFormulaDankNew() throws IGLoginException;
    NewsContent fetchRedditPosts();
}
