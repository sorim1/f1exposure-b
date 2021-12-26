package sorim.f1.slasher.relentless.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.model.ergast.ErgastConstructor;
import sorim.f1.slasher.relentless.model.ergast.ErgastDriver;
import sorim.f1.slasher.relentless.model.ergast.ErgastStanding;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConstructorStatistics {

    private ErgastConstructor ergastConstructor;
    private Integer raceCount = 0;
    private Integer winCount = 0;
    private Integer poleCount = 0;
    private Integer lapCount = 0;
    private Integer podiumCount = 0;
    private Integer wccCount = 0;
    private List<ErgastDriver> currentDrivers;
    private List<SeasonStanding> standingsBySeason = new ArrayList<>();
    private List<ChartSeries> pointsThroughSeasons = new ArrayList<>();
    private String wikiSummary;
    private String wikiImage;

    public ConstructorStatistics(ErgastConstructor ergastConstructor) {
        this.ergastConstructor = ergastConstructor;
    }

    public void pushSeasonStanding(Integer season, ErgastStanding es) {
        if(es.getPosition()==1){
            this.wccCount++;
        }
        standingsBySeason.add(new SeasonStanding(season, es));
    }

    public void addPointThroughSeason(Integer season, Integer round, ErgastStanding es) {
        Optional<ChartSeries> cs = pointsThroughSeasons.stream().filter(x -> season.toString().equals(x.getName()))
                .findFirst();
        if (cs.isPresent()){
            List<BigDecimal> newPoint = new ArrayList<>();
            newPoint.add(new BigDecimal(round));
            newPoint.add(es.getPoints());
            cs.get().getSeries().add(newPoint);
        } else {
            pointsThroughSeasons.add(new ChartSeries(season.toString()));
        }
    }

    public void incrementRaceCounts(ErgastStanding es) {
        this.raceCount++;
        this.lapCount =  this.lapCount+es.getLaps();
        if(es.getGrid()==1){
            this.poleCount++;
        }
        if(es.getPosition()<3){
            this.podiumCount++;
            if(es.getPosition()==1){
                this.winCount++;
            }
        }
    }
}
