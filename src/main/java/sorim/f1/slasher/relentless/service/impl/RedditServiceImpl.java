package sorim.f1.slasher.relentless.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import sorim.f1.slasher.relentless.entities.NewsContent;
import sorim.f1.slasher.relentless.entities.RedditPost;
import sorim.f1.slasher.relentless.repository.ImageRepository;
import sorim.f1.slasher.relentless.repository.NewsRepository;
import sorim.f1.slasher.relentless.repository.RedditRepository;
import sorim.f1.slasher.relentless.service.InstagramService;
import sorim.f1.slasher.relentless.service.RedditService;
import sorim.f1.slasher.relentless.util.MainUtility;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedditServiceImpl implements RedditService {

    private static final String REDDIT_NEW_POSTS = "https://reddit.com/r/formula1/new/.json?limit=100";
    private static final String REDDIT_NEW_F1_PORN_POSTS = "https://reddit.com/r/f1porn/hot/.json?limit=100";
    private static final String IMGUR_COM_3_ALBUM = "https://api.imgur.com/3/album/";
    private static final String FAVICON = "/favicon.ico";
    private static final String REDDIT_DAILY_NEWS = "https://reddit.com/r/formula1/search.json?q=flair:news&sort=comments&restrict_sr=on&t=day";
    private static final String REDDIT_DAILY_VIDEO_POSTS = "https://reddit.com/r/formula1/search.json?q=flair:video&sort=comments&restrict_sr=on&t=day";
    private static final String I_REDDIT = "i.redd.it";
    private static final String I_IMGUR = "i.imgur.com";
    private static final String TWITTER_URL = "twitter.com";
    private static final String IMAGE_BASE_PATH = "/f1exposure/image/";
    private static final String NEWS_PREFIX = "NEWS_";
    private static final String FORMULA_DANK_NEW_POSTS = "https://reddit.com/r/formuladank/new/.json?limit=100";
    private static final String TWITTER_FAVICON = "https://abs.twimg.com/favicons/twitter.ico";
    private static String lastNewPost = "";
    private static String lastNewF1PornPost = "";
    private final RedditRepository redditRepository;
    private final NewsRepository newsRepository;
    private final ImageRepository imageRepository;
    private final InstagramService instagramService;
    private final MainProperties mainProperties;
    private final ObjectMapper mapper = new ObjectMapper();
    private final RestTemplate restTemplate;
    HttpHeaders headers = new HttpHeaders();
    HttpHeaders htmlHeaders = new HttpHeaders();
    TypeReference<HashMap<String, Object>> typeRef = new TypeReference<>() {
    };
    TypeReference<ArrayList<Object>> typeRefList = new TypeReference<>() {
    };


    @Override
    public List<RedditPost> getRedditPosts(Integer page) {
        Pageable paging = PageRequest.of(page, 21);
        return redditRepository.findAllByOrderByCreatedDesc(paging);
    }

    @Override
    public NewsContent fetchRedditPosts() {
        getRFormula1NewImages();
        getRF1PornHot();
        return getNews();
    }

    private NewsContent getNews() {
        List<NewsContent> newsPosts =  getRedditNewsByFlair(REDDIT_DAILY_NEWS, 3);
        List<NewsContent> videoPosts =  getRedditNewsByFlair(REDDIT_DAILY_VIDEO_POSTS, 5);
        List<NewsContent> finalList = mergeAndEnrichNewsLists(newsPosts, videoPosts);
        saveNewsList(finalList);
        return finalList.get(0);
    }

    private List<NewsContent> mergeAndEnrichNewsLists(List<NewsContent> newsPosts, List<NewsContent> videoPosts) {
        List<NewsContent> filteredVideoPosts = new ArrayList<>();
        videoPosts.forEach(post->{
            if(post.getUrl().contains("streamja")){
                post.setUrl(post.getUrl().replace("streamja.com/", "streamja.com/embed/"));
                filteredVideoPosts.add(post);
            }
            if(post.getUrl().contains("streamable")){
                post.setUrl(post.getUrl().replace("streamable.com/", "streamable.com/e/"));
                filteredVideoPosts.add(post);
            }
            if(post.getUrl().contains("youtu")){
                post.setUrl(post.getUrl().replace("youtu.be/", "www.youtube.com/embed/"));
                post.setUrl(post.getUrl().replace("youtube.com/watch?v=", "youtube.com/embed/"));
                filteredVideoPosts.add(post);
            }
        });
        List<NewsContent> finalList = Stream.concat(newsPosts.stream(), filteredVideoPosts.stream()).sorted().collect(Collectors.toList());
        Collections.sort(finalList);
        AtomicReference<Integer> counter = new AtomicReference<>(1000);
        long currentTime = System.currentTimeMillis();
        finalList.forEach(post->{
            post.setDates(currentTime - counter.get());
            counter.set(counter.get() + 1000);
        });
        return finalList;
    }

    private List<NewsContent>  getRedditNewsByFlair(String apiUrl, Integer status) {
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<String> newsResponse = restTemplate.exchange(
                apiUrl, HttpMethod.GET, entity, String.class);
        List<NewsContent> list = new ArrayList<>();
        try {
            Map<String, Object> mapping = mapper.readValue(newsResponse.getBody(), typeRef);
            LinkedHashMap<String, Object> root = (LinkedHashMap<String, Object>) mapping.get("data");
            List<LinkedHashMap<String, Object>> children = (ArrayList<LinkedHashMap<String, Object>>) root.get("children");
            children.forEach(child -> {
                LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) child.get("data");
                NewsContent post = new NewsContent(data, status);
                list.add(post);
            });
            return list;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private void saveNewsList(List<NewsContent> list) {
        List<NewsContent> saveToDatabaseList = new ArrayList<>();
        for (NewsContent post : list) {
            NewsContent fromDb = newsRepository.findByCode(post.getCode());
            if (fromDb == null) {
                if(post.getStatus()==3) {
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
        if(post !=null && post.getUrl()!=null) {
            HttpEntity entity = new HttpEntity(htmlHeaders);
            boolean iconUrlSet = false;
            String domainUrl = MainUtility.getDomain(post.getUrl());
            if (domainUrl.contains(I_REDDIT) || domainUrl.contains(I_IMGUR)) {
                saveRedditOrImgurImageToRepository(post);
                post.setIconUrl(FAVICON);
                post.setStatus(4);
            } else if (domainUrl.contains(TWITTER_URL)) {
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
                    ex.printStackTrace();
                }
            }
        }
    }

    private void saveRedditOrImgurImageToRepository(NewsContent content) {
        byte[] imageBytes = instagramService.getImageFromUrl(content.getUrl());
        String imageCode = NEWS_PREFIX + content.getCode();
        ImageRow imageRow = ImageRow.builder().code(imageCode).image(imageBytes).build();
        imageRepository.save(imageRow);
        String imageUrl = mainProperties.getUrl() + IMAGE_BASE_PATH + imageCode;
        content.setUrl(null);
        content.setImageUrl(imageUrl);
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

    private void getRFormula1NewImages() {
        AtomicReference<Boolean> iterate = new AtomicReference<>(true);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                REDDIT_NEW_POSTS, HttpMethod.GET, entity, String.class);
        List<RedditPost> list = new ArrayList<>();
        try {
            Map<String, Object> mapping = mapper.readValue(response.getBody(), typeRef);
            LinkedHashMap<String, Object> root = (LinkedHashMap<String, Object>) mapping.get("data");
            List<LinkedHashMap<String, Object>> children = (ArrayList<LinkedHashMap<String, Object>>) root.get("children");
            children.forEach(child -> {
                if (iterate.get()) {
                    LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) child.get("data");
                    RedditPost post = new RedditPost(data);
                    if (post.getId().equals(lastNewPost)) {
                        iterate.set(false);
                    }
                    if (post.getValid()) {
                        list.add(post);
                    }
                }
            });
            lastNewPost = list.get(0).getId();
            redditRepository.saveAll(list);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String postFormulaDankToInstagram() throws IGLoginException {
        AtomicReference<Integer> two = new AtomicReference<>(0);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                FORMULA_DANK_NEW_POSTS, HttpMethod.GET, entity, String.class);
        List<RedditPost> output = new ArrayList<>();
        List<RedditPost> fallback = new ArrayList<>();
        try {
            Map<String, Object> mapping = mapper.readValue(response.getBody(), typeRef);

            LinkedHashMap<String, Object> root = (LinkedHashMap<String, Object>) mapping.get("data");
            List<LinkedHashMap<String, Object>> children = (ArrayList<LinkedHashMap<String, Object>>) root.get("children");
            fallback.add(new RedditPost(0));
            fallback.add(new RedditPost(0));
            children.forEach(child -> {
                if (two.get()<2) {
                    LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) child.get("data");
                    RedditPost post = new RedditPost(data);
                    if (post.isItJpeg()) {
                        if (post.getUps()>1300) {
                            two.set(two.get()+1);
                            output.add(post);
                        }
                        if (post.getUps()> fallback.get(0).getUps()) {
                            fallback.set(1, fallback.get(0));
                            fallback.set(0, post);
                        }
                    }
                }
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if(output.size()==2){
            return instagramService.postDankToInstagram(output);
        } else {
            log.warn("dank instagram post fallback!");
            return instagramService.postDankToInstagram(fallback);
        }
    }

    private void getRF1PornHot() {
        AtomicReference<Boolean> iterate = new AtomicReference<>(true);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                REDDIT_NEW_F1_PORN_POSTS, HttpMethod.GET, entity, String.class);
        List<RedditPost> list = new ArrayList<>();
        try {
            Map<String, Object> mapping = mapper.readValue(response.getBody(), typeRef);
            LinkedHashMap<String, Object> root = (LinkedHashMap<String, Object>) mapping.get("data");
            List<LinkedHashMap<String, Object>> children = (ArrayList<LinkedHashMap<String, Object>>) root.get("children");
            children.forEach(child -> {
                if (iterate.get()) {
                    LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) child.get("data");
                    RedditPost post = new RedditPost(data);
                    if (post.getId().equals(lastNewF1PornPost)) {
                        iterate.set(false);
                    }
                    if (post.getValid()) {
                        list.add(post);
                    }
                }
            });
            lastNewF1PornPost = list.get(0).getId();
            redditRepository.saveAll(list);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
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
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("user-agent", "Mozilla/4.8 Firefox/21.0");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }

}
