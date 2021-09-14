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
import sorim.f1.slasher.relentless.service.ForchanService;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ForchanServiceImpl implements ForchanService {

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
    public List<ForchanPost> fetch4chanPosts() {
        List<ForchanPost> list = new ArrayList<>();
        List<Object> response = restTemplate
                .getForObject(catalogUrl, ArrayList.class);
        try {
            log.info(String.valueOf(response));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
