package sorim.f1.slasher.relentless.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sorim.f1.slasher.relentless.configuration.MainProperties;
import sorim.f1.slasher.relentless.entities.*;
import sorim.f1.slasher.relentless.handling.Logger;
import sorim.f1.slasher.relentless.model.*;
import sorim.f1.slasher.relentless.model.enums.ExposureStatusEnum;
import sorim.f1.slasher.relentless.repository.*;
import sorim.f1.slasher.relentless.service.ErgastService;
import sorim.f1.slasher.relentless.service.ExposureStrawpollService;
import sorim.f1.slasher.relentless.service.FourchanService;
import sorim.f1.slasher.relentless.service.StrawpollService;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StrawpollServiceImpl implements StrawpollService {
    private static final String strawPollApiV3 = "https://api.strawpoll.com/v3/polls/";

    @Override
    public StrawpollModelTwo createStrawpoll() {
        return null;
    }

    @Override
    public String getStrawpollDrivers() {
        return null;
    }
    @Override
    public String generateStrawpollDrivers() {
        return null;
    }
    @Override
    public String setStrawpollDrivers(String drivers) {
        return null;
    }
}
