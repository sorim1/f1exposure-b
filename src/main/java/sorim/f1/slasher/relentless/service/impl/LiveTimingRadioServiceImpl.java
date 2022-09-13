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
import sorim.f1.slasher.relentless.handling.Logger;
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
            success = getPostRaceRadioMessages2();
        }
        return String.valueOf(success);
    }

    private Boolean getPostRaceRadioMessages2() {
        List<Driver> drivers = getDriversData();
        String jsonStream = restTemplate.getForObject(LIVETIMING_URL + relativePath + TEAM_RADIO_STREAM, String.class);
        List<RadioData> radioData = getRadioData(null, jsonStream);
        List<RadioData> postRaceRadioData = radioData.stream().filter(entry -> entry.getUtc().isAfter(this.finishedDate)).collect(Collectors.toList());
        Map<Integer, byte[]> radioMap = new TreeMap<>();
        postRaceRadioData.forEach(entry -> {
            byte[] byteArray = restTemplate.getForObject(LIVETIMING_URL + relativePath + entry.getPath(), byte[].class);
            if (!radioMap.containsKey(Integer.valueOf(entry.getDriverNumber()))) {
                radioMap.put(Integer.valueOf(entry.getDriverNumber()), byteArray);
            } else if (radioMap.get(Integer.valueOf(entry.getDriverNumber())).length < byteArray.length) {
                radioMap.put(Integer.valueOf(entry.getDriverNumber()), byteArray);
            }
        });
        saveRadioMessagesToFiles(radioMap, drivers);
        return true;
    }

    private void saveRadioMessagesToFiles(Map<Integer, byte[]> radioMap, List<Driver> drivers) {
        drivers.sort(Comparator.comparing(Driver::getPosition));
        String rootPath = properties.getSaveRadioLocation();
        String directoryName = rootPath + finishedDate.toString().substring(0, 10);
        createRadioDirectories(directoryName);
        AtomicReference<String> mergeImageAndAudioString = new AtomicReference<>("");
        AtomicReference<String> bottomImage = new AtomicReference<>("ffmpeg ");
        AtomicReference<String> topImageString = new AtomicReference<>("");
        AtomicReference<String> mkvListString = new AtomicReference<>("");

        drivers.forEach(driver->{
            bottomImage.set(generateBottomImageString(bottomImage.get(), String.valueOf(driver.getNum()), driver.getPosition()));
            topImageString.set(generateTopImageString(topImageString.get(), String.valueOf(driver.getNum()), driver.getPosition()));

            if(radioMap.containsKey(Integer.valueOf(driver.getNum()))){
            mergeImageAndAudioString.set(generateMergeImageAndAudioString(directoryName, mergeImageAndAudioString.get(), String.valueOf(driver.getNum())));
            mkvListString.set(generateMkvListString(directoryName, mkvListString.get(), String.valueOf(driver.getNum())));

            File file = new File(directoryName + "/mp3/" + driver.getNum() + ".mp3");
            try {
                OutputStream os = new FileOutputStream(file);
                os.write(radioMap.get(Integer.valueOf(driver.getNum())));
                os.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            }
        });
        try {
            bottomImage.set(closeImagesString(bottomImage.get(), drivers.size()));
            mergeImageAndAudioString.set(generateMergeImageAndAudioStringEnding(directoryName, mergeImageAndAudioString.get()));

            BufferedWriter writer = new BufferedWriter(new FileWriter(directoryName + "/generate.bat"));
            writer.write(bottomImage.get() + topImageString.get() + mergeImageAndAudioString.get());
            writer.close();
            writer = new BufferedWriter(new FileWriter(directoryName + "/list.txt"));
            writer.write(mkvListString.get());
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void createRadioDirectories(String directoryName) {
        File directory = new File(directoryName);
        if (!directory.exists()) {
            directory.mkdir();
        }
        File directory2 = new File(directoryName +"/mp3");
        if (!directory2.exists()) {
            directory2.mkdir();
        }
        File directory3 = new File(directoryName +"/images");
        if (!directory3.exists()) {
            directory3.mkdir();
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
        response += "ffmpeg -loop 1 -i images/F" + key + ".png -i " + "mp3/" + key + ".mp3 -af volume=+6dB -shortest -tune stillimage " + dateDirectory + "/" + key + ".mkv";
        response += System.lineSeparator();
        return response;
    }
    private String generateBottomImageString(String input, String key, Integer position) {
        String response = input;
         response += "-i C:/root/drivers/L" + key + ".png ";
         if(position==10){
             response += "-filter_complex \"hstack=inputs=" + 10 + "[v]\" -map \"[v]\" images/bottom-image-1.png";
             response += System.lineSeparator();
             response += "ffmpeg ";
         }
        return response;
    }
    private String generateTopImageString(String input, String key, Integer position) {
        String response = input;
        response += "ffmpeg -i C:/root/drivers/P"+position+".png -i C:/root/drivers/T"+key+".png -filter_complex hstack images/T" +key+ ".png";
        response += System.lineSeparator();
        response += "ffmpeg -i images/T"+key+".png -i images/bottom-image.png -filter_complex vstack images/F" +key+ ".png";
        response += System.lineSeparator();
        return response;
    }
    private String closeImagesString(String input, Integer size) {
        String response = input;
        response += "-filter_complex \"hstack=inputs=" + (size-10) + "[v]\" -map \"[v]\" images/bottom-image-2.png";
        response += System.lineSeparator();
        response += "ffmpeg -i images/bottom-image-1.png -i images/bottom-image-2.png -filter_complex vstack images/bottom-image.png";
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
    private List<Driver>  getDriversData() {
        log.info(LIVETIMING_URL + relativePath + "SPFeed.json");
        String liveTimingResponse = restTemplate.getForObject(LIVETIMING_URL + relativePath + "SPFeed.json", String.class);
        liveTimingResponse = liveTimingResponse.substring(liveTimingResponse.indexOf("{"));
        List<Driver> driversResponse = null;
        try {
            LiveTimingData response = mapper.readValue(liveTimingResponse, LiveTimingData.class);
            driversResponse = response.getInit().getData().getDrivers();
            List<LinkedHashMap> freeDr = (List<LinkedHashMap>) response.getFree().data.getDataField("DR");

            for (int i = 0; i < driversResponse.size(); i++) {
                List<String> fData = (List<String>) freeDr.get(i).get("F");
                driversResponse.get(i).setPosition(Integer.valueOf(fData.get(3)));
            }
        } catch (JsonProcessingException e) {
            Logger.log("getDriversData failed", e.getMessage());
        }
        driversResponse.sort(Comparator.comparing(Driver::getPosition));
        return driversResponse;
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
