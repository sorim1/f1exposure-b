package sorim.f1.slasher.relentless.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import masecla.reddit4j.client.Reddit4J;
import masecla.reddit4j.objects.RedditPost;
import masecla.reddit4j.objects.Sorting;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sorim.f1.slasher.relentless.configuration.MainProperties;
import sorim.f1.slasher.relentless.entities.ImageRow;
import sorim.f1.slasher.relentless.entities.MyRedditPost;
import sorim.f1.slasher.relentless.entities.NewsContent;
import sorim.f1.slasher.relentless.repository.ImageRepository;
import sorim.f1.slasher.relentless.repository.NewsRepository;
import sorim.f1.slasher.relentless.repository.RedditRepository;
import sorim.f1.slasher.relentless.service.InstagramService;
import sorim.f1.slasher.relentless.service.RedditService;
import sorim.f1.slasher.relentless.util.MainUtility;

import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedditServiceImpl implements RedditService {

    private static final String IMGUR_COM_3_ALBUM = "https://api.imgur.com/3/album/";
    private static final String FAVICON = "/favicon.ico";
    private static final String I_REDDIT = "i.redd.it";
    private static final String I_IMGUR = "i.imgur.com";
    private static final String TWITTER_URL = "twitter.com";
    private static final String X_URL = "x.com";
    private static final String IMAGE_BASE_PATH = "/f1exposure/image/";
    private static final String NEWS_PREFIX = "NEWS_";
    private static final String FORMULA_DANK_NEW_POSTS = "https://old.reddit.com/r/formuladank/new/.json?limit=100";
    private static final String TWITTER_FAVICON = "https://abs.twimg.com/favicons/twitter.3.ico";
    private static String lastNewPost = "";
    private static String lastNewF1PornPost = "";
    private final RedditRepository redditRepository;
    private final NewsRepository newsRepository;
    private final ImageRepository imageRepository;
    private final InstagramService instagramService;
    private final MainProperties mainProperties;
    private final ObjectMapper mapper = new ObjectMapper();
    private final RestTemplate restTemplate;
    private Reddit4J reddit4JClient;

    HttpHeaders headers = new HttpHeaders();
    HttpHeaders htmlHeaders = new HttpHeaders();
    TypeReference<HashMap<String, Object>> typeRef = new TypeReference<>() {
    };
    TypeReference<ArrayList<Object>> typeRefList = new TypeReference<>() {
    };


    @Override
    public List<MyRedditPost> getRedditPosts(Integer page) {
        Pageable paging = PageRequest.of(page, 21);
        return redditRepository.findAllByOrderByCreatedDesc(paging);
    }

    @Override
    public NewsContent fetchRedditPosts() {
        log.info("fetchRedditPosts");
        fetchF1PornSubreddit();
        return getReddit4j();
    }

    private List<NewsContent> mergeAndEnrichNewsLists(List<NewsContent> newsPosts, List<NewsContent> videoPosts) {
        List<NewsContent> filteredVideoPosts = new ArrayList<>();
        List<NewsContent> filteredNewsPosts = new ArrayList<>();
        videoPosts.forEach(post -> {
            if (post.getUrl() != null) {
                if (post.getUrl().contains("streamja")) {
                    post.setUrl(post.getUrl().replace("streamja.com/", "streamja.com/embed/"));
                    filteredVideoPosts.add(post);
                }
                if (post.getUrl().contains("streamable")) {
                    post.setUrl(post.getUrl().replace("streamable.com/", "streamable.com/e/"));
                    filteredVideoPosts.add(post);
                }
                if (post.getUrl().contains("imgur.com/a")) {
                    post.setStatus(1);
                    filteredVideoPosts.add(post);
                }
                if (post.getUrl().contains("i.imgur.com/")) {
                    post.setUrl(post.getUrl().replace("gifv", "mp4"));
                    if(!post.getUrl().contains(".mp4")){
                        post.setUrl(post.getUrl() + ".mp4");
                    }
                    filteredVideoPosts.add(post);
                }
                if (post.getUrl().contains("dubz.")) {
                    post.setStatus(1);
                        String[] parts = post.getUrl().split("/c/");
                        if(parts.length > 1){
                            post.setImageUrl("https://cdn.makevos.com/thumbnails/"+ parts[1] + ".jpg");
                        }
                        filteredVideoPosts.add(post);
                }
                if (post.getUrl().contains("youtu")) {
                    post.setIconUrl("https://www.youtube.com/favicon.ico");
                    post.setImageUrl(getYoutubeThumbnail(post.getUrl()));
                    post.setStatus(1);
                    filteredVideoPosts.add(post);
                }
            }
        });
        newsPosts.forEach(post -> {
            if (newsPostIsValid(post)) {
                filteredNewsPosts.add(post);
            }

        });
        List<NewsContent> finalList = Stream.concat(filteredNewsPosts.stream(), filteredVideoPosts.stream()).sorted().collect(Collectors.toList());
        Collections.sort(finalList);
        AtomicReference<Integer> counter = new AtomicReference<>(1000);
        long currentTime = System.currentTimeMillis();
        finalList.forEach(post -> {
            post.setDates(currentTime - counter.get());
            counter.set(counter.get() + 1000);
        });
        return finalList;
    }

    private String getUrlFromImgurAlbum(String url) {
        List<String> list = new ArrayList<>();
        log.info("getUrlFromImgurAlbum: " + url);
        try {
            HttpEntity entity = new HttpEntity(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            String string = response.getBody();
            Integer index;
            do {
                index = string.indexOf("https://i.imgur.com/");
                if (index >= 0) {
                    string = string.substring(index);
                    String videoUrl = string.substring(0, string.indexOf("\""));
                    log.info("videoUrl: " + videoUrl);
                    if (videoUrl.indexOf(".mp4") > 0 && !list.contains(videoUrl)) {
                        list.add(videoUrl);
                    }
                    string = string.substring(videoUrl.length());
                }
            } while (index >= 0);
        } catch (Exception ex) {
            log.error("nisam dohvatio imgur url");
            log.error(ex.getMessage());
            ex.printStackTrace();
            return null;
        }
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    private Boolean newsPostIsValid(NewsContent news) {
        if (news.getUrl() == null && news.getImageUrl() == null && news.getTextContent() == null) {
            return false;
        }
        return news.getUrl() == null || news.getUrl().indexOf("reddit.com") <= 0 || news.getImageUrl() != null;
    }


    private String getYoutubeThumbnail(String finalUrl) {

        finalUrl = finalUrl.replace("youtu.be/", "/MD/");
        finalUrl = finalUrl.replace("youtube.com/watch?v=", "/MD/");
        if (finalUrl.contains("&")) {
            finalUrl = finalUrl.substring(0, finalUrl.indexOf("&"));
        }
        Integer index = finalUrl.indexOf("/MD/");
        if (index > 0) {
            String code = finalUrl.substring(index + 4, index + 15);
            return "https://img.youtube.com/vi/" + code + "/maxresdefault.jpg";
        }
        return null;
    }

    @SneakyThrows
    public NewsContent  getReddit4j() {
        List<NewsContent> newsPosts = new ArrayList<>();
        List<NewsContent> videoPosts = new ArrayList<>();
        List<MyRedditPost> imagePosts = new ArrayList<>();

        List<RedditPost> posts = reddit4JClient.getSubredditPosts("formula1", Sorting.HOT).submit();
        List<RedditPost> redditImagePosts = posts.stream().filter(this::isImageRedditPost).collect(Collectors.toList());
        List<RedditPost> redditNewsPosts = posts.stream().filter(this::isNewsRedditPost).collect(Collectors.toList());
        List<RedditPost> redditVideoPosts = posts.stream().filter(this::isVideoRedditPost).collect(Collectors.toList());


        redditNewsPosts.forEach(redditPost->{
            NewsContent newsPost = new NewsContent(redditPost, 3);
            newsPosts.add(newsPost);
        });
        redditVideoPosts.forEach(redditPost->{
            NewsContent newsPost = new NewsContent(redditPost, 5);
            videoPosts.add(newsPost);
        });
        redditImagePosts.forEach(redditPost->{
            MyRedditPost post = new MyRedditPost(redditPost);
            if (post.getValid()) {
                imagePosts.add(post);
            }
        });
        lastNewPost = imagePosts.get(0).getId();
        redditRepository.saveAll(imagePosts);
        log.info("save imagePosts count:{}", imagePosts.size());
        List<NewsContent> finalList = mergeAndEnrichNewsLists(newsPosts, videoPosts);
        saveNewsList(finalList);
        log.info("save finalList count:{}", finalList.size());
        if (!finalList.isEmpty()) {
            return finalList.get(0);
        } else {
            return null;
        }
    }

    @SneakyThrows
    private void fetchF1PornSubreddit(){
        List<MyRedditPost> imagePosts = new ArrayList<>();

        List<RedditPost> posts = reddit4JClient.getSubredditPosts("f1porn", Sorting.NEW).submit();
        posts.forEach(redditPost->{
            MyRedditPost post = new MyRedditPost(redditPost);
            if (post.getValid()) {
                imagePosts.add(post);
            }
        });
        log.info("save f1p count:{}", imagePosts.size());
        redditRepository.saveAll(imagePosts);
    }

    private boolean isImageRedditPost(RedditPost post) {
        if(post.getLinkFlairText()!=null && post.getLinkFlairText().contains("photo")){
            return true;
        }
        return post.getUrl().startsWith("https://i.redd")
                || post.getUrl().startsWith("https://i.imgur.");
    }

    private boolean isNewsRedditPost(RedditPost post) {
        if (post.getLinkFlairText() == null || post.getUrl() == null || post.getUrl().contains("bsky.app")) {
            return false;
        } else {
            return post.getLinkFlairText().contains("news")
                    || post.getLinkFlairText().contains("social-media")
                    || post.getLinkFlairText().contains("statistics");
        }
    }
    private boolean isVideoRedditPost(RedditPost post) {
        if(post.getLinkFlairText()==null){
            return false;
        } else {
            return post.getLinkFlairText().contains("video");
        }
    }

    private void saveNewsList(List<NewsContent> list) {
        List<NewsContent> saveToDatabaseList = new ArrayList<>();
        for (NewsContent post : list) {
            NewsContent fromDb = newsRepository.findByCode(post.getCode());
            if (fromDb == null) {
                if (post.getStatus() == 3) {
                    getImagesForPost(post);
                }

                saveToDatabaseList.add(post);
            } else {
                if (fromDb.getTimestampActivity().before(post.getTimestampActivity())) {
                    fromDb.setTimestampActivity(post.getTimestampActivity());
                    saveToDatabaseList.add(fromDb);
                }
            }
        }
        newsRepository.saveAll(saveToDatabaseList);
    }

    @Override
    public void updatePostImages(NewsContent post) {
        HttpEntity entity = new HttpEntity(htmlHeaders);
        boolean iconUrlSet = false;
        String domainUrl = MainUtility.getDomain(post.getUrl());
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    post.getUrl(), HttpMethod.GET, entity, String.class);

            String rawHtml = response.getBody();
            assert rawHtml != null;
            Document doc = Jsoup.parse(rawHtml);

            if (!iconUrlSet) {
                Elements linkTags = doc.getElementsByTag("link");
                Optional<Element> relTag = linkTags.stream().filter(tag -> "icon".equals(tag.attr("rel"))).findAny();
                if (relTag.isPresent()) {
                    String href = relTag.get().attr("href");
                    if (check200Status(domainUrl + href)) {
                        post.setIconUrl(domainUrl + href);
                        iconUrlSet = true;
                    }
                }
                if (!iconUrlSet) {
                    relTag = linkTags.stream().filter(tag -> "shortcut icon".equals(tag.attr("rel"))).findAny();
                    if (relTag.isPresent()) {
                        String href = relTag.get().attr("href");
                        if (check200Status(domainUrl + href)) {
                            post.setIconUrl(domainUrl + href);
                            iconUrlSet = true;
                        }
                    }
                }
            }
            if (check200Status(domainUrl + FAVICON)) {
                post.setIconUrl(domainUrl + FAVICON);
                iconUrlSet = true;
            }
            Elements metaTags = doc.getElementsByTag("meta");
            Optional<Element> imageTag = metaTags.stream().filter(tag -> "og:image".equals(tag.attr("property"))).findAny();
            if (imageTag.isPresent()) {
                String imageUrl = imageTag.get().attr("content");
                if (imageUrl != null && imageUrl.startsWith("/")) {
                    imageUrl = domainUrl + imageUrl;
                }
                post.setImageUrl(imageUrl);
            }
        } catch (Exception ex) {
            log.info(ex.getMessage());
        }
    }

    private void getImagesForPost(NewsContent post) {
        if (post != null && post.getUrl() != null) {
            HttpEntity entity = new HttpEntity(htmlHeaders);
            boolean iconUrlSet = false;
            String domainUrl = MainUtility.getDomain(post.getUrl());
            if (domainUrl.contains(I_REDDIT) || domainUrl.contains(I_IMGUR)) {
                saveRedditOrImgurImageToRepository(post);
                post.setIconUrl(FAVICON);
                post.setStatus(4);
            } else if (domainUrl.contains(TWITTER_URL) || domainUrl.contains(X_URL)) {
                post.setUrl(post.getUrl().replace(X_URL, TWITTER_URL));
                post.setIconUrl(TWITTER_FAVICON);
                post.setStatus(4);
            } else {
                try {
                    ResponseEntity<String> response = restTemplate.exchange(
                            post.getUrl(), HttpMethod.GET, entity, String.class);

                    String rawHtml = response.getBody();
                    assert rawHtml != null;
                    Document doc = Jsoup.parse(rawHtml);

                    if (!iconUrlSet) {
                        Elements linkTags = doc.getElementsByTag("link");

                        Optional<Element> relTag1 = linkTags.stream().filter(tag -> "shortcut icon".equals(tag.attr("rel"))).findAny();
                        if (relTag1.isPresent()) {
                            String href = relTag1.get().attr("href");
                            String absoluteUrl = relativeToAbsoluteUrl(domainUrl, href);
                            if (check200Status(absoluteUrl)) {
                                post.setIconUrl(absoluteUrl);
                                iconUrlSet = true;
                            }
                        }
                        if (!iconUrlSet) {
                            Optional<Element> relTag = linkTags.stream().filter(tag -> "icon".equals(tag.attr("rel"))).findAny();
                            if (relTag.isPresent()) {
                                String href = relTag.get().attr("href");
                                String absoluteUrl = relativeToAbsoluteUrl(domainUrl, href);
                                if (check200Status(absoluteUrl)) {
                                    post.setIconUrl(absoluteUrl);
                                    iconUrlSet = true;
                                }
                            }
                        }

                    }

                    if (!iconUrlSet && check200Status(domainUrl + FAVICON)) {
                        post.setIconUrl(domainUrl + FAVICON);
                    }

                    Elements metaTags = doc.getElementsByTag("meta");
                    Optional<Element> imageTag = metaTags.stream().filter(tag -> "og:image".equals(tag.attr("property"))).findAny();
                    if (imageTag.isPresent()) {
                        String content = imageTag.get().attr("content");
                        post.setImageUrl(content);
                    }
                } catch (Exception ex) {
                    log.info("ex2 " + post.getUrl());
                    log.info("ex2 " + ex.getMessage());
                }
            }
        }
    }

    private void saveRedditOrImgurImageToRepository(NewsContent content) {
        byte[] imageBytes = instagramService.getImageFromUrl(content.getUrl());
        content.setUrl(null);
        if (imageBytes != null) {
            String imageCode = NEWS_PREFIX + content.getCode();
            ImageRow imageRow = ImageRow.builder().code(imageCode).image(imageBytes).build();
            imageRepository.save(imageRow);
            String imageUrl = mainProperties.getUrl() + IMAGE_BASE_PATH + imageCode;
            content.setImageUrl(imageUrl);
        } else {
            content.setImageUrl(content.getUrl());
        }
    }

    private String relativeToAbsoluteUrl(String domainUrl, String href) {
        if (href.startsWith("//")) {
            return "https:" + href;
        }
        if (href.contains("googleapis.com")) {
            return href;
        } else if (href.contains(domainUrl)) {
            return href;
        }
        {
            return domainUrl + href;
        }
    }

    private Boolean check200Status(String url) {
        HttpEntity entity = new HttpEntity(htmlHeaders);
        try {
            ResponseEntity<byte[]> faviconResponse = restTemplate.exchange(
                    url, HttpMethod.GET, entity, byte[].class);

            Boolean response =
                    faviconResponse.getHeaders().getContentType().getType().contains("image")
                            && faviconResponse.getStatusCode().is2xxSuccessful();
            return response;
        } catch (Exception e) {
            return false;
        }
    }

    @SneakyThrows
    @Override
    public String postFormulaDankToInstagram() {
        List<MyRedditPost> output = new ArrayList<>();

        List<RedditPost> posts = reddit4JClient.getSubredditPosts("formuladank", Sorting.HOT).submit();
        List<RedditPost> imagePosts = posts.stream().filter(post->post.getUrl()!=null && post.getUrl().contains("i.redd.it")).collect(Collectors.toList());
        if (imagePosts.size() >= 2) {
            output.add(new MyRedditPost(imagePosts.get(0)));
            output.add(new MyRedditPost(imagePosts.get(1)));
            return instagramService.postDankToInstagram(output);
        } else {
            log.warn("dank instagram post fallback!");
            return "failed";
        }
    }



    private String getImgurlAlbumFirstImage(String albumUrl) {
        String code = albumUrl.substring(albumUrl.lastIndexOf("/"));
        HttpEntity entity = new HttpEntity(headers);

//        String response = restTemplate
//                .getForObject(imgurAlbumUrl, String.class, code);
        ResponseEntity<String> response2 = restTemplate.exchange(
                IMGUR_COM_3_ALBUM + code, HttpMethod.GET, entity, String.class);
        try {
            List<Object> root = mapper.readValue(response2.getBody(), typeRefList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    @PostConstruct
    void init() {

        reddit4JClient = Reddit4J.rateLimited();
        reddit4JClient.setClientSecret("jMGac0NNRSD_pEcwwLtsQyVug2UqJw");
        reddit4JClient.setClientId("iVJwKQTZgdjgkgqzAe4iVg");
        reddit4JClient.setUsername("F1Exposure");
        reddit4JClient.setPassword("qwadrat1");
        reddit4JClient.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:115.0) Gecko/20100101 Firefox/115.0");


        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:115.0) Gecko/20100101 Firefox/115.0");

        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }

}
