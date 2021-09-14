package sorim.f1.slasher.relentless.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sorim.f1.slasher.relentless.configuration.MainProperties;
import sorim.f1.slasher.relentless.entities.RedditPostNew;
import sorim.f1.slasher.relentless.entities.RedditPostTop;
import sorim.f1.slasher.relentless.repository.RedditNewRepository;
import sorim.f1.slasher.relentless.repository.RedditTopRepository;
import sorim.f1.slasher.relentless.service.RedditService;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedditServiceImpl implements RedditService {

    private final MainProperties properties;
    private final RedditNewRepository redditNewRepository;
    private final RedditTopRepository redditTopRepository;

    private static final String redditNew200Posts = "https://reddit.com/r/formula1/new/.json?limit=200";
    private static final String redditTop200Posts = "https://reddit.com/r/formula1/top/.json?limit=200";
    private static final String imgurAlbumUrl = "https://api.imgur.com/3/album/{code}";
    private static final String imgurAlbumUrl2 = "https://api.imgur.com/3/album/";
    private static String lastNewPost = "";
    private static final String lastTopPost = "";

    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
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
        getRedditNew();
        getRedditTop();
    }

    private void getRedditNew() {
        AtomicReference<Boolean> iterate = new AtomicReference<>(true);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                redditNew200Posts, HttpMethod.GET, entity, String.class);
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

    private void getRedditTop() {
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                redditTop200Posts, HttpMethod.GET, entity, String.class);
        List<RedditPostTop> list = new ArrayList<>();
        try {
            Map<String, Object> mapping = mapper.readValue(response.getBody(), typeRef);
            LinkedHashMap<String, Object> root = (LinkedHashMap<String, Object>) mapping.get("data");
            List<LinkedHashMap<String, Object>> children = (ArrayList<LinkedHashMap<String, Object>>) root.get("children");
            children.forEach(child -> {
                LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) child.get("data");
                RedditPostTop post = new RedditPostTop(data);
                if (post.getValid()) {
                    list.add(post);
                }

            });
            redditTopRepository.saveAll(list);
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
        headers.add("user-agent", "Mozilla/4.9 Firefox/23.0");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

    }

}
