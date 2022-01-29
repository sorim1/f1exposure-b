package sorim.f1.slasher.relentless.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sorim.f1.slasher.relentless.entities.AppProperty;
import sorim.f1.slasher.relentless.entities.FourChanPostEntity;
import sorim.f1.slasher.relentless.model.FourchanCatalog;
import sorim.f1.slasher.relentless.model.FourchanPost;
import sorim.f1.slasher.relentless.model.FourchanThread;
import sorim.f1.slasher.relentless.repository.FourChanPostRepository;
import sorim.f1.slasher.relentless.repository.PropertiesRepository;
import sorim.f1.slasher.relentless.service.FourchanService;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FourchanServiceImpl implements FourchanService {

    private static final String catalogUrl = "https://a.4cdn.org/sp/catalog.json";
    private static final String threadUrl = "https://a.4cdn.org/sp/thread/{threadNumber}.json";
    private static Integer processedThread = 0;
    private final PropertiesRepository propertiesRepository;
    private final FourChanPostRepository fourChanPostRepository;
    private final ObjectMapper mapper = new ObjectMapper();
    RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<FourChanPostEntity> get4chanPosts(Integer page) {
        Pageable paging = PageRequest.of(page, 21);
        return fourChanPostRepository.findAllByOrderByIdDesc(paging);
    }

    @Override
    public Boolean fetch4chanPosts() {
        List<Integer> f1ThreadNumbers = getF1ThreadNumbers();
        fetchF1Threads(f1ThreadNumbers);
        return true;
    }

    private Boolean fetchF1Threads(List<Integer> f1ThreadNumbers) {
        f1ThreadNumbers.forEach(this::fetchSingleF1Thread);
        if (f1ThreadNumbers.size() > 1) {
            setProcessedThread(f1ThreadNumbers.get(1));
        }
        return true;
    }

    private void setProcessedThread(Integer threadId) {
        processedThread = threadId;
        AppProperty ap = AppProperty.builder().name("4CHAN_THREAD").value(String.valueOf(threadId)).build();
        propertiesRepository.save(ap);
    }

    @PostConstruct
    private void init() {
        AppProperty ap = propertiesRepository.findDistinctFirstByName("4CHAN_THREAD");
        if (ap != null) {
            processedThread = Integer.valueOf(ap.getValue());
        } else {
            setProcessedThread(0);
        }
    }

    private Boolean fetchSingleF1Thread(Integer threadId) {
        List<FourChanPostEntity> images = new ArrayList<>();
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("threadNumber", String.valueOf(threadId));
        FourchanThread response = restTemplate
                .getForObject(threadUrl, FourchanThread.class, uriVariables);

        response.getPosts().forEach(post -> {
            if (checkFourchanImage(post)) {
                images.add(new FourChanPostEntity(post, threadId));
            }
        });

        fourChanPostRepository.saveAll(images);
        return true;
    }

    private List<FourChanPostEntity> getTopEntities(List<FourchanPost> posts, Integer threadId) {
        List<FourChanPostEntity> entities = new ArrayList<>();
        posts.forEach(post -> {
            if (post.getReplyCounter() > 4) {
                entities.add(new FourChanPostEntity(post, threadId));
            }
        });
        return entities;
    }

    private boolean checkFourchanImage(FourchanPost post) {
        if (post.getW() == null || post.getH() == null) {
            return false;
        }
        if (post.getW() + post.getH() > 2100) {
            return true;
        }
        return ".webm".equals(post.getExt()) && post.getFsize() > 1700000;
    }

    private void increaseReplyCounters(FourchanPost post, Map<Integer, FourchanPost> map) {
        String text = post.getCom();
        if (text != null) {
            Integer index = text.indexOf("<a href=\"#p");
            while (index >= 0) {
                Integer postNumber = Integer.valueOf(text.substring(index + 11, index + 20));
                if (map.containsKey(postNumber)) {
                    map.get(postNumber).incrementReplyCounter();
                }
                text = text.substring(index + 21);
                index = text.indexOf("<a href=\"#p");
            }
        }

    }

    private List<Integer> getF1ThreadNumbers() {
        List<Integer> f1ThreadNumbers = new ArrayList();
        String responseString = restTemplate
                .getForObject(catalogUrl, String.class);
        TypeReference<List<FourchanCatalog>> typeRef = new TypeReference<>() {
        };
        try {
            List<FourchanCatalog> response = mapper.readValue(responseString, typeRef);

            response.forEach(page -> page.getThreads().forEach(thread -> {
                if (thread.getNo() > processedThread && thread.getSub() != null && thread.getSub().toUpperCase().contains("/F1/")) {
                    f1ThreadNumbers.add(thread.getNo());
                }
            }));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return f1ThreadNumbers;
    }

    @Override
    public String getExposureStrawpoll() {
        String strawpoll = null;
        try {
            List<Integer> f1ThreadNumbers = getF1ThreadNumbers();
            if (!f1ThreadNumbers.isEmpty()) {
                int i = 0;
                do {
                    strawpoll = search4ChanThread(f1ThreadNumbers.get(i));
                    log.info("i - {} - strawpoll - {} - thread - {}", i, strawpoll, f1ThreadNumbers.get(i));
                    if (strawpoll == null) {
                        i++;
                    } else {
                        i = 10;
                    }
                } while (i < f1ThreadNumbers.size() - 1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return strawpoll;
    }

    @Override
    public Boolean deleteFourChanPost(Integer id) {
        Integer count = fourChanPostRepository.deleteById(id);
        return count == 1;
    }

    private String getExposureStrawpoll2() {
        List<Integer> f1ThreadNumbers = new ArrayList();
        String strawpoll = null;
        try {
            List<LinkedHashMap<String, Object>> response = restTemplate
                    .getForObject(catalogUrl, ArrayList.class);

            List<LinkedHashMap<String, Object>> listOfPosts = (List<LinkedHashMap<String, Object>>) response.get(0).get("threads");
            listOfPosts.stream().filter(post -> post.containsKey("sub") && post.get("sub").toString().toUpperCase().contains("/F1/"))
                    .forEach(f1Thread -> {
                        f1ThreadNumbers.add((Integer) f1Thread.get("no"));
                    });
            listOfPosts = (List<LinkedHashMap<String, Object>>) response.get(1).get("threads");
            listOfPosts.stream().filter(post -> post.containsKey("sub") && post.get("sub").toString().toUpperCase().contains("/F1/"))
                    .forEach(f1Thread -> {
                        f1ThreadNumbers.add((Integer) f1Thread.get("no"));
                    });
            if (f1ThreadNumbers.size() > 0) {

                Integer i = 0;
                do {
                    strawpoll = search4ChanThread(f1ThreadNumbers.get(i));
                    log.info("i - {} - strawpoll - {} - thread - {}", i, strawpoll, f1ThreadNumbers.get(i));
                    if (strawpoll == null) {
                        i++;
                    } else {
                        i = 10;
                    }
                } while (i < f1ThreadNumbers.size() - 1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return strawpoll;
    }

    private String search4ChanThread(Integer threadId) {
        AtomicReference<String> strawpollId = new AtomicReference<>();
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("threadNumber", String.valueOf(threadId));

        FourchanThread response = restTemplate
                .getForObject(threadUrl, FourchanThread.class, uriVariables);

        response.getPosts().stream().filter(post -> post.getCom() != null && post.getCom().toUpperCase().contains("STRAWPOLL.COM/"))
                .forEach(strawPollPost -> {
                    int counter = 0;
                    if (strawPollPost.getCom().contains("VERY IMPORTANT POLL")) {
                        counter += 2;
                    }
                    if (strawPollPost.getCom().contains("EXPOSURE POLL")) {
                        counter++;
                    }
                    if (strawPollPost.getCom().contains("EXPOSED POLL")) {
                        counter++;
                    }
                    if (counter >= 2) {
                        log.info("FOUND STRAWPOLL POST");
                        Integer index = strawPollPost.getCom().toUpperCase().indexOf("STRAWPOLL.COM/") + 14;
                        String strawpollString = strawPollPost.getCom().substring(index, index + 9);
                        log.info("strawpollString: {}", strawpollString);
                        strawpollId.set(strawpollString);
                    }
                });
        return strawpollId.get();
    }


}
