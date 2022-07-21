package sorim.f1.slasher.relentless.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sorim.f1.slasher.relentless.configuration.MainProperties;
import sorim.f1.slasher.relentless.model.enums.RoundEnum;
import sorim.f1.slasher.relentless.model.livetiming.*;
import sorim.f1.slasher.relentless.service.LiveTimingRadioService;

import java.io.*;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LiveTimingRadioServiceImpl implements LiveTimingRadioService {
    private static final String SESSION_INFO_URL = "SessionInfo.json";
    private static final String TEAM_RADIO_STREAM = "TeamRadio.jsonStream";
    private static final String LIVETIMING_URL = "https://livetiming.formula1.com/static/";
    private final ObjectMapper mapper = new ObjectMapper();
    private final MainProperties properties;
    RestTemplate restTemplate = new RestTemplate();
    private String relativePath;
    private ZonedDateTime finishedDate;

    @Override
    public void enrichUpcomingRaceAnalysisWithRadioData(UpcomingRaceAnalysis upcomingRaceAnalysis, String jsonStream, RoundEnum session) {
        if (jsonStream != null) {
            List<Driver> drivers = getDriverList(upcomingRaceAnalysis, session);
            List<RadioData> radioData = getRadioData(drivers, jsonStream);
            updateDriverList(upcomingRaceAnalysis, drivers, session, radioData);
        }
    }

    @Override
    public List<RadioData> enrichRaceAnalysisWithRadioData(List<Driver> drivers, String jsonStream) {
        return getRadioData(drivers, jsonStream);
    }

    @Override
    public String generatePostRaceRadio() {
        Boolean success = getPathAndTimestamp();
        if (success) {
            success = getPostRaceRadioMessages();
        }
        return String.valueOf(success);
    }

    private Boolean getPostRaceRadioMessages() {
        String jsonStream = restTemplate.getForObject(LIVETIMING_URL + relativePath + TEAM_RADIO_STREAM, String.class);
        List<RadioData> radioData = getRadioData(null, jsonStream);
        List<RadioData> postRaceRadioData = radioData.stream().filter(entry -> entry.getUtc().isAfter(this.finishedDate)).collect(Collectors.toList());
        log.info("getPostRaceRadioMessages");
        log.info(String.valueOf(radioData.size()));
        log.info(String.valueOf(postRaceRadioData.size()));
        Map<Integer, byte[]> radioMap = new TreeMap<>();
        postRaceRadioData.forEach(entry -> {
            byte[] byteArray = restTemplate.getForObject(LIVETIMING_URL + relativePath + entry.getPath(), byte[].class);
            if (!radioMap.containsKey(Integer.valueOf(entry.getDriverNumber()))) {
                radioMap.put(Integer.valueOf(entry.getDriverNumber()), byteArray);
            } else if (radioMap.get(Integer.valueOf(entry.getDriverNumber())).length < byteArray.length) {
                radioMap.put(Integer.valueOf(entry.getDriverNumber()), byteArray);
            }
        });
        saveRadioMessagesToFiles(radioMap);
        log.info(String.valueOf(radioMap.size()));
        return false;
    }

    private void saveRadioMessagesToFiles(Map<Integer, byte[]> radioMap) {
        String rootPath = properties.getSaveRadioLocation();
        String directoryName = rootPath + finishedDate.toString().substring(0, 10);
        File directory = new File(directoryName);
        AtomicReference<String> mergeImageAndAudioString = new AtomicReference<>("");
        AtomicReference<String> mkvListString = new AtomicReference<>("");
        if (!directory.exists()) {
            directory.mkdir();
        }
        radioMap.forEach((key, value) -> {
            mergeImageAndAudioString.set(generateMergeImageAndAudioString(directoryName, mergeImageAndAudioString.get(), String.valueOf(key)));
            mkvListString.set(generateMkvListString(directoryName, mkvListString.get(), String.valueOf(key)));

            File file = new File(directoryName + "/" + key + ".mp3");
            try {
                OutputStream os = new FileOutputStream(file);
                os.write(value);
                os.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        try {
            mergeImageAndAudioString.set(generateMergeImageAndAudioStringEnding(directoryName, mergeImageAndAudioString.get()));

            BufferedWriter writer = new BufferedWriter(new FileWriter(directoryName + "/generate.bat"));
            writer.write(mergeImageAndAudioString.get());
            writer.close();
            writer = new BufferedWriter(new FileWriter(directoryName + "/list.txt"));
            writer.write(mkvListString.get());
            writer.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String generateMkvListString(String dateDirectory, String input, String key) {
        String response = input;
        response += "file '" + dateDirectory + "/" + key + ".mkv'";
        response += System.lineSeparator();
        return response;
    }

    private String generateMergeImageAndAudioString(String dateDirectory, String input, String key) {
        String response = input;
        response += "ffmpeg -loop 1 -i C:/root/drivers/" + key + ".png -i " + dateDirectory + "/" + key + ".mp3 -shortest -acodec copy -vcodec mjpeg " + dateDirectory + "/" + key + ".mkv";
        response += System.lineSeparator();
        return response;
    }

    private String generateMergeImageAndAudioStringEnding(String dateDirectory, String input) {
        String response = input;
        response += "ffmpeg -safe 0 -f concat -i list.txt -c copy " + dateDirectory +"\\000.mp4";
        response += System.lineSeparator();
        return response;
    }

    private Boolean getPathAndTimestamp() {
        SessionInfo sessionInfo = restTemplate.getForObject(LIVETIMING_URL + SESSION_INFO_URL, SessionInfo.class);
        if (sessionInfo.getType().equals("Race")) {
            relativePath = sessionInfo.getPath();
            SessionData sessionData = restTemplate.getForObject(LIVETIMING_URL + relativePath + "SessionData.json", SessionData.class);
            Optional<SessionDataSeries> finishedOptional = sessionData.getStatusSeries().stream().filter(entry -> "Finished".equals(entry.getSessionStatus())).findFirst();
            if (finishedOptional.isPresent()) {
                finishedDate = finishedOptional.get().getUtc();
                return true;
            }

        }
        return false;
    }

    private RadioData getRadioDataFromObject(Integer id, Object object, List<Driver> drivers) {
        LinkedHashMap<String, Object> dataMap = (LinkedHashMap<String, Object>) object;
        RadioData entry = new RadioData();
        entry.setId(id);
        ZonedDateTime dateTime = ZonedDateTime.parse((String) dataMap.get("Utc"));
        entry.setUtc(dateTime);
        entry.setDriverNumber((String) dataMap.get("RacingNumber"));
        entry.setPath((String) dataMap.get("Path"));
        if (drivers != null) {
            Optional<Driver> driver = drivers.stream().filter(ds -> ds.getNum().equals(entry.getDriverNumber()))
                    .findFirst();
            if (driver.isPresent()) {
                entry.setDriverName(driver.get().firstName + " " + driver.get().lastName);
                driver.get().getRadioData().add(entry);
            } else {
                entry.setDriverName(entry.getPath().substring(10, 16));
                log.error("driver not found: " + entry.getDriverNumber());
            }
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
            case SPRINT:
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
            case SPRINT:
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
        if (!radioData.isEmpty()) {
            radioData.get(0).setActive(true);
        }
        return radioData;
    }
}
