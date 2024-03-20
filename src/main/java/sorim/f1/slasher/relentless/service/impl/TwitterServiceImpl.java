package sorim.f1.slasher.relentless.service.impl;

import com.apptasticsoftware.rssreader.DateTime;
import com.apptasticsoftware.rssreader.Item;
import com.apptasticsoftware.rssreader.RssReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sorim.f1.slasher.relentless.configuration.MainProperties;
import sorim.f1.slasher.relentless.entities.TwitterPost;
import sorim.f1.slasher.relentless.repository.TwitterRepository;
import sorim.f1.slasher.relentless.service.TwitterService;
import twitter4j.Twitter;
import twitter4j.v1.Query;
import twitter4j.v1.QueryResult;
import twitter4j.v1.ResponseList;
import twitter4j.v1.Status;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TwitterServiceImpl implements TwitterService {

    private static final Map<String, TwitterPost> twitterPostsMap = new HashMap<>();
    private static List<TwitterPost> twitterPostsList = new ArrayList<>();
    private static Boolean twitterFetchRunning = false;
    private static Date latestTweetDate;
    private final MainProperties properties;
    private final TwitterRepository twitterRepository;
    private static List<String> nitterList = Arrays.asList("nitter.privacydev.net", "nitter.no-logs.com", "nitter.poast.org");
    //nitter.no-logs.com - dead?
    //nitter.perennialte.ch - login code?
    private static List<String> nitterListObsolete = Arrays.asList("nitter.perennialte.ch", "nitter.mint.lgbt");
    private static Integer twitterIterator = 0;

    @Value("${twitter.accounts.list}")
    private String twitterAccountsForRss;

    @Override
    public List<TwitterPost> getTwitterPosts(Integer page) {
        Pageable paging = PageRequest.of(page, 21);
        return twitterRepository.findAllByOrderByCreatedAtDesc(paging);
    }

    @Override
    public TwitterPost getMostPopularDailyPost() {
        Date yesterday = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        log.info("yesterday; " + yesterday);
        return twitterRepository.findFirstByCreatedAtAfterOrderByRetweetCountDesc(yesterday);
    }

    @Override
    public Boolean cleanup() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Date lastMonth = cal.getTime();
        twitterRepository.deleteByCreatedAtBefore(lastMonth);
        return true;
    }

    @Override
    public Date getLatestTweetDate() {
        return latestTweetDate;
    }

    @Override
    public Boolean fetchTwitterPosts() throws Exception {
        if (!twitterFetchRunning) {
            twitterFetchRunning = true;
            List<String> rssEndpoints = generateRssList();
            log.info("zovem twitter RSS: " +  rssEndpoints.size());
            for (String url : rssEndpoints) {
                try{
                    List<Item> timeline = new RssReader().read(url).sorted().collect(Collectors.toList());
                    List<TwitterPost> list = getListFromRssFeed(timeline);
                    twitterRepository.saveAll(list);
                    log.error("SUCCESS: " +  url);
                } catch(Exception e){
                    log.error("FAILED: " +  url, e);
                    log.error(e.getMessage());
                    ;
                }
                Thread.sleep(20 * 1000);
            }
            log.info("fetchTwitterPosts DONE");
            twitterFetchRunning = false;
            TwitterPost latest = twitterRepository.findFirstByOrderByCreatedAtDesc();
            if(latest!=null){
                latestTweetDate = latest.getCreatedAt();
            }
        } else {
            log.info("twitterFetchRunning");
        }
        return twitterFetchRunning;
    }

    private List<String> generateRssList() {
        List<String> accountNames = Arrays.asList(twitterAccountsForRss.toLowerCase().split(","));
        List<String> output = new ArrayList<>();
   //     accountNames.forEach(account -> {
//            counter.set(counter.get()+1);
//            if(counter.get() >= nitterList.size()){
//                counter.set(0);
//            }
            if(twitterIterator>accountNames.size()-nitterList.size()){
                twitterIterator=0;
            }
            for(int i=0;i<nitterList.size();i++){
            String nitterEndpoint = nitterList.get(i);
            String account = accountNames.get(twitterIterator++);
            String rssUrl = "https://" + nitterEndpoint + "/" + account + "/rss";
            output.add(rssUrl);
            }
        return output;
    }

    private Boolean fetchTwitterPostsOld() throws Exception {
        Twitter twitter = getTwitterinstance();
        ResponseList<Status> timeline = twitter.v1().timelines().getHomeTimeline();
        List<TwitterPost> list = getListFromResponseList(timeline);
        twitterRepository.saveAll(list);
        fetchTwitterFerrariPosts();
        return true;
    }

    private List<TwitterPost> getListFromResponseList(ResponseList<Status> timeline) {
        List<TwitterPost> list = new ArrayList<>();
        timeline.forEach(item -> {
            TwitterPost post = getTwitterPostFromResponseItem(item, true);
            if (post != null) {
                list.add(post);
            }
        });
        return list;
    }

    private List<TwitterPost> getListFromRssFeed(List<Item> timeline) {
        List<TwitterPost> list = new ArrayList<>();
        timeline.forEach(item -> {
            try {

                if (!item.getTitle().get().startsWith("RT by @")) {
                    List<TwitterPost> posts = getTwitterPostsFromRssItem(item);
                    if (!posts.isEmpty()) {
                        list.addAll(posts);
                    }
                }
            } catch (Exception e) {
                log.error("getListFromRssFeed error:" + item.getChannel().getTitle());
                twitterFetchRunning = false;
                e.printStackTrace();
            }
        });
        return list;
    }

    private List<TwitterPost> getTwitterPostsFromRssItem(Item item) {
        List<TwitterPost> output = new ArrayList<>();
        Long id = DateTime.toEpochMilli(item.getPubDate().get());
        Instant instant = DateTime.toInstant(item.getPubDate().get());
        Date date1 = Date.from(instant);
        List<String> imageUrls = getImageUrlsFromRssDescription(item.getDescription().get());
        String url = getTwitterFromNitter(item.getGuid().get());
        String username = item.getChannel().getTitle().substring(item.getChannel().getTitle().indexOf("@"));
        AtomicReference<Integer> counter = new AtomicReference<>(0);
        imageUrls.forEach(imageUrl -> {
            TwitterPost post = TwitterPost.builder()
                    .id(counter.get()-id)
                    .text(item.getTitle().get())
                    .url(url)
                    .source(1)
                    .mediaUrl(imageUrl)
                    .username(username)
                    .createdAt(date1)
                    .build();
            output.add(post);
            counter.set(counter.get() + 1);
        });
        return output;
    }

    private String getTwitterFromNitter(String input) {
        for ( String entry:nitterList){
            input = input.replace(entry, "twitter.com");
        }
        return input;
    }

    private String getImageFromNitter(String input) {
        String imageUrl = input;
        String imageUrl2 = input;
        if (input.contains("pic/enc/")) {
            String encodedString = input.substring(input.indexOf("pic/enc/") + 8);
            byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
            imageUrl = new String(decodedBytes);
        } else if(input.contains("nitter") && input.contains("/pic/")) {
            String imageUrlRelative = input.substring(input.indexOf("/pic/") + 5);
            imageUrl = imageUrlRelative.replace("%2F", "/");
            imageUrl2 = imageUrlRelative.replaceAll("%2F", "/");
        }
        if(imageUrl.startsWith("pbs.twimg.com")){
            return "https://" + imageUrl;
        } else if(imageUrl.startsWith("http")) {
            return input;
        } else {
            return "https://pbs.twimg.com/" + imageUrl;
        }
    }

    private List<String> getImageUrlsFromRssDescription(String description) {
        List<String> output = new ArrayList<>();
        Document doc = Jsoup.parse(description);
        Elements images = doc.getElementsByTag("img");
        for (Element el : images) {
            // If alt is empty or null, add one to counter
            String src = el.attr("src");
            String imageUrl = getImageFromNitter(src);
            output.add(imageUrl);
        }
        return output;
    }

    private TwitterPost getTwitterPostFromResponseItem(Status item, Boolean mediaOnly) {
        TwitterPost response = null;
        Integer source = 0;
        String text = item.getText();
        String url = null;
        String mediaUrl = null;
        if (item.getUser() != null && item.getUser().getScreenName().equals("F1Exposure")) {
            return null;
        }
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
                    // .createdAt(item.getCreatedAt())
                    .build();
        }
        return response;
    }

    // @PostConstruct
    @Override
    public List<TwitterPost> fetchTwitterFerrariPosts() throws Exception {
        if (twitterPostsMap.size() > 300) {
            twitterPostsMap.clear();
            for (TwitterPost post : twitterPostsList) {
                twitterPostsMap.put(post.getUrl(), post);
            }
        }
        Twitter twitter = getTwitterinstance();
        Query query = Query.of("ScuderiaFerrari");
        QueryResult result = twitter.v1().search().search(query);
        for (Status status : result.getTweets()) {
            TwitterPost post = getTwitterPostFromResponseItem(status, true);
            if (post != null) {
                twitterPostsMap.put(post.getUrl(), post);
            }
        }
        List<TwitterPost> list = new ArrayList<>(twitterPostsMap.values());
        Collections.sort(list);
        int upperLimit;
        if (list.size() > 30) {
            upperLimit = 30;
        } else {
            upperLimit = list.size();
        }
        twitterPostsList = list.subList(0, upperLimit);
        return twitterPostsList;
    }

    @Override
    public List<TwitterPost> getTwitterFerrariPosts() {
        return twitterPostsList;
    }

    private Twitter getTwitterinstance() {
        Twitter twitter = Twitter.newBuilder()
                .oAuthConsumer(properties.getTwitterKey(), properties.getTwitterSecret())
                .oAuthAccessToken(properties.getTwitterAccessToken(), properties.getTwitterAccessTokenSecret())
                .build();
//        Twitter twitter2 = Twitter.newBuilder().oau
//                .oAuthConsumer(properties.getTwitterKey(), properties.getTwitterSecret())
//                .oAuthAccessToken(properties.getTwitterAccessToken(), properties.getTwitterAccessTokenSecret())
//                .build();
        return twitter;
    }

}
