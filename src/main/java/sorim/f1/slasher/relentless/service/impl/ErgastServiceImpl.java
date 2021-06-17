package sorim.f1.slasher.relentless.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sorim.f1.slasher.relentless.entities.ergast.Race;
import sorim.f1.slasher.relentless.model.ergast.ErgastResponse;
import sorim.f1.slasher.relentless.repository.ErgastRaceRepository;
import sorim.f1.slasher.relentless.service.ErgastService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ErgastServiceImpl implements ErgastService {
        private final static String  CURRENT_SEASON = "http://ergast.com/api/f1/current.json";

    private final ErgastRaceRepository ergastRaceRepository;
    @Override
    public List<Race> fetchCurrentSeason() {
        RestTemplate restTemplate = new RestTemplate();
        ErgastResponse ergastResponse = restTemplate
                .getForObject(CURRENT_SEASON, ErgastResponse.class);
        return ergastResponse.getMrData().getRaceTable().getRaces();
    }

    @Override
    public List<Race> getAllRaces() {
        return ergastRaceRepository.findAll();
    }

    @Override
    public void saveRaces(List<Race> races) {
        ergastRaceRepository.saveAll(races);
    }

    @Override
    public Race fetchSingleRace() {
        return ergastRaceRepository.findByRound(6);
    }
}
