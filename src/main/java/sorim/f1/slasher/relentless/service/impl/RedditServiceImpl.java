package sorim.f1.slasher.relentless.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import sorim.f1.slasher.relentless.entities.AwsContent;
import sorim.f1.slasher.relentless.entities.RedditPostNew;
import sorim.f1.slasher.relentless.entities.RedditPostTop;
import sorim.f1.slasher.relentless.repository.AwsRepository;
import sorim.f1.slasher.relentless.repository.RedditNewRepository;
import sorim.f1.slasher.relentless.repository.RedditTopRepository;
import sorim.f1.slasher.relentless.service.RedditService;
import sorim.f1.slasher.relentless.util.MainUtility;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedditServiceImpl implements RedditService {

    private final MainProperties properties;
    private final RedditNewRepository redditNewRepository;
    private final RedditTopRepository redditTopRepository;
    private final AwsRepository awsRepository;

    private static final String redditNewPosts = "https://reddit.com/r/formula1/new/.json?limit=100";
    private static final String redditNewF1PornPosts = "https://reddit.com/r/f1porn/hot/.json?limit=100";
    private static final String imgurAlbumUrl2 = "https://api.imgur.com/3/album/";
    private static String lastNewPost = "";
    private static String lastNewF1PornPost = "";
    private static final String FAVICON = "/favicon.ico";
    private static final String redditDailyNews = "https://reddit.com/r/formula1/search.json?q=flair:news&sort=comments&restrict_sr=on&t=day";
    private static final String iReddit = "i.redd.it";
    private static final String REDDIT_FAVICON = "https://reddit.com/favicon.ico";
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    HttpHeaders htmlHeaders = new HttpHeaders();
    TypeReference<HashMap<String, Object>> typeRef = new TypeReference<>() {
    };
    TypeReference<ArrayList<Object>> typeRefList = new TypeReference<>() {
    };
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public List<RedditPostNew> getRedditNewPosts(Integer page) {
        Pageable paging = PageRequest.of(page, 21);
         return redditNewRepository.findAllByOrderByCreatedDesc(paging);
    }

    @Override
    public List<RedditPostTop> getRedditTopPosts(Integer page) {
        Pageable paging = PageRequest.of(page, 21);
        return redditTopRepository.findAllByOrderByCreatedDesc(paging);
    }

    @Override
    public void fetchRedditPosts() {
        getRFormula1New();
        getRF1PornHot();
        getNews();
    }

    private void getNews() {
        log.info("getNews");
            HttpEntity entity = new HttpEntity(headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    redditDailyNews, HttpMethod.GET, entity, String.class);
            List<AwsContent> list = new ArrayList<>();
            long currentTime = System.currentTimeMillis();
            try {
                Map<String, Object> mapping = mapper.readValue(response.getBody(), typeRef);
                LinkedHashMap<String, Object> root = (LinkedHashMap<String, Object>) mapping.get("data");
                List<LinkedHashMap<String, Object>> children = (ArrayList<LinkedHashMap<String, Object>>) root.get("children");
                AtomicReference<Integer> counter = new AtomicReference<>(1000);
                children.forEach(child -> {
                        LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) child.get("data");
                    AwsContent post = new AwsContent(data, currentTime- counter.get());
                            list.add(post);
                    counter.set(counter.get() + 1000);
                });
            saveAwsList(list);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

    private void saveAwsList(List<AwsContent> list) {
        List<AwsContent> saveToDatabaseList = new ArrayList<>();
        for(AwsContent post: list){
            AwsContent fromDb = awsRepository.findByCode(post.getCode());
            if(fromDb==null){
                getImagesForPost(post);
                saveToDatabaseList.add(post);
            } else {
                if(fromDb.getTimestampActivity().before(post.getTimestampActivity())){
                    fromDb.setTimestampActivity(post.getTimestampActivity());
                    saveToDatabaseList.add(fromDb);
                }
            }
        }
        awsRepository.saveAll(saveToDatabaseList);
    }

    @Override
    public void updatePostImages(AwsContent post) {
        HttpEntity entity = new HttpEntity(htmlHeaders);
        boolean iconUrlSet = false;
        String domainUrl = MainUtility.getDomain(post.getUrl());
        if(check200Status(domainUrl + FAVICON)){
            post.setIconUrl(domainUrl + FAVICON);
            iconUrlSet=true;
        }
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    post.getUrl(), HttpMethod.GET, entity, String.class);

            String rawHtml = response.getBody();
            assert rawHtml != null;
            Document doc = Jsoup.parse(rawHtml);

            if(!iconUrlSet) {
                Elements linkTags = doc.getElementsByTag("link");
                Optional<Element> relTag = linkTags.stream().filter(tag -> "icon".equals(tag.attr("rel"))).findAny();
                if (relTag.isPresent()) {
                    String href = relTag.get().attr("href");
                    if(check200Status(domainUrl + href)){
                        post.setIconUrl(domainUrl + href);
                        iconUrlSet=true;
                    }
                }
                if(!iconUrlSet) {
                    relTag = linkTags.stream().filter(tag -> "shortcut icon".equals(tag.attr("rel"))).findAny();
                    if (relTag.isPresent()) {
                        String href = relTag.get().attr("href");
                        if(check200Status(domainUrl + href)){
                            post.setIconUrl(domainUrl + href);
                        }
                    }
                }
            }
            Elements metaTags = doc.getElementsByTag("meta");
            Optional<Element> imageTag = metaTags.stream().filter(tag -> "og:image".equals(tag.attr("property"))).findAny();
            if (imageTag.isPresent()) {
                String imageUrl = imageTag.get().attr("content");
                if(imageUrl!=null && imageUrl.startsWith("/")){
                    imageUrl = domainUrl + imageUrl;
                }
                post.setImageUrl(imageUrl);
            }
        }catch(Exception ex){
            log.info(ex.getMessage());
        }
    }

    private void getImagesForPost(AwsContent post) {
        HttpEntity entity = new HttpEntity(htmlHeaders);
        boolean iconUrlSet = false;
        String domainUrl = MainUtility.getDomain(post.getUrl());
        if(domainUrl.contains(iReddit)){
            post.setIconUrl(REDDIT_FAVICON);
            iconUrlSet=true;
        }
        if(!iconUrlSet || check200Status(domainUrl + FAVICON)){
            post.setIconUrl(domainUrl + FAVICON);
            iconUrlSet=true;
        }
        try {
        ResponseEntity<String> response = restTemplate.exchange(
                post.getUrl(), HttpMethod.GET, entity, String.class);

        String rawHtml = response.getBody();
            assert rawHtml != null;
            Document doc = Jsoup.parse(rawHtml);

        if(!iconUrlSet) {
            Elements linkTags = doc.getElementsByTag("link");
            Optional<Element> relTag = linkTags.stream().filter(tag -> "icon".equals(tag.attr("rel"))).findAny();
            if (relTag.isPresent()) {
                String href = relTag.get().attr("href");
                if(check200Status(domainUrl + href)){
                    post.setIconUrl(domainUrl + href);
                }
            }
        }
        Elements metaTags = doc.getElementsByTag("meta");
            Optional<Element> imageTag = metaTags.stream().filter(tag -> "og:image".equals(tag.attr("property"))).findAny();
            if (imageTag.isPresent()) {
                String content = imageTag.get().attr("content");
                post.setImageUrl(content);
            }
            Elements titleTags = doc.getElementsByTag("title");
            if(titleTags.size()>0 && !domainUrl.contains("instagram")){
                post.setTitle(titleTags.get(0).wholeText());
            }
        }catch(Exception ex){
            log.info("ex2 ");
            log.info(ex.getMessage());
        }
    }

    private Boolean check200Status(String url) {
        HttpEntity entity = new HttpEntity(htmlHeaders);
        try {
            ResponseEntity<byte[]> faviconResponse = restTemplate.exchange(
                    url, HttpMethod.GET, entity, byte[].class);
            return faviconResponse.getStatusCode().is2xxSuccessful();
        }catch(Exception e){
            log.info("nije: check200Status");
            log.info(e.getMessage());
            return false;
        }
    }

    private void getRFormula1New() {
        AtomicReference<Boolean> iterate = new AtomicReference<>(true);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                redditNewPosts, HttpMethod.GET, entity, String.class);
        List<RedditPostNew> list = new ArrayList<>();
        try {
            Map<String, Object> mapping = mapper.readValue(response.getBody(), typeRef);
            LinkedHashMap<String, Object> root = (LinkedHashMap<String, Object>) mapping.get("data");
            List<LinkedHashMap<String, Object>> children = (ArrayList<LinkedHashMap<String, Object>>) root.get("children");
            children.forEach(child -> {
                if(iterate.get()) {
                    LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) child.get("data");
                    RedditPostNew post = new RedditPostNew(data);
                    if (post.getId().equals(lastNewPost)) {
                        iterate.set(false);
                    }
                    if (post.getValid()) {
                        list.add(post);
                    }
                }
            });
            lastNewPost = list.get(0).getId();
            redditNewRepository.saveAll(list);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void getRF1PornHot() {
        AtomicReference<Boolean> iterate = new AtomicReference<>(true);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                redditNewF1PornPosts, HttpMethod.GET, entity, String.class);
        List<RedditPostNew> list = new ArrayList<>();
        try {
            Map<String, Object> mapping = mapper.readValue(response.getBody(), typeRef);
            LinkedHashMap<String, Object> root = (LinkedHashMap<String, Object>) mapping.get("data");
            List<LinkedHashMap<String, Object>> children = (ArrayList<LinkedHashMap<String, Object>>) root.get("children");
            children.forEach(child -> {
                if(iterate.get()) {
                    LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) child.get("data");
                    RedditPostNew post = new RedditPostNew(data);
                    if (post.getId().equals(lastNewF1PornPost)) {
                        iterate.set(false);
                    }
                    if (post.getValid()) {
                        list.add(post);
                    }
                }
            });
            lastNewF1PornPost = list.get(0).getId();
            redditNewRepository.saveAll(list);
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
                imgurAlbumUrl2+code, HttpMethod.GET, entity, String.class);
        try {
            List<Object> root = mapper.readValue(response2.getBody(), typeRefList);
            log.info(String.valueOf(root));
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
                .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
    }

}
