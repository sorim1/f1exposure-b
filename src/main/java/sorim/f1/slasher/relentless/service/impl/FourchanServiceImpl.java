package sorim.f1.slasher.relentless.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sorim.f1.slasher.relentless.entities.*;
import sorim.f1.slasher.relentless.model.FourchanCatalog;
import sorim.f1.slasher.relentless.model.FourchanPost;
import sorim.f1.slasher.relentless.model.FourchanThread;
import sorim.f1.slasher.relentless.repository.FourChanImageRepository;
import sorim.f1.slasher.relentless.repository.FourChanPostRepository;
import sorim.f1.slasher.relentless.repository.PropertiesRepository;
import sorim.f1.slasher.relentless.repository.StreamableRepository;
import sorim.f1.slasher.relentless.service.FourchanService;
import sorim.f1.slasher.relentless.service.InstagramService;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FourchanServiceImpl implements FourchanService {

    private static final String CATALOG_URL = "https://a.4cdn.org/sp/catalog.json";
    private static final String THREAD_URL = "https://a.4cdn.org/sp/thread/{threadNumber}.json";

    private static final String GOOGLE_REVERSE_IMAGE ="https://www.google.com/searchbyimage?image_url=";

    private static String NO_DUPLICATES_FOUND ="ene druge veli";

    private static Integer processedThread = 0;

    private final InstagramService instagramService;
    private final PropertiesRepository propertiesRepository;
    private final FourChanPostRepository fourChanPostRepository;
    private final StreamableRepository streamableRepository;

    private final FourChanImageRepository fourChanImageRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    RestTemplate restTemplate = new RestTemplate();
    WebClient client;

    @Override
    public List<FourChanPostEntity> get4chanPosts(Integer page) {
        Pageable paging = PageRequest.of(page, 21);
        return fourChanPostRepository.findAllByOrderByIdDesc(paging);
    }

    @Override
    public List<Streamable> getStreamables() {
        return streamableRepository.findAllByOrderByIdDesc();
    }

    @Override
    public Boolean fetch4chanPosts() {
        Boolean includesActiveThread = false;
        List<Integer> f1ThreadNumbers = getPreviousF1ThreadNumbers();
        if (f1ThreadNumbers.isEmpty()) {
            log.error("FOUND NO PreviousF1ThreadNumbers");
            f1ThreadNumbers = getF1ThreadNumbers();
            includesActiveThread = true;
        }
        f1ThreadNumbers = removeProcessedThreads(f1ThreadNumbers);
        log.info("fetch4chanPosts:" + f1ThreadNumbers);
        if (!f1ThreadNumbers.isEmpty()) {
            fetchF1Threads(f1ThreadNumbers);
            if (!includesActiveThread) {
                setProcessedThread(f1ThreadNumbers.get(f1ThreadNumbers.size() - 1));
            }
        }
        return true;
    }

    private List<Integer> removeProcessedThreads(List<Integer> f1ThreadNumbers) {
        List<Integer> response = new ArrayList();
        Collections.sort(f1ThreadNumbers);
        f1ThreadNumbers.forEach(threadNo -> {
            if (threadNo > processedThread) {
                response.add(threadNo);
            }
        });
        return response;
    }

    @Override
    public Boolean deleteFourChanPosts() {
        fourChanPostRepository.deleteAll();
        streamableRepository.deleteAll();
        return true;
    }


    private void fetchF1Threads(List<Integer> f1ThreadNumbers) {
        initWebClient();
        f1ThreadNumbers.forEach(this::fetchSingleF1Thread);
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

    private void fetchSingleF1Thread(Integer threadId) {
        List<FourChanPostEntity> chanPosts = new ArrayList<>();
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("threadNumber", String.valueOf(threadId));
        FourchanThread response = restTemplate
                .getForObject(THREAD_URL, FourchanThread.class, uriVariables);

        response.getPosts().forEach(post -> {
            if (checkFourchanImageSize(post)) {
                int status;
                if(checkFourchanImageUniqueness(post)){
                    status = 1;
                } else {
                    status = 2;
                }
                chanPosts.add(new FourChanPostEntity(post, status));
            }
        });

        fourChanPostRepository.saveAll(chanPosts);
        fetchImages(chanPosts);
    }
    private void fetchImages(List<FourChanPostEntity> chanPosts) {
        List<FourChanImageRow> images = new ArrayList<>();
        chanPosts.forEach(post -> {
            byte[] imageBytes = instagramService.getImageFromUrl(post.getUrl());
            images.add(FourChanImageRow.builder().id(post.getId()).image(imageBytes).build());
        });
        fourChanImageRepository.saveAll(images);
    }

    private boolean checkFourchanImageSize(FourchanPost post) {
        if (post.getW() == null || post.getH() == null) {
            return false;
        }
        return post.getW() + post.getH() > 1500;
    }

    private boolean checkFourchanImageUniqueness(FourchanPost post) {
            String url = "https://i.4cdn.org/sp/" + post.getTim() + post.getExt();
            Boolean notFoundOnGoogle = reverseGoogleImage(url, false);
            log.info(url + " - UNIQUE: " + notFoundOnGoogle);
            return notFoundOnGoogle;
    }

    @Override
    public Boolean reverseGoogleImage(String url, Boolean logResponse) {
        if(client==null){
            initWebClient();
        }
        try {
            Page page = client.getPage(GOOGLE_REVERSE_IMAGE + url);
            WebResponse response = page.getWebResponse();
            String responseString =response.getContentAsString();
            if(logResponse){
                log.info("checkUrlUsingHtmlUnit");
                log.info(responseString);
            } else {
                Thread.sleep(5000);
            }
            return responseString.contains(NO_DUPLICATES_FOUND);
        }catch(Exception e){
            log.error("checkUrlUsingHtmlUnit ERROR");
            e.printStackTrace();
        }finally{
            client.close();
        }
        return false;
    }

    @Override
    public List<FourChanPostEntity> getChanPostsByStatus(Integer status) {
        Pageable paging = PageRequest.of(0, 20);
        return fourChanPostRepository.findAllByThreadOrderByIdAsc(status, paging);
    }

    @Override
    public List<Integer> getChanPostsSums() {
        Integer one = Math.toIntExact(fourChanPostRepository.countRowsByStatus(1));
        Integer two = Math.toIntExact(fourChanPostRepository.countRowsByStatus(2));
        Integer three = Math.toIntExact(fourChanPostRepository.countRowsByStatus(3));
        Integer four = Math.toIntExact(fourChanPostRepository.countRowsByStatus(4));
        Integer five = Math.toIntExact(fourChanImageRepository.countRows());
        return Arrays.asList(one,two,three,four,five);
    }

    @Override
    public String setNoDuplicatesFound(String newValue) {
        String response = NO_DUPLICATES_FOUND + " -> ";
        NO_DUPLICATES_FOUND = newValue;
        response = response + NO_DUPLICATES_FOUND;
        return response;
    }

    private void initWebClient() {
        client = new WebClient(BrowserVersion.CHROME);
        client.setCssErrorHandler(new SilentCssErrorHandler());

    }

    private List<Integer> getF1ThreadNumbers() {
        List<Integer> f1ThreadNumbers = new ArrayList();
        String responseString = restTemplate
                .getForObject(CATALOG_URL, String.class);
        TypeReference<List<FourchanCatalog>> typeRef = new TypeReference<>() {
        };
        try {
            List<FourchanCatalog> response = mapper.readValue(responseString, typeRef);

            response.forEach(page -> page.getThreads().forEach(thread -> {
                if (thread.getSub() != null && thread.getSub().toUpperCase().contains("/F1/")) {
                    f1ThreadNumbers.add(thread.getNo());
                }
            }));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return f1ThreadNumbers;
    }

    private List<Integer> getPreviousF1ThreadNumbers() {
        List<Integer> f1ThreadNumbers = new ArrayList();
        String responseString = restTemplate
                .getForObject(CATALOG_URL, String.class);
        TypeReference<List<FourchanCatalog>> typeRef = new TypeReference<>() {
        };
        try {
            List<FourchanCatalog> response = mapper.readValue(responseString, typeRef);

            response.forEach(page -> page.getThreads().forEach(thread -> {
                try {
                    if (thread.getSub() != null && thread.getSub().toUpperCase().contains("/F1/")) {
                        Integer index = thread.getCom().indexOf("/sp/thread/");
                        if (index >= 0) {
                            String threadNoString = thread.getCom().substring(index + 11, index + 20);
                            Integer threadNo = Integer.valueOf(threadNoString);
                            f1ThreadNumbers.add(threadNo);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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

    private String search4ChanThread(Integer threadId) {
        AtomicReference<String> strawpollId = new AtomicReference<>();
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("threadNumber", String.valueOf(threadId));

        FourchanThread response = restTemplate
                .getForObject(THREAD_URL, FourchanThread.class, uriVariables);

        response.getPosts().stream().filter(post -> post.getCom() != null && post.getCom().toUpperCase().contains("STRAWPOLL.COM/"))
                .forEach(strawPollPost -> {
                    String postText = strawPollPost.getCom().replace("<wbr>","");
                   int counter = 0;
                    if (postText.contains("VERY IMPORTANT POLL")) {
                        counter += 2;
                    }
                    if (postText.contains("EXPOSURE POLL")) {
                        counter++;
                    }
                    if (postText.contains("EXPOSED POLL")) {
                        counter++;
                    }
                    if (counter >= 2) {
                        log.info("FOUND STRAWPOLL POST");
                        Integer index = postText.toUpperCase().indexOf("STRAWPOLL.COM/POLLS/") + 20;
                        String strawpollString = postText.substring(index, index + 11);
                        log.info("strawpollString: {}", strawpollString);
                        strawpollId.set(strawpollString);
                    }
                });
        return strawpollId.get();
    }


}
