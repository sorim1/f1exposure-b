package sorim.f1.slasher.relentless.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sorim.f1.slasher.relentless.entities.Exposed;
import sorim.f1.slasher.relentless.entities.ExposedChart;
import sorim.f1.slasher.relentless.entities.ExposedVote;
import sorim.f1.slasher.relentless.entities.Tempo;
import sorim.f1.slasher.relentless.repository.ExposedRepository;
import sorim.f1.slasher.relentless.repository.ExposedVoteRepository;
import sorim.f1.slasher.relentless.service.MainService;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MainServiceImpl implements MainService {

    private final static Integer raceId=1;
    private final ExposedVoteRepository exposedVoteRepository;
    private final ExposedRepository exposedRepository;


    @Override
    public Tempo getTempo(){
        return Tempo.builder().success(true).tempo1("tempo_1").tempo2(2).build();
    }

    @Override
    public Boolean exposeDrivers(String[] exposedList, String ipAddress) {
            log.info("exposeDrivers: {}", exposedList );

        boolean alreadyExists = exposedVoteRepository.existsExposedVoteByIpAddress(ipAddress);
        exposedVoteRepository.save(ExposedVote.builder().drivers(exposedList).ipAddress(ipAddress).build());
        for(String s: exposedList){
            exposedRepository.incrementExposed(raceId, Integer.valueOf(s));
        }
        return !alreadyExists;
    }

    @Override
    public void validateHeader(String authorization) throws Exception {
        if(!"md123".equals(authorization)){
            throw new AuthenticationException("not authorized");
        }
    }

    @Override
    public String validateIp(HttpServletRequest request) {
        log.info("validateIp1");
        log.info(request.getRemoteAddr());
        log.info(request.getRequestURI());
        log.info(request.getLocalAddr());
        return request.getRemoteAddr();
    }

    @Override
    public ExposedChart getExposedChartData() {
        List<Integer> drivers = new ArrayList<>();
        List<Integer> results = new ArrayList<>();

        List<Exposed> list = exposedRepository.findByRaceId(raceId);
        list.stream().forEach((exposed) -> {
            drivers.add(exposed.getDriverId());
            results.add(exposed.getCounter());
        });
        return ExposedChart.builder()
                .drivers((Integer[]) drivers.toArray())
                .results((Integer[]) results.toArray()).build();
    }
}
