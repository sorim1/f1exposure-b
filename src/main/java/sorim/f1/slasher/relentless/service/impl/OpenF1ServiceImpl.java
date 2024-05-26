package sorim.f1.slasher.relentless.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sorim.f1.slasher.relentless.model.openf1.*;
import sorim.f1.slasher.relentless.service.OpenF1Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OpenF1ServiceImpl implements OpenF1Service {
    private final RestTemplate restTemplate;

    private final String baseUrl = "https://api.openf1.org/v1/";

    @Override
    public List<CarDataDto> getCarData(Integer driverNumber, Integer sessionKey, Integer speed) {
        String url = baseUrl + "car_data?driver_number=" + driverNumber + "&session_key=" + sessionKey + "&speed>=" + speed;
        return makeApiCall(url, new ParameterizedTypeReference<>() {
        });
    }

    @Override
    public List<DriverDto> getDrivers(Integer driverNumber, Integer sessionKey) {
        String url = baseUrl + "drivers?driver_number=" + driverNumber + "&session_key=" + sessionKey;
        return makeApiCall(url, new ParameterizedTypeReference<List<DriverDto>>() {});
    }


    private <T> List<T> makeApiCall(String url, ParameterizedTypeReference<List<T>> responseType) {
        ResponseEntity<List<T>> response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
        return response.getBody();
    }

    @Override
    public List<IntervalDto> getIntervals(Integer sessionKey, Double interval) {
        String url = baseUrl + "intervals?session_key=" + sessionKey + "&interval<" + interval;
        return makeApiCall(url, new ParameterizedTypeReference<List<IntervalDto>>() {});
    }

    @Override
    public List<LapDto> getLaps(Integer sessionKey, Integer driverNumber, Integer lapNumber) {
        String url = baseUrl + "laps?session_key=" + sessionKey + "&driver_number=" + driverNumber + "&lap_number=" + lapNumber;
        return makeApiCall(url, new ParameterizedTypeReference<List<LapDto>>() {});
    }

    @Override
    public List<LocationDto> getLocations(Integer sessionKey, Integer driverNumber, LocalDateTime startDate, LocalDateTime endDate) {
        // Ensure dates are formatted properly
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        String url = baseUrl + "location?session_key=" + sessionKey + "&driver_number=" + driverNumber +
                "&date>" + startDate.format(formatter) + "&date<" + endDate.format(formatter);
        return makeApiCall(url, new ParameterizedTypeReference<List<LocationDto>>() {});
    }
    @Override
    public List<MeetingDto> getMeetings(Integer year, String countryName) {
        String url = baseUrl + "meetings?year=" + year + "&country_name=" + countryName;
        return makeApiCall(url, new ParameterizedTypeReference<List<MeetingDto>>() {});
    }

    @Override
    public List<PitDto> getPitData(Integer sessionKey, Double pitDuration) {
        String url = baseUrl + "pit?session_key=" + sessionKey + "&pit_duration<" + pitDuration;
        return makeApiCall(url, new ParameterizedTypeReference<List<PitDto>>() {});
    }

    @Override
    public List<PositionDto> getPositions(Integer meetingKey, Integer driverNumber, Integer position) {
        String url = baseUrl + "position?meeting_key=" + meetingKey + "&driver_number=" + driverNumber + "&position<=" + position;
        return makeApiCall(url, new ParameterizedTypeReference<List<PositionDto>>() {});
    }

    @Override
    public List<RaceControlDto> getTodayRaceControlData(String flag, Integer triggerLap) {
        LocalDate today = LocalDate.now();
        String formattedToday = today.format(DateTimeFormatter.ISO_LOCAL_DATE);
        String url = baseUrl + "race_control?date>=" + formattedToday;
        if(triggerLap!=null){
            url += "&lap_number>" + triggerLap;
        } else if(flag!=null){
            url += "&flag=" + flag;
        }
        log.info(url);
        return makeApiCall(url, new ParameterizedTypeReference<>() {
        });
    }

    @Override
    public List<SessionDto> getSessions(String countryName, String sessionName, Integer year) {
        String url = baseUrl + "sessions?country_name=" + countryName + "&session_name=" + sessionName + "&year=" + year;
        return makeApiCall(url, new ParameterizedTypeReference<List<SessionDto>>() {});
    }

    @Override
    public List<StintDto> getStints(Integer sessionKey, Integer tyreAgeAtStart) {
        String url = baseUrl + "stints?session_key=" + sessionKey + "&tyre_age_at_start>=" + tyreAgeAtStart;
        return makeApiCall(url, new ParameterizedTypeReference<List<StintDto>>() {});
    }

    @Override
    public List<TeamRadioDto> getTeamRadioMessages(Integer sessionKey, Integer driverNumber) {
        String url = baseUrl + "team_radio?session_key=" + sessionKey + "&driver_number=" + driverNumber;
        return makeApiCall(url, new ParameterizedTypeReference<List<TeamRadioDto>>() {});
    }

    @Override
    public List<WeatherDto> getWeatherData(Integer meetingKey, Integer windDirection, Double trackTemperature) {
        String url = baseUrl + "weather?meeting_key=" + meetingKey + "&wind_direction>=" + windDirection + "&track_temperature>=" + trackTemperature;
        return makeApiCall(url, new ParameterizedTypeReference<List<WeatherDto>>() {});
    }

}
