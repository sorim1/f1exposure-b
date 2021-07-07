package sorim.f1.slasher.relentless.service.impl;

import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.actions.feed.FeedIterable;
import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import com.github.instagram4j.instagram4j.models.media.timeline.*;
import com.github.instagram4j.instagram4j.models.user.Profile;
import com.github.instagram4j.instagram4j.requests.feed.FeedTimelineRequest;
import com.github.instagram4j.instagram4j.responses.feed.FeedTimelineResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sorim.f1.slasher.relentless.entities.InstagramPost;
import sorim.f1.slasher.relentless.model.TripleInstagramFeed;
import sorim.f1.slasher.relentless.model.enums.InstagramPostType;
import sorim.f1.slasher.relentless.repository.InstagramRepository;
import sorim.f1.slasher.relentless.service.InstagramService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InstagramServiceImpl implements InstagramService {

    private static IGClient client;
    private final InstagramRepository instagramRepository;
    private static List<String> follows = new ArrayList<>();

    @Override
    public List<InstagramPost> fetchInstagramFeed() throws IGLoginException {
        List<InstagramPost> instagramPosts = new ArrayList<>();
        if (!client.isLoggedIn()) {
            init();
        }
        FeedIterable<FeedTimelineRequest, FeedTimelineResponse> response = client.getActions().timeline().feed();
        response.stream().limit(5).forEach(row -> {
            List<TimelineMedia> timelineMedias = row.getFeed_items();
            timelineMedias.forEach(post -> {
                if (follows.contains(post.getUser().getUsername())) {
                    String url = "";
                    String location = null;
                    if (post.getLocation() != null) {
                        location = post.getLocation().getName();
                    }

                    if (post instanceof TimelineCarouselMedia) {
                        TimelineCarouselMedia timelineCarouselMedia = (TimelineCarouselMedia) post;
                        List<String> urlList = new ArrayList<>();
                        timelineCarouselMedia.getCarousel_media().forEach(cItem -> {
                            if (cItem instanceof ImageCaraouselItem) {
                                ImageCaraouselItem cItem2 = (ImageCaraouselItem) cItem;
                                urlList.add(cItem2.getImage_versions2().getCandidates().get(0).getUrl());
                            } else if (cItem instanceof VideoCaraouselItem) {
                                VideoCaraouselItem cItem2 = (VideoCaraouselItem) cItem;
                                urlList.add(cItem2.getImage_versions2().getCandidates().get(0).getUrl());
                            } else {
                                log.error("OVO NIJE ImageCaraouselItem: {}", cItem.getClass().getName());
                            }
                        });
                        url = String.join(",", urlList);
                        instagramPosts.add(InstagramPost.builder().code(timelineCarouselMedia.getCode())
                                .comments(timelineCarouselMedia.getComment_count())
                                .likes(timelineCarouselMedia.getLike_count())
                                .postType(InstagramPostType.TimelineCarouselMedia.getValue())
                                .url(url)
                                .location(location)
                                .username(post.getUser().getFull_name())
                                .userpic(post.getUser().getProfile_pic_url())
                                .build());
                    } else if (post instanceof TimelineImageMedia) {
                        TimelineImageMedia timelineImageMedia = (TimelineImageMedia) post;
                        url = timelineImageMedia.getImage_versions2().getCandidates().get(0).getUrl();
                        instagramPosts.add(InstagramPost.builder().code(timelineImageMedia.getCode())
                                .comments(timelineImageMedia.getComment_count())
                                .likes(timelineImageMedia.getLike_count())
                                .postType(InstagramPostType.TimelineImageMedia.getValue())
                                .url(url)
                                .location(location)
                                .username(post.getUser().getFull_name())
                                .userpic(post.getUser().getProfile_pic_url())
                                .build());
                    } else if (post instanceof TimelineVideoMedia) {
                        TimelineVideoMedia timelineVideoMedia = (TimelineVideoMedia) post;
                        url = timelineVideoMedia.getImage_versions2().getCandidates().get(0).getUrl();
                        instagramPosts.add(InstagramPost.builder().code(timelineVideoMedia.getCode())
                                .comments(timelineVideoMedia.getComment_count())
                                .likes(timelineVideoMedia.getLike_count())
                                .postType(InstagramPostType.TimelineVideoMedia.getValue())
                                .url(url)
                                .location(location)
                                .username(post.getUser().getFull_name())
                                .userpic(post.getUser().getProfile_pic_url())
                                .build());
                    } else {
                        log.error("OVAJ INSTAGRAM POST JE: {}", post.getClass().getName());
                    }

                } else {
                    log.error("OVO JE REKLAMA: {}", post.getUser().getUsername());
                }
            });

        });
        instagramRepository.saveAll(instagramPosts);
        return instagramPosts;
    }

    @Override
    public TripleInstagramFeed getInstagramFeed() {
        List<InstagramPost> posts = instagramRepository.findFirst10ByOrderByLikesDesc();

        return new TripleInstagramFeed(posts);
    }

    @Override
    public TripleInstagramFeed getInstagramFeedPage(Integer page) {
        Pageable paging = PageRequest.of(page, 20);
        List<InstagramPost> posts = instagramRepository.findAllByOrderByLikesDesc(paging);

        return new TripleInstagramFeed(posts);
    }

    @Override
    public void getMyFollows() {
        List<Profile> result = client.actions().users().findByUsername("miroslav.dragicevic1")
                .thenApply(userAction -> userAction.followingFeed().stream()
                        .flatMap(feedUsersResponse -> feedUsersResponse.getUsers().stream()).collect(Collectors.toList())
                ).join();
        result.forEach(profile -> {
            follows.add(profile.getUsername());
        });

    }

    @PostConstruct
    void init() throws IGLoginException {
        client = IGClient.builder()
                .username("miroslav.dragicevic1")
                .password("!qwadrat1")
                .login();
        getMyFollows();
    }

}
