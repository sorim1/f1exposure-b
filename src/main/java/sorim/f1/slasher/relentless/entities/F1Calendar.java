package sorim.f1.slasher.relentless.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.fortuna.ical4j.model.PropertyList;
import sorim.f1.slasher.relentless.model.enums.RoundEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

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
    private LocalDateTime sprint;
    private String practice1Name;
    private String practice2Name;
    private String practice3Name;
    private String qualifyingName;
    private String raceName;
    private String sprintName;
    private LocalDateTime practice1Original;
    private LocalDateTime practice2Original;
    private LocalDateTime practice3Original;
    private LocalDateTime qualifyingOriginal;
    private LocalDateTime raceOriginal;
    private LocalDateTime sprintOriginal;
    private String ergastDateTime;
    private String ergastName;
    private String location;
    private String summary;

    public F1Calendar(PropertyList properties, Boolean mainCalendar) throws Exception {
        if (mainCalendar) {
            String[] idAndRound = properties.get("UID").get(0).getValue().split("@");
            this.raceId = Integer.valueOf(idAndRound[1]);
            this.location = properties.get("LOCATION").get(0).getValue();
            this.summary = properties.get("SUMMARY").get(0).getValue().replace(" - Practice 1", "");
            this.practice1Name = "Practice 1";
            setDateAndNameFromRoundDescription(idAndRound[0], properties.get("DTSTART").get(0).getValue(), properties.get("SUMMARY").get(0).getValue());
        } else {
            String tempo = "33" + String.valueOf(System.currentTimeMillis()).substring(8, 10) + ThreadLocalRandom.current().nextInt(0, 10000);
            this.raceId = Integer.valueOf(tempo);
            this.location = properties.get("LOCATION").get(0).getValue();
            String summary = properties.get("SUMMARY").get(0).getValue();
            Integer start = summary.lastIndexOf("(") + 1;
            Integer end = summary.lastIndexOf(")");
            this.summary = summary.substring(start, end);
            setDateAndNameFromCategory(properties.get("CATEGORIES").get(0).getValue(), properties.get("DTSTART").get(0).getValue(), properties.get("SUMMARY").get(0).getValue());

        }
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
            case SPRINT:
                this.sprint = dateTime;
                this.sprintOriginal = dateTime;
                this.sprintName = fullSummary.replace(this.summary, "").substring(3);
                break;
        }
    }

    public void setDateAndNameFromCategory(String category, String dateTimeString, String summary) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
        Integer start = summary.indexOf("(");
        Integer end = summary.indexOf(")") + 1;
        // String summary = inputSummary;
        do {
            String deleteMe = summary.substring(start, end);
            summary = summary.replace(deleteMe, "");
            start = summary.indexOf("(");
            end = summary.indexOf(")") + 1;

        } while (start >= 0);

        switch (category) {
            case "Free Practice 1":
            case "FP1,F1":
                this.practice1 = dateTime;
                this.practice1Original = dateTime;
                this.practice1Name = "Free Practice 1";
                break;
            case "Free Practice 2":
            case "FP2,F1":
                this.practice2 = dateTime;
                this.practice2Original = dateTime;
                this.practice2Name = "Free Practice 2";
                break;
            case "Free Practice 3":
            case "FP3,F1":
                this.practice3 = dateTime;
                this.practice3Original = dateTime;
                this.practice3Name = "Free Practice 3";
                break;
            case "Qualifying":
            case "Qualifying,F1":
                this.qualifying = dateTime;
                this.qualifyingOriginal = dateTime;
                this.qualifyingName = "Qualifying";
                break;
            case "Grand Prix":
            case "Grand Prix,F1":
                this.race = dateTime;
                this.raceOriginal = dateTime;
                this.raceName = "Grand Prix";
                break;
            case "Sprint":
            case "Sprint,F1":
                this.sprint = dateTime;
                this.sprintOriginal = dateTime;
                this.sprintName = "Sprint";
                break;
            case "Sprint Shootout":
            case "Sprint Shootout,F1":
                this.practice2 = this.qualifying;
                this.practice2Original = this.qualifyingOriginal;
                this.practice2Name = "Qualifying";
                this.qualifying = dateTime;
                this.qualifyingOriginal = dateTime;
                this.qualifyingName = "Sprint Shootout";
                break;
            default:
                System.out.println("ERROR setDateAndNameFromCategory: " + category);
                break;
        }
    }
}
