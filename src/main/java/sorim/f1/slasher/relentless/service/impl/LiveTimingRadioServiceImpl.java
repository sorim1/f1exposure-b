package sorim.f1.slasher.relentless.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sorim.f1.slasher.relentless.model.enums.RoundEnum;
import sorim.f1.slasher.relentless.model.livetiming.Driver;
import sorim.f1.slasher.relentless.model.livetiming.RaceAnalysis;
import sorim.f1.slasher.relentless.model.livetiming.RadioData;
import sorim.f1.slasher.relentless.model.livetiming.UpcomingRaceAnalysis;
import sorim.f1.slasher.relentless.service.LiveTimingRadioService;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LiveTimingRadioServiceImpl implements LiveTimingRadioService {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void enrichUpcomingRaceAnalysisWithRadioData(UpcomingRaceAnalysis upcomingRaceAnalysis, String jsonStream, RoundEnum session) {
        List<Driver> drivers = getDriverList(upcomingRaceAnalysis, session);
        List<RadioData> radioData = getRadioData(drivers, jsonStream);
        updateDriverList(upcomingRaceAnalysis, drivers, session, radioData);
    }

    @Override
    public List<RadioData> enrichRaceAnalysisWithRadioData(List<Driver> drivers, String jsonStream) {
        return getRadioData(drivers, jsonStream);
    }

    private RadioData getRadioDataFromObject(Integer id, Object object, List<Driver> drivers) {
        LinkedHashMap<String, Object> dataMap = (LinkedHashMap<String, Object>) object;
        RadioData entry = new RadioData();
        entry.setId(id);
        ZonedDateTime dateTime = ZonedDateTime.parse((String) dataMap.get("Utc"));
        entry.setUtc(dateTime);
        entry.setDriverNumber((String) dataMap.get("RacingNumber"));
        entry.setPath((String) dataMap.get("Path"));
        Optional<Driver> driver = drivers.stream().filter(ds -> ds.getNum().equals(entry.getDriverNumber()))
                .findFirst();
        if (driver.isPresent()) {
            entry.setDriverName(driver.get().firstName + " " + driver.get().lastName);
            driver.get().getRadioData().add(entry);
        } else {
            entry.setDriverName(entry.getPath().substring(10, 16));
            log.error("driver not found: " + entry.getDriverNumber());
        }
        return entry;
    }

    private void updateDriverList(UpcomingRaceAnalysis upcomingRaceAnalysis, List<Driver> drivers, RoundEnum session, List<RadioData> radioData) {
        switch (session) {
            case QUALIFYING:
                upcomingRaceAnalysis.setQuali(drivers);
                upcomingRaceAnalysis.setQualiRadio(radioData);
                break;
            case PRACTICE_1:
                upcomingRaceAnalysis.setFp1(drivers);
                upcomingRaceAnalysis.setFp1Radio(radioData);
                break;
            case PRACTICE_2:
                upcomingRaceAnalysis.setFp2(drivers);
                upcomingRaceAnalysis.setFp2Radio(radioData);
                break;
            case PRACTICE_3:
                upcomingRaceAnalysis.setFp3(drivers);
                upcomingRaceAnalysis.setFp3Radio(radioData);
                break;
            case SPRINT_QUALIFYING:
                upcomingRaceAnalysis.setSprintQuali(drivers);
                upcomingRaceAnalysis.setSprintQualiRadio(radioData);
                break;
            default:
                log.error("CASE NE POSTOJI: " + session);
        }
    }

    private List<Driver> getDriverList(UpcomingRaceAnalysis upcomingRaceAnalysis, RoundEnum session) {
        switch (session) {
            case QUALIFYING:
                return upcomingRaceAnalysis.getQuali();
            case PRACTICE_1:
                return upcomingRaceAnalysis.getFp1();
            case PRACTICE_2:
                return upcomingRaceAnalysis.getFp2();
            case PRACTICE_3:
                return upcomingRaceAnalysis.getFp3();
            case SPRINT_QUALIFYING:
                return upcomingRaceAnalysis.getSprintQuali();
            default:
                log.error("CASE NE POSTOJI: " + session);
                return Collections.emptyList();
        }
    }

    public List<RadioData> getRadioData(List<Driver> drivers, String jsonStream) {
        TypeReference<HashMap<String, Object>> typeRef = new TypeReference<>() {
        };
        List<RadioData> radioData = new ArrayList<>();
        List<String> radioDataLines = Arrays.asList(jsonStream.split(System.lineSeparator()));
        radioDataLines.forEach(stringLine -> {
            String jsonLine = stringLine.substring(stringLine.indexOf("{"));
            Map<String, Object> mapping = null;
            try {
                mapping = mapper.readValue(jsonLine, typeRef);
            } catch (JsonProcessingException e) {
                log.error("enrichUpcomingRaceAnalysisWithRadioData JsonProcessingException", e);
                e.printStackTrace();
            }

            Object captures = mapping.get("Captures");
            if (captures instanceof ArrayList) {
                ((ArrayList<?>) captures).forEach(object -> {
                    RadioData entry = getRadioDataFromObject(0, object, drivers);
                    radioData.add(entry);
                });
            } else {
                LinkedHashMap<String, Object> capturesMap = (LinkedHashMap<String, Object>) captures;
                capturesMap.forEach((k, v) -> {
                    LinkedHashMap<String, Object> dataMap = (LinkedHashMap<String, Object>) v;
                    RadioData entry = getRadioDataFromObject(Integer.valueOf(k), dataMap, drivers);
                    radioData.add(entry);
                });
            }
        });
        if(!radioData.isEmpty()){
            radioData.get(0).setActive(true);
        }
        return radioData;
    }
}
