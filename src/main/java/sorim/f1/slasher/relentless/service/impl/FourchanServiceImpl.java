package sorim.f1.slasher.relentless.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.*;
import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import sorim.f1.slasher.relentless.configuration.MainProperties;
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
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

    private final MainProperties mainProperties;
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
                if (".jpg".equals(post.getExt())) {
                    chanPosts.add(new FourChanPostEntity(post, 1));
                }
//                if (".png".equals(post.getExt())) {
//                    chanPosts.add(new FourChanPostEntity(post, 2));
//                }
            }
        });

        fourChanPostRepository.saveAll(chanPosts);
        fetchImages(chanPosts);
    }
    private void fetchImages(List<FourChanPostEntity> chanPosts) {
        List<FourChanImageRow> images = new ArrayList<>();
        chanPosts.forEach(post -> {
            byte[] imageBytes = instagramService.getImageFromUrl(post.getUrl());
            images.add(FourChanImageRow.builder().id(post.getId()).status(post.getStatus()).image(imageBytes).build());
        });
        fourChanImageRepository.saveAll(images);
    }
    private void uploadImageToDatabase(Integer id, Integer status, MultipartFile file) {
        try {
            FourChanImageRow image  = FourChanImageRow.builder().id(id).status(status).image(file.getBytes()).build();
            fourChanImageRepository.save(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private boolean checkFourchanImageSize(FourchanPost post) {
        if (post.getW() == null || post.getH() == null) {
            return false;
        }
        if (".webm".equals(post.getExt())) {
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
        return fourChanPostRepository.findAllByStatusOrderByIdAsc(status, paging);
    }

    @Override
    public List<Integer> getChanPostsSums() {
        Integer one = Math.toIntExact(fourChanPostRepository.countRowsByStatus(1));
        Integer two = Math.toIntExact(fourChanPostRepository.countRowsByStatus(2));
        Integer three = Math.toIntExact(fourChanPostRepository.countRowsByStatus(3));
        Integer four = Math.toIntExact(fourChanPostRepository.countRowsByStatus(4));
        Integer five = Math.toIntExact(fourChanPostRepository.countRowsByStatus(5));
        Integer six = Math.toIntExact(fourChanPostRepository.countRowsByStatus(6));
        Integer seven = Math.toIntExact(fourChanPostRepository.countRowsByStatus(7));
        Integer eight = Math.toIntExact(fourChanPostRepository.countRowsByStatus(8));
        Integer count = Math.toIntExact(fourChanImageRepository.countRows());
        return Arrays.asList(one,two,three,four,five, six, seven, eight, count);
    }

    @Override
    public String setNoDuplicatesFound(String newValue) {
        String response = NO_DUPLICATES_FOUND + " -> ";
        NO_DUPLICATES_FOUND = newValue;
        response = response + NO_DUPLICATES_FOUND;
        return response;
    }

    @Override
    public List<FourChanPostEntity> saveChanPosts(List<FourChanPostEntity> posts) {
        List<FourChanPostEntity> deletePosts = new ArrayList<>();
        List<FourChanPostEntity> savePosts = new ArrayList<>();
        posts.forEach(post->{
            if(post.getStatus()==7){
                deletePosts.add(post);
            } else {
                savePosts.add(post);
            }
        });
        savePosts.forEach(post-> fourChanImageRepository.updateStatusById(post.getId(), post.getStatus()));
        fourChanPostRepository.saveAll(savePosts);
        deletePosts.forEach(post-> fourChanImageRepository.deleteById(String.valueOf(post.getId())));
        fourChanPostRepository.deleteAll(deletePosts);
        return getChanPostsByStatus(1);
    }


    @Override
    public String postToInstagram(boolean personalMeme) throws IGLoginException {
        List<Integer> approvedPosts = Arrays.asList(4,5);
        FourChanPostEntity chanPost = null;
        FourChanImageRow chanImage;
        if(personalMeme){
            chanPost = fourChanPostRepository.findFirstByIdLessThanAndStatusInOrderByIdAsc(100000,approvedPosts);
        }
        if(!personalMeme || chanPost==null){
            chanPost = fourChanPostRepository.findFirstByIdGreaterThanAndStatusInOrderByIdAsc(100000,approvedPosts);
        }

        if(chanPost!=null) {
            chanImage = fourChanImageRepository.findFirstById(chanPost.getId());
            while (chanImage == null) {
                fourChanPostRepository.delete(chanPost);
                if (personalMeme) {
                    chanPost = fourChanPostRepository.findFirstByIdLessThanAndStatusInOrderByIdAsc(10000, approvedPosts);
                } else {
                    chanPost = fourChanPostRepository.findFirstByIdGreaterThanAndStatusInOrderByIdAsc(10000, approvedPosts);
                }
                chanImage = fourChanImageRepository.findFirstById(chanPost.getId());
            }
            String response = instagramService.postToInstagram(chanPost, chanImage);
            if(response!=null){
                chanPost.setStatus(8);
                chanImage.setStatus(8);
                fourChanPostRepository.save(chanPost);
                fourChanImageRepository.save(chanImage);
                return response;
            } else {
                fourChanPostRepository.delete(chanPost);
                fourChanImageRepository.delete(chanImage);
                return postToInstagram(personalMeme);
            }
        }
        return "NO POST FOUND - personalMeme:" +personalMeme;
    }

    @Override
    public List<String> saveChanImages(MultipartFile[] files) {
        List<FourChanPostEntity> chanPosts = new ArrayList<>();
        Long time = System.currentTimeMillis();
        String num = String.valueOf(time);
        num = num.substring(3, 8);
        String baseUrl  = mainProperties.getUrl() + "/social/image/";
        AtomicReference<Integer> id = new AtomicReference<>(Integer.valueOf(num));
        List<String> fileNames = new ArrayList<>();
        Arrays.stream(files).forEach(file -> {
            fileNames.add(id.get() + " - " + file.getOriginalFilename());
            chanPosts.add(new FourChanPostEntity(id.get(), baseUrl, 3));
            uploadImageToDatabase(id.get(), 3, file);
            id.getAndSet(id.get() + 1);
        });
        fourChanPostRepository.saveAll(chanPosts);
        return fileNames;
    }

    @Override
    public byte[] getChanImage(Integer id) {
        FourChanImageRow result = fourChanImageRepository.findFirstById(id);
        return result.getImage();
    }

    @Override
    //@PostConstruct
    public void cleanup() {
        try{

        List<Integer> safeDelete = Arrays.asList(7,8);
        log.info("chan cleanup start");
        fourChanPostRepository.deleteByStatusIn(safeDelete);
            log.info("chan cleanup 7,8 done1");
        fourChanImageRepository.deleteByStatusIn(safeDelete);
            log.info("chan cleanup 7,8 done1");
            int one = Math.toIntExact(fourChanPostRepository.countRowsByStatus(1));
            if(one>600){
                fourChanPostRepository.deleteAllByStatus(1);
                fourChanImageRepository.deleteAllByStatus(1);
            }
            log.info("chan cleanup end");
        }catch(Exception e){
            log.error("cleanup failed", e);
        }
    }
    @Override
    public Boolean deleteChanByStatus(Integer status){
        fourChanPostRepository.deleteAllByStatus(status);
        fourChanImageRepository.deleteAllByStatus(status);
        return true;
    }
    @Override
    public Integer deleteByStatus(Integer status){
        List<Integer> statusList = Collections.singletonList(status);
        fourChanPostRepository.deleteByStatusIn(statusList);
        return fourChanImageRepository.deleteByStatusIn(statusList);
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
    @Override
    public byte[] getAcceptedPngImages(HttpServletResponse response) throws IOException {
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=acceptedf1exposurePngImages.zip");
        List<FourChanPostEntity> status6Posts = fourChanPostRepository.findAllByStatus(6);
        List<Integer> ids = new ArrayList<>();
        status6Posts.forEach(post->{
            ids.add(post.getId());
        });
        List<FourChanImageRow> status6Images = fourChanImageRepository.findAllByIdIn(ids);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
        status6Images.forEach(image->{
            try {
            ZipEntry entry = new ZipEntry(image.getId() + ".png");
            entry.setSize(image.getImage().length);
                zos.putNextEntry(entry);
                zos.write(image.getImage());
                zos.closeEntry();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        zos.finish();
        return baos.toByteArray();
    }

}
