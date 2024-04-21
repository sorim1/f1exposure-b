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
    private static List<String> nitterList = Arrays.asList("nitter.privacydev.net", "nitter.privacydev.net");
    private static final String nitterPoast = "nitter.poast.org";

    private static List<String> nitterListObsolete = Arrays.asList("nitter.no-logs.com","nitter.poast.org", "nitter.perennialte.ch", "nitter.mint.lgbt");
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
   public List<String> setTwitterEndpoints(List<String> endpoints){
        nitterList = endpoints;
        return nitterList;
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
                    log.error("FAILED: {} - {}",  url, e.getMessage());
                }
                Thread.sleep(10 * 1000);
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
        int poastCounter = 0;
        StringBuilder accountsList = new StringBuilder("");
        while(poastCounter<5){
            accountsList.append(",").append(accountNames.get(twitterIterator++));
            if(twitterIterator>=accountNames.size()){
                twitterIterator=0;
            }
            poastCounter++;
        }
        String rssPoastUrl = "https://" + nitterPoast + "/" + accountsList.substring(1) + "/rss";
        output.add(rssPoastUrl);

            for(int i=0;i<nitterList.size();i++){
            String nitterEndpoint = nitterList.get(i);
            String account = accountNames.get(twitterIterator++);
            String rssUrl = "https://" + nitterEndpoint + "/" + account + "/rss";
            output.add(rssUrl);
                if(twitterIterator>=accountNames.size()){
                    twitterIterator=0;
                }
            }
        return output;
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

    // @PostConstruct
    @Override
    public List<TwitterPost> fetchTwitterFerrariPosts() throws Exception {
        if (twitterPostsMap.size() > 300) {
            twitterPostsMap.clear();
            for (TwitterPost post : twitterPostsList) {
                twitterPostsMap.put(post.getUrl(), post);
            }
        }
//        Twitter twitter = getTwitterinstance();
//        Query query = Query.of("ScuderiaFerrari");
//        QueryResult result = twitter.v1().search().search(query);
//        for (Status status : result.getTweets()) {
//            TwitterPost post = getTwitterPostFromResponseItem(status, true);
//            if (post != null) {
//                twitterPostsMap.put(post.getUrl(), post);
//            }
//        }
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

}
