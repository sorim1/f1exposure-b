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

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LiveTimingRadioServiceImpl implements LiveTimingRadioService {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final TypeReference<Map<String, Object>> RADIO_ENTRY_TYPE = new TypeReference<>() {
    };
    private static final String SESSION_INFO_URL = "SessionInfo.json";
    private static final String TEAM_RADIO_STREAM = "TeamRadio.jsonStream";
    private static final String LIVETIMING_URL = "https://livetiming.formula1.com/static/";

    private final MainProperties properties;
    private final RestTemplate restTemplate;
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
        if (drivers.isEmpty()) {
            log.warn("Unable to load driver data; skipping radio generation");
            return false;
        }

        String jsonStream = restTemplate.getForObject(LIVETIMING_URL + relativePath + TEAM_RADIO_STREAM, String.class);
        List<RadioData> radioData = getRadioData(null, jsonStream);
        List<RadioData> postRaceRadioData = radioData.stream()
                .filter(entry -> entry.getUtc().isAfter(this.finishedDate))
                .collect(Collectors.toList());
        Map<Integer, byte[]> radioMap = new TreeMap<>();
        postRaceRadioData.forEach(entry -> {
            byte[] byteArray = restTemplate.getForObject(LIVETIMING_URL + relativePath + entry.getPath(), byte[].class);
            if (byteArray == null) {
                return;
            }
            radioMap.merge(Integer.valueOf(entry.getDriverNumber()), byteArray, (existing, replacement) ->
                    existing.length >= replacement.length ? existing : replacement);
        });
        saveRadioMessagesToFiles(radioMap, drivers);
        return true;
    }

    private void saveRadioMessagesToFiles(Map<Integer, byte[]> radioMap, List<Driver> drivers) {
        drivers.sort(Comparator.comparing(Driver::getPosition));
        Path directory = Paths.get(properties.getSaveRadioLocation(), finishedDate.toString().substring(0, 10));
        createRadioDirectories(directory);

        StringBuilder mergeImageAndAudio = new StringBuilder();
        StringBuilder bottomImage = new StringBuilder("ffmpeg ");
        StringBuilder topImage = new StringBuilder();
        StringBuilder mkvList = new StringBuilder();

        drivers.forEach(driver -> {
            String driverNumber = String.valueOf(driver.getNum());
            appendBottomImageCommand(bottomImage, driverNumber, driver.getPosition());
            appendTopImageCommand(topImage, driverNumber, driver.getPosition());

            byte[] radioBytes = radioMap.get(Integer.valueOf(driverNumber));
            if (radioBytes != null) {
                appendMergeImageAndAudioCommand(mergeImageAndAudio, directory, driverNumber);
                appendMkvListEntry(mkvList, directory, driverNumber);
                Path targetFile = directory.resolve("mp3").resolve(driverNumber + ".mp3");
                try {
                    Files.write(targetFile, radioBytes);
                } catch (IOException e) {
                    throw new IllegalStateException("Failed to write radio message for driver " + driverNumber, e);
                }
            }
        });

        appendCloseImagesCommand(bottomImage, drivers.size());
        appendMergeImageAndAudioEnding(mergeImageAndAudio, directory);

        try (BufferedWriter writer = Files.newBufferedWriter(directory.resolve("generate.bat"))) {
            writer.append(bottomImage).append(topImage).append(mergeImageAndAudio);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write generate.bat", e);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(directory.resolve("list.txt"))) {
            writer.append(mkvList);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write list.txt", e);
        }
    }

    private void createRadioDirectories(Path directory) {
        try {
            Files.createDirectories(directory);
            Files.createDirectories(directory.resolve("mp3"));
            Files.createDirectories(directory.resolve("images"));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create radio directories", e);
        }
    }

    private void appendMkvListEntry(StringBuilder builder, Path directory, String driverNumber) {
        builder.append("file '")
                .append(directory.resolve(driverNumber + ".mkv"))
                .append("'")
                .append(System.lineSeparator());
    }

    private void appendMergeImageAndAudioCommand(StringBuilder builder, Path directory, String driverNumber) {
        builder.append("ffmpeg -loop 1 -i images/F")
                .append(driverNumber)
                .append(".png -i mp3/")
                .append(driverNumber)
                .append(".mp3 -af volume=+6dB -shortest -tune stillimage ")
                .append(directory.resolve(driverNumber + ".mkv"))
                .append(System.lineSeparator());
    }

    private void appendBottomImageCommand(StringBuilder builder, String driverNumber, Integer position) {
        builder.append("-i C:/root/drivers/L")
                .append(driverNumber)
                .append(".png ");
        if (position == 10) {
            builder.append("-filter_complex \"hstack=inputs=10[v]\" -map \"[v]\" images/bottom-image-1.png")
                    .append(System.lineSeparator())
                    .append("ffmpeg ");
        }
    }

    private void appendTopImageCommand(StringBuilder builder, String driverNumber, Integer position) {
        builder.append("ffmpeg -i C:/root/drivers/P")
                .append(position)
                .append(".png -i C:/root/drivers/T")
                .append(driverNumber)
                .append(".png -filter_complex hstack images/T")
                .append(driverNumber)
                .append(".png")
                .append(System.lineSeparator())
                .append("ffmpeg -i images/T")
                .append(driverNumber)
                .append(".png -i images/bottom-image.png -filter_complex vstack images/F")
                .append(driverNumber)
                .append(".png")
                .append(System.lineSeparator());
    }

    private void appendCloseImagesCommand(StringBuilder builder, int size) {
        int remaining = size - 10;
        if (remaining > 0) {
            builder.append("-filter_complex \"hstack=inputs=")
                    .append(remaining)
                    .append("[v]\" -map \"[v]\" images/bottom-image-2.png")
                    .append(System.lineSeparator())
                    .append("ffmpeg -i images/bottom-image-1.png -i images/bottom-image-2.png -filter_complex vstack images/bottom-image.png")
                    .append(System.lineSeparator());
        } else {
            builder.append(System.lineSeparator());
        }
    }

    private void appendMergeImageAndAudioEnding(StringBuilder builder, Path directory) {
        builder.append("ffmpeg -safe 0 -f concat -i list.txt -c copy ")
                .append(directory.resolve("000.mp4"))
                .append(System.lineSeparator());
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

    private List<Driver> getDriversData() {
        log.info(LIVETIMING_URL + relativePath + "SPFeed.json");
        String liveTimingResponse = restTemplate.getForObject(LIVETIMING_URL + relativePath + "SPFeed.json", String.class);
        if (liveTimingResponse == null || !liveTimingResponse.contains("{")) {
            return Collections.emptyList();
        }
        String jsonPayload = liveTimingResponse.substring(liveTimingResponse.indexOf("{"));
        try {
            LiveTimingData response = MAPPER.readValue(jsonPayload, LiveTimingData.class);
            List<Driver> driversResponse = response.getInit().getData().getDrivers();
            List<?> freeDr = (List<?>) response.getFree().data.getDataField("DR");
            if (freeDr == null || freeDr.isEmpty()) {
                return Collections.emptyList();
            }

            AtomicInteger index = new AtomicInteger();
            driversResponse.forEach(driver -> {
                if (index.get() >= freeDr.size()) {
                    return;
                }
                Object entry = freeDr.get(index.getAndIncrement());
                if (entry instanceof Map<?, ?> map) {
                    Object field = map.get("F");
                    if (field instanceof List<?> values && values.size() > 3) {
                        driver.setPosition(Integer.valueOf(values.get(3).toString()));
                    }
                }
            });
            driversResponse.sort(Comparator.comparing(Driver::getPosition));
            return driversResponse;
        } catch (JsonProcessingException e) {
            Logger.log("getDriversData failed", e.getMessage());
            return Collections.emptyList();
        }
    }

    private RadioData getRadioDataFromObject(Integer id, Map<String, Object> dataMap, List<Driver> drivers) {
        RadioData entry = new RadioData();
        entry.setId(id);
        Object utcValue = dataMap.get("Utc");
        if (utcValue instanceof String utc) {
            entry.setUtc(ZonedDateTime.parse(utc));
        }
        entry.setDriverNumber((String) dataMap.get("RacingNumber"));
        entry.setPath((String) dataMap.get("Path"));
        if (drivers != null) {
            drivers.stream()
                    .filter(ds -> ds.getNum().equals(entry.getDriverNumber()))
                    .findFirst()
                    .ifPresentOrElse(driver -> {
                        entry.setDriverName(driver.firstName + " " + driver.lastName);
                        driver.getRadioData().add(entry);
                    }, () -> {
                        entry.setDriverName(resolveDriverNameFallback(entry));
                        log.error("driver not found: {}", entry.getDriverNumber());
                    });
        }
        return entry;
    }

    private String resolveDriverNameFallback(RadioData entry) {
        if (entry.getPath() != null && entry.getPath().length() >= 16) {
            return entry.getPath().substring(10, 16);
        }
        return entry.getDriverNumber();
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
            case SPRINT_SHOOTOUT:
                upcomingRaceAnalysis.setSprintQuali(drivers);
                upcomingRaceAnalysis.setSprintQualiRadio(radioData);
                break;
            case SPRINT:
                upcomingRaceAnalysis.setSprint(drivers);
                upcomingRaceAnalysis.setSprintRadio(radioData);
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
            case SPRINT_SHOOTOUT:
                return upcomingRaceAnalysis.getSprintQuali();
            case SPRINT:
                return upcomingRaceAnalysis.getSprint();
            default:
                log.error("CASE NE POSTOJI: " + session);
                return Collections.emptyList();
        }
    }

    public List<RadioData> getRadioData(List<Driver> drivers, String jsonStream) {
        if (jsonStream == null || jsonStream.isBlank()) {
            return Collections.emptyList();
        }

        List<RadioData> radioData = new ArrayList<>();
        String[] lines = jsonStream.split(System.lineSeparator());
        for (String stringLine : lines) {
            String trimmedLine = stringLine.trim();
            if (trimmedLine.isEmpty()) {
                continue;
            }
            int jsonStart = trimmedLine.indexOf('{');
            if (jsonStart < 0) {
                log.debug("Skipping malformed radio stream line: {}", trimmedLine);
                continue;
            }
            Map<String, Object> mapping;
            try {
                mapping = MAPPER.readValue(trimmedLine.substring(jsonStart), RADIO_ENTRY_TYPE);
            } catch (JsonProcessingException e) {
                log.error("Unable to parse radio stream line", e);
                continue;
            }

            Object captures = mapping.get("Captures");
            if (captures instanceof List<?> capturesList) {
                capturesList.stream()
                        .filter(Map.class::isInstance)
                        .map(Map.class::cast)
                        .map(map -> getRadioDataFromObject(0, castToStringObjectMap(map), drivers))
                        .forEach(radioData::add);
            } else if (captures instanceof Map<?, ?> capturesMap) {
                capturesMap.forEach((key, value) -> {
                    if (value instanceof Map<?, ?> dataMap) {
                        RadioData entry = getRadioDataFromObject(Integer.valueOf(key.toString()), castToStringObjectMap(dataMap), drivers);
                        radioData.add(entry);
                    }
                });
            }
        }
        if (!radioData.isEmpty()) {
            radioData.get(0).setActive(true);
        }
        return radioData;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> castToStringObjectMap(Map<?, ?> source) {
        return (Map<String, Object>) source;
    }
}
