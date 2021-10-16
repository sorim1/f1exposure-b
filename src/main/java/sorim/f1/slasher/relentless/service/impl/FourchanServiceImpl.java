package sorim.f1.slasher.relentless.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sorim.f1.slasher.relentless.configuration.MainProperties;
import sorim.f1.slasher.relentless.entities.ForchanPost;
import sorim.f1.slasher.relentless.model.FourchanCatalog;
import sorim.f1.slasher.relentless.model.FourchanThread;
import sorim.f1.slasher.relentless.service.FourchanService;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FourchanServiceImpl implements FourchanService {

    private final MainProperties properties;
    //private final ForchanRepository forchanRepository;
    private static final String catalogUrl = "https://a.4cdn.org/sp/catalog.json";
    private static final String threadUrl = "https://a.4cdn.org/sp/thread/{threadNumber}.json";
    RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<ForchanPost> get4chanPosts(Integer page) {
        Pageable paging = PageRequest.of(page, 21);
        return null;
        //return twitterRepository.findAllByOrderByCreatedAtDesc(paging);
    }

    @Override
    public List<FourchanCatalog> fetch4chanPosts() {
        List<FourchanCatalog> response = restTemplate
                .getForObject(catalogUrl, ArrayList.class);
        try {
            log.info(String.valueOf(response));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public String getExposureStrawpoll() {
        List<Integer> f1ThreadNumbers = new ArrayList();
        String strawpoll = null;
        List<LinkedHashMap<String, Object>> response = restTemplate
                .getForObject(catalogUrl, ArrayList.class);
        try {
            log.info("---");
            List<LinkedHashMap<String, Object>> listOfPosts = (List<LinkedHashMap<String, Object>>) response.get(0).get("threads");
            log.info(String.valueOf(listOfPosts.get(0).get("no")));
            log.info(String.valueOf(listOfPosts.get(0).get("sub")));

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
                        log.info(strawPollPost.getCom());
                        Integer index = strawPollPost.getCom().indexOf("STRAWPOLL.COM/") + 14;
                        strawpollId.set(strawPollPost.getCom().substring(index, index + 9));
                    }
                });
        return strawpollId.get();
    }


}
