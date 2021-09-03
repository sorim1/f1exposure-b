package sorim.f1.slasher.relentless.model.livetiming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.entities.ergast.RaceData;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BasicRaceData {

    private String season;
    private String date;
    private Integer round;
    private List<BasicStanding> basicResultList;

    public BasicRaceData(RaceData raceData) {
        this.season = raceData.getSeason();
        this.date = raceData.getDate();
        this.round = raceData.getRound();
        this.basicResultList = new ArrayList<>();
        this.basicResultList.add(new BasicStanding(raceData.getResults().get(0)));
        this.basicResultList.add(new BasicStanding(raceData.getResults().get(1)));
        this.basicResultList.add(new BasicStanding(raceData.getResults().get(2)));
    }
}
