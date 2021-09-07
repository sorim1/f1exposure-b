package sorim.f1.slasher.relentless.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.fortuna.ical4j.model.PropertyList;
import sorim.f1.slasher.relentless.model.enums.RoundEnum;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "CALENDAR")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class F1Calendar {

    @Id
    @Column(columnDefinition = "race_id")
    private Integer raceId;
    private LocalDateTime practice1;
    private LocalDateTime practice2;
    private LocalDateTime practice3;
    private LocalDateTime qualifying;
    private LocalDateTime race;
    private String practice1Name;
    private String practice2Name;
    private String practice3Name;
    private String qualifyingName;
    private String raceName;
    private LocalDateTime practice1Original;
    private LocalDateTime practice2Original;
    private LocalDateTime practice3Original;
    private LocalDateTime qualifyingOriginal;
    private LocalDateTime raceOriginal;
    private String ergastDateTime;
    private String ergastName;
    private String location;
    private String summary;

    public F1Calendar(PropertyList properties) throws Exception {
        String[] idAndRound = properties.get("UID").get(0).getValue().split("@");
        this.raceId = Integer.valueOf(idAndRound[1]);
        this.location = properties.get("LOCATION").get(0).getValue();
        this.summary = properties.get("SUMMARY").get(0).getValue().replace(" - Practice 1", "");
        this.practice1Name = "Practice 1";
        setDateAndNameFromRoundDescription(idAndRound[0], properties.get("DTSTART").get(0).getValue(), properties.get("SUMMARY").get(0).getValue());
    }

    public void setDateAndNameFromRoundDescription(String roundDesc, String dateTimeString, String fullSummary) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
        RoundEnum round = RoundEnum.fromDescription(roundDesc);
        switch (round) {
            case PRACTICE_1:
                this.practice1 = dateTime;
                this.practice1Original = dateTime;
                this.practice1Name = fullSummary.replace(this.summary, "").substring(3);
                break;
            case PRACTICE_2:
                this.practice2 = dateTime;
                this.practice2Original = dateTime;
                this.practice2Name = fullSummary.replace(this.summary, "").substring(3);
                break;
            case PRACTICE_3:
                this.practice3 = dateTime;
                this.practice3Original = dateTime;
                this.practice3Name = fullSummary.replace(this.summary, "").substring(3);
                break;
            case QUALIFYING:
                this.qualifying = dateTime;
                this.qualifyingOriginal = dateTime;
                this.qualifyingName = fullSummary.replace(this.summary, "").substring(3);
                break;
            case RACE:
                this.race = dateTime;
                this.raceOriginal = dateTime;
                this.raceName = fullSummary.replace(this.summary, "").substring(3);
                break;
        }
    }
}
