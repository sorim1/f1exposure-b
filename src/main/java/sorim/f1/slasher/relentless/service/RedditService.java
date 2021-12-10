package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.entities.AwsContent;
import sorim.f1.slasher.relentless.entities.RedditPostNew;
import sorim.f1.slasher.relentless.entities.RedditPostTop;

import java.util.List;

public interface RedditService {

    List<RedditPostNew> getRedditNewPosts(Integer page);

    List<RedditPostTop> getRedditTopPosts(Integer page);

    void updatePostImages(AwsContent post);

    AwsContent fetchRedditPosts();
}
