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
import sorim.f1.slasher.relentless.configuration.MainProperties;
import sorim.f1.slasher.relentless.entities.ImageRow;
import sorim.f1.slasher.relentless.entities.InstagramPost;
import sorim.f1.slasher.relentless.handling.Logger;
import sorim.f1.slasher.relentless.model.TripleInstagramFeed;
import sorim.f1.slasher.relentless.model.enums.InstagramPostType;
import sorim.f1.slasher.relentless.repository.ImageRepository;
import sorim.f1.slasher.relentless.repository.InstagramRepository;
import sorim.f1.slasher.relentless.service.InstagramService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InstagramServiceImpl implements InstagramService {

    private static final List<String> follows = new ArrayList<>();
    private static IGClient client;
    private final MainProperties properties;
    private final InstagramRepository instagramRepository;
    private final ImageRepository imageRepository;
    private final String PREFIX = "INSTA_";

    @Override
    public Boolean fetchInstagramFeed() throws IGLoginException {
        List<InstagramPost> instagramPosts = new ArrayList<>();
        if (client == null || !client.isLoggedIn()) {
            init();
        }
        AtomicReference<Integer> counter = new AtomicReference<>(0);
        FeedIterable<FeedTimelineRequest, FeedTimelineResponse> response = client.getActions().timeline().feed();
        AtomicReference<Boolean> iterate = new AtomicReference<>(true);
        try {
            response.stream().takeWhile(n -> iterate.get()).forEach(row -> {
                List<TimelineMedia> timelineMedias = row.getFeed_items();
                timelineMedias.forEach(post -> {
                    if (follows.contains(post.getUser().getUsername())) {
                        String url = "";
                        String location = " ";
                        if (post.getLocation() != null) {
                            location = post.getLocation().getName();
                        }
                        if (post instanceof TimelineCarouselMedia) {
                            TimelineCarouselMedia timelineCarouselMedia = (TimelineCarouselMedia) post;
                            List<String> urlList = new ArrayList<>();
                            timelineCarouselMedia.getCarousel_media().forEach(cItem -> {
                                if (cItem instanceof ImageCarouselItem) {
                                    ImageCarouselItem cItem2 = (ImageCarouselItem) cItem;
                                    urlList.add(cItem2.getImage_versions2().getCandidates()
                                            .get(cItem2.getImage_versions2().getCandidates().size() - 1)
                                            .getUrl());
                                } else if (cItem instanceof VideoCarouselItem) {
                                    VideoCarouselItem cItem2 = (VideoCarouselItem) cItem;
                                    urlList.add(cItem2.getImage_versions2().getCandidates()
                                            .get(cItem2.getImage_versions2().getCandidates().size() - 1)
                                            .getUrl());
                                } else {
                                    log.error("OVO NIJE ImageCaraouselItem: {}", cItem.getClass().getName());
                                }
                            });
                            //url = String.join(",", urlList);
                            url = urlList.get(0);
                            instagramPosts.add(InstagramPost.builder().code(timelineCarouselMedia.getCode())
                                    .takenAt(post.getTaken_at())
                                    .comments(timelineCarouselMedia.getComment_count())
                                    .likes(timelineCarouselMedia.getLike_count())
                                    .postType(InstagramPostType.TimelineCarouselMedia.getValue())
                                    .url(url)
                                    .location(location)
                                    .caption(getPostCaption(post.getCaption()))
                                    .username(post.getUser().getFull_name())
                                    .userpic(post.getUser().getProfile_pic_url())
                                    .build());
                        } else if (post instanceof TimelineImageMedia) {
                            TimelineImageMedia timelineImageMedia = (TimelineImageMedia) post;
                            url = timelineImageMedia.getImage_versions2().getCandidates()
                                    .get(timelineImageMedia.getImage_versions2().getCandidates().size() - 1)
                                    .getUrl();
                            String caption = "";
                            if (post.getCaption() != null) {
                                caption = post.getCaption().getText();
                            }
                            instagramPosts.add(InstagramPost.builder().code(timelineImageMedia.getCode())
                                    .takenAt(post.getTaken_at())
                                    .comments(timelineImageMedia.getComment_count())
                                    .likes(timelineImageMedia.getLike_count())
                                    .postType(InstagramPostType.TimelineImageMedia.getValue())
                                    .url(url)
                                    .location(location)
                                    .caption(caption)
                                    .username(post.getUser().getFull_name())
                                    .userpic(post.getUser().getProfile_pic_url())
                                    .build());
                        } else if (post instanceof TimelineVideoMedia) {
                            TimelineVideoMedia timelineVideoMedia = (TimelineVideoMedia) post;
                            url = timelineVideoMedia.getImage_versions2().getCandidates()
                                    .get(timelineVideoMedia.getImage_versions2().getCandidates().size() - 1)
                                    .getUrl();
                            instagramPosts.add(InstagramPost.builder().code(timelineVideoMedia.getCode())
                                    .takenAt(post.getTaken_at())
                                    .comments(timelineVideoMedia.getComment_count())
                                    .likes(timelineVideoMedia.getLike_count())
                                    .postType(InstagramPostType.TimelineVideoMedia.getValue())
                                    .url(url)
                                    .location(location)
                                    .caption(getCaptionText(post.getCaption()))
                                    .username(post.getUser().getFull_name())
                                    .userpic(post.getUser().getProfile_pic_url())
                                    .build());
                        } else {
                            log.error("OVAJ INSTAGRAM POST JE: {}", post.getClass().getName());
                        }
                    }
                });
                boolean exists = instagramRepository.existsByCode(timelineMedias.get(timelineMedias.size() - 1).getCode());
                if (counter.get() < 1) {
                    iterate.set(true);
                } else if (counter.get() > 5) {
                    iterate.set(false);
                } else if (exists) {
                    iterate.set(false);
                } else {
                    iterate.set(true);
                }
                counter.set(counter.get() + 1);
            });
        } catch (Exception e) {
            log.error("insta error");
            log.error("caught2", e);
            init();
            e.printStackTrace();
        }
        instagramRepository.saveAll(instagramPosts);
        fetchImages(instagramPosts);
        Logger.log("INSTAGRAM_FETCH_DONE -  : ", String.valueOf(counter.get()));
        return true;
    }

    private String getPostCaption(Comment.Caption caption) {
        if (caption != null) {
            return caption.getText();
        } else {
            return "";
        }
    }

    private String getCaptionText(Comment.Caption caption) {
        if (caption != null) {
            return caption.getText();
        }
        return null;
    }

    private void fetchImages(List<InstagramPost> instagramPosts) {
        List<ImageRow> images = new ArrayList<>();
        instagramPosts.forEach(post -> {
            byte[] image = getImageFromUrl(post.getUrl());
            images.add(ImageRow.builder().code(PREFIX + post.getCode()).image(image).build());
        });
        imageRepository.saveAll(images);
    }

    private byte[] getImageFromUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            try (InputStream stream = url.openStream()) {
                byte[] buffer = new byte[4096];
                while (true) {
                    int bytesRead = stream.read(buffer);
                    if (bytesRead < 0) {
                        break;
                    }
                    output.write(buffer, 0, bytesRead);
                }
            }
            return output.toByteArray();
        } catch (IOException e) {
            log.error("fetchImages ", e);
        }
        return new byte[0];
    }

    @Override
    public TripleInstagramFeed getInstagramFeedPage(Integer mode, Integer page) {
        Pageable paging = PageRequest.of(page, 20);
        List<InstagramPost> posts = instagramRepository.findAllByOrderByTakenAtDesc(paging);

        return new TripleInstagramFeed(mode, posts);
    }

    @Override
    public void getMyFollows() {
        List<Profile> result = client.actions().users().findByUsername(properties.getInstagramUsername())
                .thenApply(userAction -> userAction.followingFeed().stream()
                        .flatMap(feedUsersResponse -> feedUsersResponse.getUsers().stream()).collect(Collectors.toList())
                ).join();
        result.forEach(profile -> {
            follows.add(profile.getUsername());
        });
    }

    @Override
    public byte[] getImage(String code) {
        ImageRow result = imageRepository.findFirstByCode(code);
        return result.getImage();
    }

    @Override
    public Boolean cleanup() throws IGLoginException {
        instagramRepository.deleteAll();
        imageRepository.deleteEverythingExceptM();
        fetchInstagramFeed();
        return true;
    }

    void init() throws IGLoginException {
        client = IGClient.builder()
                .username(properties.getInstagramUsername())
                .password(properties.getInstagramPassword())
                .login();
        getMyFollows();
    }

}
