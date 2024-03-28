package sorim.f1.slasher.relentless.service;

import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import sorim.f1.slasher.relentless.entities.NewsContent;
import sorim.f1.slasher.relentless.entities.MyRedditPost;

import java.util.List;

public interface RedditService {

    List<MyRedditPost> getRedditPosts(Integer page);

    void updatePostImages(NewsContent post);

    String postFormulaDankToInstagram() throws IGLoginException;

    NewsContent fetchRedditPosts();

    NewsContent  getReddit4j();

}
