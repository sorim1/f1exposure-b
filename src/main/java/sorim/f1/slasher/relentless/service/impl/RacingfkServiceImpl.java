package sorim.f1.slasher.relentless.service.impl;

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
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sorim.f1.slasher.relentless.configuration.MainProperties;
import sorim.f1.slasher.relentless.entities.Replay;
import sorim.f1.slasher.relentless.repository.ReplayRepository;
import sorim.f1.slasher.relentless.service.RacingfkService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RacingfkServiceImpl implements RacingfkService {

    private final MainProperties properties;
    private final ReplayRepository replayRepository;
    private static String replaysUrl = "https://f1hd.net/category/f1/page/";

    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    TypeReference<HashMap<String, Object>> typeRef = new TypeReference<>() {
    };
    TypeReference<ArrayList<Object>> typeRefList = new TypeReference<>() {
    };
    private final ObjectMapper mapper = new ObjectMapper();


    @PostConstruct
    void init() {
        replaysUrl = properties.getReplaysUrl();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("user-agent", "Mozilla/4.8 Firefox/21.0");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

    }

    @Override
    public Boolean fetchReplayLinks() {
        Boolean iterate = true;
        Integer page = 1;
        List<Replay> list = new ArrayList<>();
        String latestReplayTitle = null;
        Replay latestReplay = replayRepository.findFirstByOrderByIdDesc();
        if (latestReplay != null) {
            latestReplayTitle = latestReplay.getTitle();
        }
        log.info("latestReplayTitle");
        log.info(latestReplayTitle);
        do {
            String rawHtml = getHtmlResponse(page);
            Document doc = Jsoup.parse(rawHtml);

            Elements h2Elements = doc.getElementsByTag("h2");
            if (h2Elements.size() != 10) {
                iterate = false;
            } else {
                log.error("NIJE KRAJ LISTE");
            }
            for (int i = 0; i < h2Elements.size(); i++) {
                Element element = h2Elements.get(i);
                Elements aElements = element.getElementsByTag("a");
                Element aElement = aElements.get(0);
                if (aElement.wholeText().equals(latestReplayTitle)) {
                    i = h2Elements.size();
                    iterate = false;
                } else {
                    Replay entry = Replay.builder().title(aElement.wholeText())
                            .url(aElement.attr("href")).build();
                    list.add(entry);
                }
            }
            page++;
            if (page > 10) {
                iterate = false;
            }
        } while (iterate);
        //  Collections.reverse(list);

        for (int i = list.size() - 1; i >= 0; i--) {
            replayRepository.save(list.get(i));
        }
        return true;
    }

    @Override
    public List<Replay> getReplays(Integer page) {
        Pageable paging = PageRequest.of(page, 15);
        return replayRepository.findAllByOrderByIdDesc(paging);
    }

    private String getHtmlResponse(Integer page) {
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                replaysUrl + page, HttpMethod.GET, entity, String.class);
        String rawHtml = response.getBody();
        return rawHtml;
    }
}
