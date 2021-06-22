package sorim.f1.slasher.relentless.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sorim.f1.slasher.relentless.model.livetiming.FrontendGraphWeatherData;
import sorim.f1.slasher.relentless.model.livetiming.LiveTimingData;
import sorim.f1.slasher.relentless.model.livetiming.RaceAnalysis;
import sorim.f1.slasher.relentless.model.livetiming.WeatherData;
import sorim.f1.slasher.relentless.service.LiveTimingService;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@RequestMapping("liveTiming")
public class LiveTimingController {

    private final LiveTimingService service;
    //TODO validacija, zasebni key za admin sučelje, ovo je isto admin-only kontroler

    @GetMapping("/getLatestRaceData")
    boolean getLatestRaceData() throws Exception {
        service.getLatestRaceData();
        return true;
    }

    @GetMapping("/getAllRaceData/{year}")
    boolean getAllRaceDataFromErgastTable(@PathVariable("year") String year) throws Exception {
        log.info("getAllRaceDataFromErgastTable: {}", year);
        service.getAllRaceDataFromErgastTable(year);
        return true;
    }

    @GetMapping("/processSingleRace")
    LiveTimingData processSingleRace() throws Exception {
        return service.processSingleRace();
    }

    @GetMapping("/getWeather")
    FrontendGraphWeatherData getWeather() throws Exception {
        WeatherData weatherData = service.getWeather();
        return new FrontendGraphWeatherData(weatherData, null);
    }

    @GetMapping("/getRaceAnalysis")
    RaceAnalysis getRaceAnalysis() throws Exception {
        return service.getRaceAnalysis();
    }
}
