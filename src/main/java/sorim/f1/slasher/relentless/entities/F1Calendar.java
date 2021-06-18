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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Integer id;
    @Column(columnDefinition = "race_id")
    private Integer raceId;
    private LocalDateTime practice1;
    private LocalDateTime practice2;
    private LocalDateTime practice3;
    private LocalDateTime qualifying;
    private LocalDateTime race;
    private String location;
    private String summary;

    public F1Calendar(PropertyList properties) throws Exception {
        String[] idAndRound = properties.get("UID").get(0).getValue().split("@");
        this.raceId = Integer.valueOf(idAndRound[1]);
        this.location = properties.get("LOCATION").get(0).getValue();
        this.summary = properties.get("SUMMARY").get(0).getValue().replace(" - Practice 1", "");
        setDateFromRoundDescription(idAndRound[0], properties.get("DTSTART").get(0).getValue());
    }


    public void setDateFromRoundDescription(String roundDesc, String dateTimeString) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
        RoundEnum round = RoundEnum.fromDescription(roundDesc);
        switch (round) {
            case PRACTICE_1:
                this.practice1 = dateTime;
                break;
            case PRACTICE_2:
                this.practice2 = dateTime;
                break;
            case PRACTICE_3:
                this.practice3 = dateTime;
                break;
            case QUALIFYING:
                this.qualifying = dateTime;
                break;
            case RACE:
                this.race = dateTime;
                break;
        }
    }
}
