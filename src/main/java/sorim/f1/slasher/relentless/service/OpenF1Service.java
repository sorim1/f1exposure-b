package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.model.openf1.*;

import java.time.LocalDateTime;
import java.util.List;

public interface OpenF1Service {
    List<CarDataDto> getCarData(Integer driverNumber, Integer sessionKey, Integer speed);
    List<DriverDto> getDrivers(Integer driverNumber, Integer sessionKey);
    List<IntervalDto> getIntervals(Integer sessionKey, Double interval);
    List<LapDto> getLaps(Integer sessionKey, Integer driverNumber, Integer lapNumber);
    List<LocationDto> getLocations(Integer sessionKey, Integer driverNumber, LocalDateTime startDate, LocalDateTime endDate);
    List<MeetingDto> getMeetings(Integer year, String countryName);
    List<PitDto> getPitData(Integer sessionKey, Double pitDuration);
    List<PositionDto> getPositions(Integer meetingKey, Integer driverNumber, Integer position);
    List<RaceControlDto> getTodayRaceControlData(String flag);
    List<SessionDto> getSessions(String countryName, String sessionName, Integer year);
    List<StintDto> getStints(Integer sessionKey, Integer tyreAgeAtStart);
    List<TeamRadioDto> getTeamRadioMessages(Integer sessionKey, Integer driverNumber);
    List<WeatherDto> getWeatherData(Integer meetingKey, Integer windDirection, Double trackTemperature);
    // Additional methods for any other endpoints...
}
