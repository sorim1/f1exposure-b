package sorim.f1.slasher.relentless.service.impl;

import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.actions.feed.FeedIterable;
import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import com.github.instagram4j.instagram4j.models.media.timeline.TimelineMedia;
import com.github.instagram4j.instagram4j.requests.feed.FeedTimelineRequest;
import com.github.instagram4j.instagram4j.responses.feed.FeedTimelineResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sorim.f1.slasher.relentless.entities.InstagramPost;
import sorim.f1.slasher.relentless.model.TripleInstagramFeed;
import sorim.f1.slasher.relentless.repository.F1CommentRepository;
import sorim.f1.slasher.relentless.repository.InstagramRepository;
import sorim.f1.slasher.relentless.service.InstagramService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InstagramServiceImpl implements InstagramService {

    private static IGClient client;
    private final InstagramRepository instagramRepository;

    @Override
    public List<InstagramPost> fetchInstagramFeed() throws IGLoginException {
        List<InstagramPost> instagramPosts = new ArrayList<>();
        if(!client.isLoggedIn()){
            init();
        }
        FeedIterable<FeedTimelineRequest, FeedTimelineResponse> response = client.getActions().timeline().feed();
        AtomicInteger counter1 = new AtomicInteger();
        response.stream().limit(5).forEach(row->{
            List<TimelineMedia> timelineMedias = row.getFeed_items();
            timelineMedias.forEach(post->{
                instagramPosts.add(InstagramPost.builder().code(post.getCode())
                        .comments(post.getComment_count())
                        .likes(post.getLike_count())
                        .deviceTimestamp(post.getTaken_at()).build());
            });
            counter1.getAndIncrement();
        });
        instagramRepository.saveAll(instagramPosts);
        return instagramPosts;
    }

    @Override
    public TripleInstagramFeed getInstagramFeed(){
        List<InstagramPost> posts =instagramRepository.findFirst50ByOrderByDeviceTimestampDesc();
        return new TripleInstagramFeed(posts);
    }

    @PostConstruct
    void init() throws IGLoginException {
        client = IGClient.builder()
                .username("miroslav.dragicevic1")
                .password("!qwadrat1")
                .login();
    }

}
