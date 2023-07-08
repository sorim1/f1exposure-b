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
public class FrontendRace {
    private Integer round;
    private String raceName;
    private String url;
    private String date;
    private String circuitName;
    private String circuitId;
    private String x;
    private String y;
    private ErgastDriver driver;
    private String  exposureWinner;

    public FrontendRace(RaceData raceData) {
        this.round= raceData.getRound();
        this.raceName= raceData.getRaceName();
        this.url = raceData.getUrl();
        this.date = raceData.getDate();
        this.circuitName = raceData.getCircuit().getCircuitName();
        this.circuitId = raceData.getCircuit().getCircuitId();
        this.x = raceData.getCircuit().getLocation().getLat();
        this.y = raceData.getCircuit().getLocation().getLongitude();
        if(raceData.getResults()!=null){
        this.driver = raceData.getResults().get(0).getDriver();
        }
    }
}
