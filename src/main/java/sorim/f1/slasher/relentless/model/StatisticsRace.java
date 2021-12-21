package sorim.f1.slasher.relentless.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.entities.ergast.RaceData;
import sorim.f1.slasher.relentless.model.ergast.ErgastDriver;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticsRace {
    private Integer season;
    private Integer round;
    private String url;
    private String date;
    private String raceName;
    private ErgastDriver winner;

    public StatisticsRace(RaceData race) {
        this.season = Integer.valueOf(race.getSeason());
        this.round = race.getRound();
        this.url = race.getUrl();
        this.date = race.getDate();
        this.raceName = race.getRaceName();
        this.winner = race.getResults().get(0).getDriver();
    }
}
