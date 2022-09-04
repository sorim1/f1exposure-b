package sorim.f1.slasher.relentless.service.impl;

import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.actions.feed.FeedIterable;
import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import com.github.instagram4j.instagram4j.models.media.timeline.*;
import com.github.instagram4j.instagram4j.models.user.Profile;
import com.github.instagram4j.instagram4j.requests.feed.FeedTagRequest;
import com.github.instagram4j.instagram4j.requests.feed.FeedTimelineRequest;
import com.github.instagram4j.instagram4j.requests.friendships.FriendshipsActionRequest;
import com.github.instagram4j.instagram4j.responses.IGResponse;
import com.github.instagram4j.instagram4j.responses.feed.FeedTimelineResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sorim.f1.slasher.relentless.configuration.MainProperties;
import sorim.f1.slasher.relentless.entities.*;
import sorim.f1.slasher.relentless.handling.Logger;
import sorim.f1.slasher.relentless.model.KeyValue;
import sorim.f1.slasher.relentless.model.TripleInstagramFeed;
import sorim.f1.slasher.relentless.model.enums.InstagramPostType;
import sorim.f1.slasher.relentless.repository.ImageRepository;
import sorim.f1.slasher.relentless.repository.InstagramRepository;
import sorim.f1.slasher.relentless.repository.PropertiesRepository;
import sorim.f1.slasher.relentless.service.InstagramService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InstagramServiceImpl implements InstagramService {

    private static final List<String> instagram_following = new ArrayList<>();
    private static final List<KeyValue> instagramfollowingHashtags = new ArrayList<>();
    private static IGClient workerClient;
    private static IGClient officialClient;
    private final MainProperties properties;

    private final PropertiesRepository propertiesRepository;
    private final InstagramRepository instagramRepository;
    private final ImageRepository imageRepository;
    private static final String PREFIX = "INSTA_";
    private static final List<String> FUN_TAGS = Arrays.asList("#f1", "#formula1", "#f1meme","#f1edit", "#formula1meme", "#formula1memes","#f1memes", "#f1humor", "#lewishamilton", "#charlesleclerc", "#carlossainz","#maxverstappen", "#ferrari", "#scuderiaferrari");
    private static final List<String> SERIOUS_TAGS = Arrays.asList("#f1", "#formula1", "#f1meme","#f1edit", "#formula1meme", "#formula1memes","#f1memes", "#f1driver", "#lewishamilton", "#charlesleclerc", "#carlossainz","#maxverstappen", "#ferrari", "#scuderiaferrari");
   private static List<Long> ACCOUNT_IDS_TO_FOLLOW = new ArrayList<>();
    @Override
    public Boolean fetchInstagramFeed() throws IGLoginException {
        List<InstagramPost> instagramPosts = new ArrayList<>();
        IGClient client = getWorkerClient(false);
        AtomicReference<Integer> counter = new AtomicReference<>(0);
        FeedIterable<FeedTimelineRequest, FeedTimelineResponse> response = client.getActions().timeline().feed();
        AtomicReference<Boolean> iterate = new AtomicReference<>(true);
        try {
            response.stream().takeWhile(n -> iterate.get()).forEach(row -> {
                List<TimelineMedia> timelineMedias = row.getFeed_items();
                timelineMedias.forEach(post -> {
                    if (instagram_following.contains(post.getUser().getUsername())) {
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
            e.printStackTrace();
            getWorkerClient(true);
            return false;
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

    @Override
    public byte[] getImageFromUrl(String urlString) {
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
    public List<KeyValue> getInstagramFollows() throws IGLoginException {
        if(instagramfollowingHashtags.isEmpty()){
            getWorkerClient(false);
        }
        return instagramfollowingHashtags;
    }

    private void fetchInstagramFollows() {
        List<Profile> result = workerClient.actions().users().findByUsername(properties.getInstagramWorkerUsername())
                .thenApply(userAction -> userAction.followingFeed().stream()
                        .flatMap(feedUsersResponse -> feedUsersResponse.getUsers().stream()).collect(Collectors.toList())
                ).join();
        result.forEach(profile -> {
            instagram_following.add(profile.getUsername());
            String hashtag = fullNameToHashtag(profile.getFull_name());
            instagramfollowingHashtags.add(KeyValue.builder().key(profile.getUsername()).value(hashtag).build());
        });
        Collections.sort(instagram_following);
    }

    private String fullNameToHashtag(String fullName) {
        return "#" + fullName.replaceAll("\\s","");
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

    @Override
    public String postToInstagram(FourChanPostEntity chanPost, FourChanImageRow chanImage) throws IGLoginException {
        log.info("postToInstagram");
        String caption = generateCaption(chanPost);
        IGClient client = getOfficialClient(false);
        try{
        client.actions()
                .timeline()
                .uploadPhoto(chanImage.getImage(), caption)
                .thenAccept(response -> {
                    log.info("uploaded photo {}", chanPost.getId());
                })
                .join();
        }catch(Exception e){
            caption = null;
            log.error("postToInstagram error");
            e.printStackTrace();
            getOfficialClient(true);
        }
        return caption;
    }
    @Override
    public
    void followMoreOnInstagram() throws Exception {
        log.info("followMoreOnInstagram called");
        IGClient client = getOfficialClient(false);
        if(ACCOUNT_IDS_TO_FOLLOW.size()<10){
            getAccountsToFollow(client);
        }
        followAccounts(client, 4);
    }

    private void getAccountsToFollow(IGClient client) throws Exception {
        FeedTagRequest request = new FeedTagRequest("f1memes");
        client.sendRequest(request).get().getRanked_items()
                .forEach(entry->{
                    entry.getPreview_comments().forEach(previewComment->{
                        Long id = previewComment.getUser_id();
                        ACCOUNT_IDS_TO_FOLLOW.add(id);
                    });
                });
    }
    private void followAccounts(IGClient client, Integer accountCount) throws Exception {
        int counter = 0;
        do{
            followAnAccount(client, ACCOUNT_IDS_TO_FOLLOW.get(0));
            ACCOUNT_IDS_TO_FOLLOW.remove(0);
            counter++;
            Thread.sleep(1000);
        } while(counter<accountCount);
    }
    private void followAnAccount(IGClient client, Long pk) throws Exception {
        IGResponse response = new FriendshipsActionRequest(pk, FriendshipsActionRequest.FriendshipsAction.CREATE)
                .execute(client).join();
    }
    private void unfollowAnAccount(IGClient client, Long pk) throws Exception {
        IGResponse response = new FriendshipsActionRequest(pk, FriendshipsActionRequest.FriendshipsAction.DESTROY)
                .execute(client).join();
        log.info("followAnAccount: {}", response.getStatus());
    }


    private String generateCaption(FourChanPostEntity chanPost) {
        Random rand = new Random();
        if(chanPost.getTags()==null){
            chanPost.setTags("");
        }
        StringBuilder response = new StringBuilder(chanPost.getTags());
        response.append("\r\n\n");
        response.append("f1exposure.com");
        response.append("\r\n");
        response.append("Follow @f1exposure for more daily content.");
        response.append("\r\n\n");
        if(chanPost.getStatus()==4){
            for (int i = 0; i < 5; i++) {
                int randomIndex = rand.nextInt(FUN_TAGS.size());
                String randomElement = FUN_TAGS.get(randomIndex);
                response.append(randomElement).append(" ");
            }
        }
        if(chanPost.getStatus()==5){
            for (int i = 0; i < 5; i++) {
                int randomIndex = rand.nextInt(SERIOUS_TAGS.size());
                String randomElement = SERIOUS_TAGS.get(randomIndex);
                response.append(randomElement).append(" ");
            }
        }

        return response.toString();
    }


    IGClient getWorkerClient(boolean force) throws IGLoginException {
        if(workerClient == null || !workerClient.isLoggedIn() || force) {
            workerClient = IGClient.builder()
                    .username(properties.getInstagramWorkerUsername())
                    .password(getInstagramWorkerPassword())
                    .login();
            fetchInstagramFollows();
        }
        return workerClient;
    }

    private String getInstagramWorkerPassword() {
            AppProperty ap = propertiesRepository.findDistinctFirstByName("INSTAGRAM_WORKER_PASSWORD");
            if (ap == null) {
                ap = AppProperty.builder().name("INSTAGRAM_WORKER_PASSWORD").value("qwadrat1").build();
                propertiesRepository.save(ap);
            }
        return ap.getValue();
    }
    @Override
    public String setInstagramWorkerPassword(String password) throws IGLoginException {
        AppProperty ap = AppProperty.builder().name("INSTAGRAM_WORKER_PASSWORD").value(password).build();
            propertiesRepository.save(ap);
        getWorkerClient(true);
        return ap.getValue();
    }
    IGClient getOfficialClient(Boolean force) throws IGLoginException {
        if(officialClient == null || !officialClient.isLoggedIn() || force) {
            officialClient = IGClient.builder()
                    .username(properties.getInstagramOfficialUsername())
                    .password(properties.getInstagramOfficialPassword())
                    .login();
        }
        return officialClient;
    }

}
