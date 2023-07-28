package sorim.f1.slasher.relentless.model.livetiming;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Driver {
    @JsonProperty("BroadcastName")
    public String name;
    @JsonProperty("Tla")
    public String initials;
    @JsonProperty("FullName")
    public String fullName;
    @JsonProperty("FirstName")
    public String firstName;
    @JsonProperty("LastName")
    public String lastName;
    @JsonProperty("TeamColour")
    public String color;
    @JsonProperty("TeamName")
    public String team;
    @JsonProperty("RacingNumber")
    public String num;
    @JsonProperty("HeadshotUrl")
    public String headshotUrl;
    @JsonProperty("Line")
    public String line;
    @JsonProperty("Reference")
    public String reference;
    @JsonProperty("CountryCode")
    public String countryCode;
    @JsonProperty("NameFormat")
    public String nameFormat;

    //TODO delete all properties
//    Integer steering;
//    Integer gforceLat;
//    Integer gforceLong;
//    Integer brake;
//    Integer performance;
//    Integer throttle;
//    private String finalGap;
//    private String fastestLap;
//    private Integer fastestLapPosition;
//    private Integer fastestLapNumber;
//    private String fastestLapSector1;
//    private Integer fastestLapPositionSector1;
//    private String fastestLapSector2;
//    private Integer fastestLapPositionSector2;
//    private String fastestLapSector3;
//    private Integer fastestLapPositionSector3;

    private Integer position;
    private Integer startingPosition;
    private String ergastCode;
    private String finishStatus;
    //    private Integer points;
//    private BigDecimal standingsNewAveragePoints;
//    private BigDecimal standingsAverageDifference;
    private List<Integer> pitstops = new ArrayList<>();
    private LapByLapData lapByLapData = new LapByLapData();
    private List<RadioData> radioData = new ArrayList<>();

    private TimingDataF1 timingDataF1;
}
