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
import java.util.concurrent.atomic.AtomicReference;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverStatistics {

    private String driverId;
    private Integer permanentNumber;
    private String code;
    private String url;
    private String givenName;
    private String familyName;
    private String dateOfBirth;
    private String nationality;
    private Integer raceCount = 0;
    private Integer winCount = 0;
    private Integer poleCount = 0;
    private Integer lapCount = 0;
    private Integer podiumCount = 0;
    private Integer wdcCount = 0;
    private List<String> wdcList = new ArrayList<>();
    private ErgastConstructor currentConstructor;
    private List<SeasonStanding> standingsBySeason = new ArrayList<>();
    private List<ChartSeries> pointsThroughSeasons = new ArrayList<>();
    private List<List<String>> constructorHistory = new ArrayList<>();

    private String wikiSummary;
    private String wikiImage;

    public DriverStatistics(ErgastDriver ergastDriver) {
        this.driverId = ergastDriver.getDriverId();
        this.permanentNumber = ergastDriver.getPermanentNumber();
        this.code = ergastDriver.getCode();
        this.url = ergastDriver.getUrl();
        this.givenName = ergastDriver.getGivenName();
        this.familyName = ergastDriver.getFamilyName();
        this.dateOfBirth = ergastDriver.getDateOfBirth();
        this.nationality = ergastDriver.getNationality();
    }

    public void pushSeasonStanding(Integer season, ErgastStanding es) {
        if(es.getPosition()==1){
            this.wdcCount++;
            this.wdcList.add(season + " - " + extractConstructor(es.getConstructors()));
        }
        standingsBySeason.add(new SeasonStanding(season, es));

        es.getConstructors().forEach(team->{
            List<String> newConstructorEntry = new ArrayList<>();
            newConstructorEntry.add(String.valueOf(season));
            newConstructorEntry.add(team.getConstructorId());
            newConstructorEntry.add(team.getName());
            constructorHistory.add(newConstructorEntry);
        });
    }

    private String extractConstructor(List<ErgastConstructor> constructors) {
        if(constructors.size()==1){
            return constructors.get(0).getName();
        } else if(constructors.size()>1){
            AtomicReference<String> ar = new AtomicReference<>("");
            constructors.forEach(cs->{
                ar.set(ar.get() + cs.getName() + " / ");
            });
            return ar.get().substring(0, ar.get().length()-3);
        }
        return null;
    }

    public void incrementRaceCounts(ErgastStanding es) {
        this.raceCount++;
        this.lapCount =  this.lapCount+es.getLaps();
        if(es.getGrid()==1){
            this.poleCount++;
        }
        if(es.getPosition()<4){
            this.podiumCount++;
            if(es.getPosition()==1){
                this.winCount++;
            }
        }
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

}
