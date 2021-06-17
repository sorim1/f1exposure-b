package sorim.f1.slasher.relentless.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sorim.f1.slasher.relentless.model.ergast.ErgastResponse;
import sorim.f1.slasher.relentless.repository.ErgastRaceRepository;
import sorim.f1.slasher.relentless.service.ErgastService;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ErgastServiceImpl implements ErgastService {
        private final static String  CURRENT_SEASON = "http://ergast.com/api/f1/current.json";

    private final ErgastRaceRepository ergastRaceRepository;
    @Override
    public String fetchCurrentSeason() {
        RestTemplate restTemplate = new RestTemplate();
        String foo1 = restTemplate
                .getForObject(CURRENT_SEASON, String.class);
        log.info(String.valueOf(foo1));
        ErgastResponse foo = restTemplate
                .getForObject(CURRENT_SEASON, ErgastResponse.class);
        log.info(String.valueOf(foo));
        ergastRaceRepository.saveAll(foo.getMrData().getRaceTable().getRaces());
        return foo.getMrData().getUrl();
    }


}
