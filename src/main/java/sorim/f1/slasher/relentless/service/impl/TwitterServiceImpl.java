package sorim.f1.slasher.relentless.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sorim.f1.slasher.relentless.configuration.MainProperties;
import sorim.f1.slasher.relentless.entities.TwitterPost;
import sorim.f1.slasher.relentless.repository.TwitterRepository;
import sorim.f1.slasher.relentless.service.TwitterService;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import javax.annotation.PostConstruct;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TwitterServiceImpl implements TwitterService {

    private final MainProperties properties;
    private final TwitterRepository twitterRepository;
    private static List<TwitterPost> twitterPostsList = new ArrayList<>();
    private static Map<String, TwitterPost> twitterPostsMap = new HashMap<>();

    @Override
    public List<TwitterPost> getTwitterPosts(Integer page) {
        Pageable paging = PageRequest.of(page, 21);
        return twitterRepository.findAllByOrderByCreatedAtDesc(paging);
    }

    @Override
    public Boolean cleanup() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Date lastMonth = cal.getTime();
        twitterRepository.deleteByCreatedAtBefore(lastMonth);
        fetchTwitterPosts();
        return true;
    }

    @Override
    public Boolean fetchTwitterPosts() throws Exception {
        Twitter twitter = getTwitterinstance();
        ResponseList<Status> timeline = twitter.getHomeTimeline();
        List<TwitterPost> list = getListFromResponseList(timeline);
        twitterRepository.saveAll(list);
        fetchTwitterFerrariPosts();
        return true;
    }

    private List<TwitterPost> getListFromResponseList(ResponseList<Status> timeline) {
        List<TwitterPost> list = new ArrayList<>();
        timeline.forEach(item -> {
            TwitterPost post = getTwitterPostFromResponseItem(item, true);
            if(post!=null){
                list.add(post);
            }
        });
        return list;
    }

    private TwitterPost getTwitterPostFromResponseItem(Status item, Boolean mediaOnly) {
        TwitterPost response = null;
            Integer source = 0;
            String text = item.getText();
            String url = null;
            String mediaUrl = null;
            int splitter = text.lastIndexOf("https://t.co");
            if (splitter > 0 && splitter > item.getText().length() - 30) {
                text = item.getText().substring(0, splitter);
                url = item.getText().substring(splitter);
                source = 0;
            }

            if (item.getMediaEntities() != null && item.getMediaEntities().length > 0) {
                mediaUrl = item.getMediaEntities()[0].getMediaURLHttps();
                url = item.getMediaEntities()[0].getURL();
                source = 1;
            }
            if (url == null && item.getURLEntities().length > 0) {
                url = item.getURLEntities()[0].getURL();
                source = 2;
            }
            if (url == null && item.getRetweetedStatus() != null && item.getRetweetedStatus().getURLEntities().length > 0) {
                url = item.getRetweetedStatus().getURLEntities()[0].getURL();
                source = 3;
            }
            if (url == null) {
                url = "https://twitter.com/" + item.getUser().getScreenName() + "/status/" + item.getId();
                source = 4;
            }
            if (mediaUrl != null || !mediaOnly) {
                response = TwitterPost.builder()
                        .id(item.getId())
                        .text(text)
                        .favoriteCount(item.getFavoriteCount())
                        .retweetCount(item.getRetweetCount())
                        .url(url)
                        .source(source)
                        .mediaUrl(mediaUrl)
                        .userPicture(item.getUser().getProfileImageURLHttps())
                        .username(item.getUser().getName())
                        .createdAt(item.getCreatedAt())
                        .build();
            }
        return response;
    }

    @PostConstruct
    @Override
    public List<TwitterPost> fetchTwitterFerrariPosts() throws Exception {
        if(twitterPostsMap.size()>300){
            twitterPostsMap.clear();
            for(TwitterPost post : twitterPostsList){
                twitterPostsMap.put(post.getUrl(), post);
            }
        }
        Twitter twitter = getTwitterinstance();
        Query query = new Query("ScuderiaFerrari");

        QueryResult result = twitter.search(query);
        for (Status status : result.getTweets()) {
            TwitterPost post = getTwitterPostFromResponseItem(status, true);
            if(post!=null) {
                twitterPostsMap.put(post.getUrl(), post);
            }
        }
        List<TwitterPost> list = new ArrayList<>(twitterPostsMap.values());
        Collections.sort(list);
        int upperLimit;
        if(list.size()>30){
            upperLimit = 30;
        } else {
            upperLimit = list.size();
        }
        twitterPostsList = list.subList(0, upperLimit);
        return twitterPostsList;
    }

    @Override
    public List<TwitterPost> getTwitterFerrariPosts(){
        return twitterPostsList;
    }
    private Twitter getTwitterinstance() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(properties.getTwitterDebug())
                .setOAuthConsumerKey(properties.getTwitterKey())
                .setOAuthConsumerSecret(properties.getTwitterSecret())
                .setOAuthAccessToken(properties.getTwitterAccessToken())
                .setOAuthAccessTokenSecret(properties.getTwitterAccessTokenSecret());
        TwitterFactory tf = new TwitterFactory(cb.build());
        return tf.getInstance();
    }

}
