package sorim.f1.slasher.relentless.service.impl;

import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
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
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TwitterServiceImpl implements TwitterService {

    private final MainProperties properties;
    private final TwitterRepository twitterRepository;


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
        List<TwitterPost> list = new ArrayList<>();
        Twitter twitter = getTwitterinstance();
        twitter.getHomeTimeline().forEach(item -> {
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
            if (mediaUrl != null) {
                //getTwitterPost(twitter, item.getId());

                list.add(TwitterPost.builder()
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
                        .build());
            }
        });
        twitterRepository.saveAll(list);
        return true;
    }

    private void getTwitterPost(Twitter twitter, long tweetID) {
        try {
            Status status = twitter.showStatus(tweetID);
            log.info("getTwitterPost1");
            log.info(status.getText());
        } catch (TwitterException e) {
            e.printStackTrace();
        }
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
